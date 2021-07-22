package io.buyan.colorgc.server.service;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/20
 */
public interface ServerModule {

    void prepare() throws Throwable;

    void boot() throws Throwable;

    void onComplete() throws Throwable;

    void shutdown() throws Throwable;

    int priority();

}
