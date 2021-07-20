package io.buyan.colorgc.server.core;

import io.buyan.colorgc.protocol.HeartbeatRequest;
import io.buyan.colorgc.protocol.HeartbeatServiceGrpc;
import io.buyan.colorgc.protocol.common.Empty;
import io.grpc.stub.StreamObserver;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
public class HeartbeatServiceImpl extends HeartbeatServiceGrpc.HeartbeatServiceImplBase {

    @Override
    public void heartbeat(HeartbeatRequest request, StreamObserver<Empty> responseObserver) {
        System.out.println(request.getServiceName());
        System.out.println(request.getIp());
        System.out.println(request.getProcessNo());
        System.out.println("-------------------------------------");
    }
}
