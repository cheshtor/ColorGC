package io.buyan.colorgc.client.service.channel.builder;

import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class StandardChannelBuilder implements ChannelBuilder {

    private final static int MAX_INBOUND_MESSAGE_SIZE = 1024 * 1024 * 50;

    @Override
    public ManagedChannelBuilder build(ManagedChannelBuilder managedChannelBuilder) throws Exception {
        return managedChannelBuilder.nameResolverFactory(new DnsNameResolverProvider())
                .maxInboundMessageSize(MAX_INBOUND_MESSAGE_SIZE)
                .usePlaintext();
    }
}
