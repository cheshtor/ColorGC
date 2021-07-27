package io.buyan.colorgc.client.service.heartbeat;

import io.buyan.colorgc.client.service.Module;
import io.buyan.colorgc.client.service.ModuleManager;
import io.buyan.colorgc.client.service.conf.Config;
import io.buyan.colorgc.client.service.remote.GRPCChannelListener;
import io.buyan.colorgc.client.service.remote.GRPCChannelModule;
import io.buyan.colorgc.client.service.remote.GRPCChannelStatus;
import io.buyan.colorgc.common.utils.OSUtils;
import io.buyan.colorgc.common.utils.StringUtils;
import io.buyan.colorgc.protocol.heartbeat.HeartbeatRequest;
import io.buyan.colorgc.protocol.heartbeat.HeartbeatResponse;
import io.buyan.colorgc.protocol.heartbeat.HeartbeatServiceGrpc;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 心跳模块
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
@Slf4j
public class HeartbeatModule implements Module, GRPCChannelListener, Runnable {

    /**
     * 网络连接状态
     */
    private volatile GRPCChannelStatus channelStatus = GRPCChannelStatus.DISCONNECTED;

    /**
     * 心跳定时任务
     */
    private volatile ScheduledFuture<?> heartbeatFuture;

    /**
     * 心跳请求
     */
    private volatile HeartbeatServiceGrpc.HeartbeatServiceBlockingStub heartbeatServiceBlockingStub;

    @Override
    public void statusChanged(GRPCChannelStatus status) {
        log.error("连接状态发生变化");
        if (GRPCChannelStatus.CONNECTED.equals(status)) {
            Channel channel = ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).getChannel();
            heartbeatServiceBlockingStub = HeartbeatServiceGrpc.newBlockingStub(channel);
        } else {
            heartbeatServiceBlockingStub = null;
        }
        this.channelStatus = status;
    }

    @Override
    public void prepare() throws Throwable {
        ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).addChannelListener(this);
    }

    @Override
    public void boot() throws Throwable {
        heartbeatFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this, 0, Config.HEARTBEAT_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        heartbeatFuture.cancel(true);
    }

    @Override
    public void run() {
        if (GRPCChannelStatus.CONNECTED.equals(channelStatus)) {
            if (null != heartbeatServiceBlockingStub) {
                try {
                    String ip = OSUtils.getIPV4();
                    int processNo = OSUtils.getProcessNo();
                    HeartbeatRequest heartbeatRequest = HeartbeatRequest.newBuilder()
                            .setServiceName(Config.SERVICE_NAME)
                            .setIp(ip)
                            .setProcessNo(processNo)
                            .build();
                    HeartbeatResponse response = heartbeatServiceBlockingStub
                            .withDeadlineAfter(1, TimeUnit.SECONDS)
                            .heartbeat(heartbeatRequest);
                    if (StringUtils.isEmpty(Config.NodeInfo.SERVICE_CODE)) {
                        if (ip.equals(response.getIp()) && processNo == response.getProcessNo()) {
                            Config.NodeInfo.SERVICE_CODE = response.getServiceCode();
                        }
                    }
                } catch (Throwable t) {
                    log.error("Heartbeat Failed.", t);
                    ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).reportError(t);
                }
            }
        }
    }

    @Override
    public int priority() {
        return 2;
    }
}
