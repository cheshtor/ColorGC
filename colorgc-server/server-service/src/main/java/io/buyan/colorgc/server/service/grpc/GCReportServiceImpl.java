package io.buyan.colorgc.server.service.grpc;

import io.buyan.colorgc.protocol.common.Empty;
import io.buyan.colorgc.protocol.gc.GCBatch;
import io.buyan.colorgc.protocol.gc.GCReportServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * GC 上报服务
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/23
 */
public class GCReportServiceImpl extends GCReportServiceGrpc.GCReportServiceImplBase {

    @Override
    public void report(GCBatch request, StreamObserver<Empty> responseObserver) {
        super.report(request, responseObserver);
    }
}
