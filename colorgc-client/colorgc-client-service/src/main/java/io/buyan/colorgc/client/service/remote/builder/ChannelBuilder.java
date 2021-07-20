package io.buyan.colorgc.client.service.remote.builder;

import io.grpc.ManagedChannelBuilder;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public interface ChannelBuilder<B extends ManagedChannelBuilder> {

    B build(B managedChannelBuilder) throws Exception;

}
