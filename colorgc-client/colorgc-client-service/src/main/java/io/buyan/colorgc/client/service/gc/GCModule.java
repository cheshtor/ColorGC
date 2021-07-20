package io.buyan.colorgc.client.service.gc;

import com.sun.management.GarbageCollectionNotificationInfo;
import io.buyan.colorgc.client.service.Module;
import io.buyan.colorgc.client.service.ModuleManager;
import io.buyan.colorgc.client.service.remote.GRPCChannelListener;
import io.buyan.colorgc.client.service.remote.GRPCChannelModule;
import io.buyan.colorgc.client.service.remote.GRPCChannelStatus;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GCModule implements Module, GRPCChannelListener, Runnable {

    private LinkedBlockingQueue<GarbageCollectionNotificationInfo> cache;

    private volatile ScheduledFuture<?> dataSendFuture;

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

    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public void statusChanged(GRPCChannelStatus status) {

    }

    @Override
    public void run() {

    }
}
