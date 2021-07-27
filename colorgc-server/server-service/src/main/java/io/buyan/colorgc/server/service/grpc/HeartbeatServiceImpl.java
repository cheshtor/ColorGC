package io.buyan.colorgc.server.service.grpc;

import io.buyan.colorgc.protocol.heartbeat.HeartbeatRequest;
import io.buyan.colorgc.protocol.heartbeat.HeartbeatResponse;
import io.buyan.colorgc.protocol.heartbeat.HeartbeatServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * 心跳服务
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/23
 */
public class HeartbeatServiceImpl extends HeartbeatServiceGrpc.HeartbeatServiceImplBase {

    @Override
    public void heartbeat(HeartbeatRequest request, StreamObserver<HeartbeatResponse> responseObserver) {
        super.heartbeat(request, responseObserver);
    }
}
