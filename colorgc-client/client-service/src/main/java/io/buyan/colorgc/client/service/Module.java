package io.buyan.colorgc.client.service;

/**
 * 服务模块
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public interface Module {

    void prepare() throws Throwable;

    void boot() throws Throwable;

    void onComplete() throws Throwable;

    void shutdown() throws Throwable;

    int priority();

}
