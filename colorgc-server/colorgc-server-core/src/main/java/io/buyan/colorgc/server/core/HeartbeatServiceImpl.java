package io.buyan.colorgc.server.core;

import io.buyan.colorgc.protocol.HeartbeatRequest;
import io.buyan.colorgc.protocol.HeartbeatServiceGrpc;
import io.buyan.colorgc.protocol.common.Empty;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
@Slf4j
public class HeartbeatServiceImpl extends HeartbeatServiceGrpc.HeartbeatServiceImplBase {

    @Override
    public void heartbeat(HeartbeatRequest request, StreamObserver<Empty> responseObserver) {
        log.info("ServiceName: {}", request.getServiceName());
        log.info("IP Address: {}", request.getIp());
        log.info("ProcessNo: {}", request.getProcessNo());
        log.info("==========================================");
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
