package io.buyan.colorgc.server.core;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.concurrent.CountDownLatch;

/**
 * 服务端启动类
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
public class ColorGCServerBootstrap {

    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8899)
                .addService(new HeartbeatServiceImpl())
                .build()
                .start();
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

}
