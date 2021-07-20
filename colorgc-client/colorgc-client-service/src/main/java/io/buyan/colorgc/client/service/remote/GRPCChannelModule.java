package io.buyan.colorgc.client.service.remote;


import io.buyan.colorgc.client.service.Module;
import io.buyan.colorgc.client.service.conf.Config;
import io.buyan.colorgc.common.utils.StringUtils;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GRPC 连接管理模块
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
@Slf4j
public class GRPCChannelModule implements Module, Runnable {

    /**
     * GRPC 连接
     */
    private volatile GRPCChannel channel = null;

    /**
     * ColorGC 服务端地址列表
     */
    private volatile List<String> serverList;

    /**
     * GRPC 连接状态监听器
     */
    private final List<GRPCChannelListener> listeners = Collections.synchronizedList(new LinkedList<>());

    /**
     * 当前选中的要连接的服务端下标
     */
    private AtomicInteger selectedIndex = new AtomicInteger(-1);

    /**
     * 是否需要重新与服务端建立连接标识符
     */
    private volatile boolean reconnect = true;

    /**
     * 维护 GRPC 连接状态的定时任务
     */
    private volatile ScheduledFuture<?> connectStatusCheckFuture;

    @Override
    public void prepare() throws Throwable {

    }

    @Override
    public void boot() throws Throwable {
        if (StringUtils.isEmpty(Config.SERVER_LIST)) {
            log.error("Config item server_list can not be empty.");
            return;
        }
        serverList = Arrays.asList(Config.SERVER_LIST.split(","));
        connectStatusCheckFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        if (null != connectStatusCheckFuture) {
            connectStatusCheckFuture.cancel(true);
        }
        if (null != channel) {
            channel.shutdownNow();
        }
        log.info("GRPCChannel module shutdown.");
    }

    @Override
    public void run() {
        if (reconnect) {
            if (!serverList.isEmpty()) {
                try {
                    String serverAddress = serverList.get(getNextServerIndex());
                    String[] ipAndPort = serverAddress.split(":");
                    if (null != channel) {
                        channel.shutdownNow();
                    }
                    channel = new GRPCChannel(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                    notify(GRPCChannelStatus.CONNECTED);
                    reconnect = false;
                } catch (Throwable e) {
                    log.error("Establish connections with server failed.");
                }
            }
        }
    }

    /**
     * 获取 GRPC 原生 Channel
     * @return GRPC 原生 Channel
     */
    public Channel getChannel() {
        return this.channel.getChannel();
    }

    /**
     * 添加 GRPC 连接状态监听器
     * @param listener GRPC 连接状态监听器
     */
    public void addChannelListener(GRPCChannelListener listener) {
        listeners.add(listener);
    }

    /**
     * 通知连接状态变更
     * @param status GRPC 连接状态
     */
    private void notify(GRPCChannelStatus status) {
        for (GRPCChannelListener listener : listeners) {
            try {
                listener.statusChanged(status);
            } catch (Throwable e) {
                log.error("Notify GRPCChannel status to {} failed.", listener.getClass().getName());
            }
        }
    }

    /**
     * 获取下一个要尝试建立连接的服务端地址下标
     * @return 服务端地址下标
     */
    private int getNextServerIndex() {
        if (selectedIndex.get() >= serverList.size() - 1) {
            selectedIndex.set(-1);
        }
        return selectedIndex.incrementAndGet();
    }

    public void reportError(Throwable throwable) {
        if (isNetworkError(throwable)) {
            reconnect = true;
            notify(GRPCChannelStatus.DISCONNECTED);
        }
    }

    /**
     * 检查是否为网络相关异常
     * @param throwable 异常信息
     * @return true 网络相关异常 false 非网络相关异常
     */
    private boolean isNetworkError(Throwable throwable) {
        if (throwable instanceof StatusRuntimeException) {
            StatusRuntimeException statusRuntimeException = (StatusRuntimeException) throwable;
            return statusEquals(
                    statusRuntimeException.getStatus(), Status.UNAVAILABLE, Status.PERMISSION_DENIED,
                    Status.UNAUTHENTICATED, Status.RESOURCE_EXHAUSTED, Status.UNKNOWN
            );
        }
        return false;
    }

    /**
     * 判断指定状态是否存在于状态列表中
     * @param source 指定状态
     * @param potentialStatus 状态列表
     * @return true 存在 false 不存在
     */
    public boolean statusEquals(Status source, Status... potentialStatus) {
        for (Status status : potentialStatus) {
            if (source.getCode() == status.getCode()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int priority() {
        return 1;
    }
}
