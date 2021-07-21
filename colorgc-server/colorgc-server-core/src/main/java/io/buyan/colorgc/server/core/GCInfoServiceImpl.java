package io.buyan.colorgc.server.core;

import io.buyan.colorgc.protocol.common.Empty;
import io.buyan.colorgc.protocol.gc.GCInfoServiceGrpc;
import io.buyan.colorgc.protocol.gc.GCNotification;
import io.buyan.colorgc.protocol.gc.GCRecords;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/21
 */
@Slf4j
public class GCInfoServiceImpl extends GCInfoServiceGrpc.GCInfoServiceImplBase {

    @Override
    public void report(GCRecords request, StreamObserver<Empty> responseObserver) {
        List<GCNotification> notificationList = request.getRecordsList();
        log.info("收到 GC 消息 {} 条", notificationList.size());
        notificationList.forEach(System.out::println);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
