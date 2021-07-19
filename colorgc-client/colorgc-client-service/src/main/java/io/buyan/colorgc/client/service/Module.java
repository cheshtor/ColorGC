package io.buyan.colorgc.client.service;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public interface Module {

    void prepare() throws Throwable;

    void boot() throws Throwable;

    void onComplete() throws Throwable;

    void shutdown() throws Throwable;

}
