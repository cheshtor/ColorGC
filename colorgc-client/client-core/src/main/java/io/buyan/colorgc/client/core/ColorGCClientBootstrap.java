package io.buyan.colorgc.client.core;

import io.buyan.colorgc.client.service.ModuleManager;
import io.buyan.colorgc.client.service.conf.ConfigInitializer;

import java.util.concurrent.CountDownLatch;

/**
 * 客户端启动类
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class ColorGCClientBootstrap {

    public static void main(String[] args) throws Exception {
        ConfigInitializer.init();
        ModuleManager.INSTANCE.load();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

}
