package io.buyan.colorgc.server.service.gc;

import io.buyan.colorgc.protocol.common.Empty;
import io.buyan.colorgc.protocol.gc.GCBatch;
import io.buyan.colorgc.protocol.gc.GCReportServiceGrpc;
import io.buyan.colorgc.server.service.ServerModule;
import io.grpc.stub.StreamObserver;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/22
 */
public class GCModule extends GCReportServiceGrpc.GCReportServiceImplBase implements ServerModule {

    @Override
    public void prepare() throws Throwable {

    }

    @Override
    public void boot() throws Throwable {

    }

    @Override
    public void onComplete() throws Throwable {

    }

    @Override
    public void shutdown() throws Throwable {

    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void report(GCBatch request, StreamObserver<Empty> responseObserver) {
        super.report(request, responseObserver);
    }
}
