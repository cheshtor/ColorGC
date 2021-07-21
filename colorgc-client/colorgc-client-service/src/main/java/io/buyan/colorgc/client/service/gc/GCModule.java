package io.buyan.colorgc.client.service.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import io.buyan.colorgc.client.service.Module;
import io.buyan.colorgc.client.service.ModuleManager;
import io.buyan.colorgc.client.service.remote.GRPCChannelListener;
import io.buyan.colorgc.client.service.remote.GRPCChannelModule;
import io.buyan.colorgc.client.service.remote.GRPCChannelStatus;
import io.buyan.colorgc.common.utils.JsonUtils;
import io.buyan.colorgc.protocol.gc.GCInfoServiceGrpc;
import io.buyan.colorgc.protocol.gc.GCNotification;
import io.buyan.colorgc.protocol.gc.GCRecords;
import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * GC 信息采集模块
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/21
 */
@Slf4j
public class GCModule implements Module, GRPCChannelListener, Runnable {

    /**
     * GC 信息缓存
     */
    private LinkedBlockingQueue<GarbageCollectionNotificationInfo> cache;

    /**
     * 发送 GC 信息定时任务
     */
    private volatile ScheduledFuture<?> dataSendFuture;

    /**
     * 网络连接状态
     */
    private volatile GRPCChannelStatus channelStatus = GRPCChannelStatus.DISCONNECTED;

    /**
     * GC 信息发送请求
     */
    private volatile GCInfoServiceGrpc.GCInfoServiceBlockingStub stub;

    @Override
    public void prepare() throws Throwable {
        cache = new LinkedBlockingQueue<>(1000);
        ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).addChannelListener(this);

        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    offer(info);
                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

    private void offer(GarbageCollectionNotificationInfo info) {
        if (!cache.offer(info)) {
            cache.poll();
            cache.offer(info);
        }
    }

    @Override
    public void boot() throws Throwable {
        dataSendFuture = Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this, 0, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {
        dataSendFuture.cancel(true);
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public void statusChanged(GRPCChannelStatus status) {
        if (GRPCChannelStatus.CONNECTED.equals(status)) {
            Channel channel = ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).getChannel();
            stub = GCInfoServiceGrpc.newBlockingStub(channel);
        } else {
            stub = null;
        }
        this.channelStatus = status;
    }

    @Override
    public void run() {
        if (GRPCChannelStatus.CONNECTED.equals(channelStatus)) {
            if (null != stub) {
                try {
                    List<GarbageCollectionNotificationInfo> buffer = new LinkedList<>();
                    cache.drainTo(buffer);
                    if (!buffer.isEmpty()) {
                        List<GCNotification> list = buffer.stream()
                                .map(info -> GCNotification.newBuilder()
                                        .setGcName(info.getGcName())
                                        .setGcCause(info.getGcCause())
                                        .setGcAction(info.getGcAction())
                                        .setGcInfo(JsonUtils.obj2String(info))
                                        .build())
                                .collect(Collectors.toList());
                        GCRecords records = GCRecords.newBuilder().addAllRecords(list).build();
                        stub.report(records);
                    }
                } catch (Throwable t) {
                    log.error("Send GCInfo Failed.", t);
                    ModuleManager.INSTANCE.findModule(GRPCChannelModule.class).reportError(t);
                }
            }
        }
    }
}
