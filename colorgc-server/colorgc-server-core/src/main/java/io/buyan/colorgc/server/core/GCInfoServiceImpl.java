package io.buyan.colorgc.server.core;

import io.buyan.colorgc.protocol.common.Empty;
import io.buyan.colorgc.protocol.gc.GCBatch;
import io.buyan.colorgc.protocol.gc.GCDetails;
import io.buyan.colorgc.protocol.gc.GCReportServiceGrpc;
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
public class GCInfoServiceImpl extends GCReportServiceGrpc.GCReportServiceImplBase {

    @Override
    public void report(GCBatch request, StreamObserver<Empty> responseObserver) {
        List<GCDetails> notificationList = request.getRowsList();
        log.info("收到 GC 消息 {} 条", notificationList.size());
        notificationList.forEach(System.out::println);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
