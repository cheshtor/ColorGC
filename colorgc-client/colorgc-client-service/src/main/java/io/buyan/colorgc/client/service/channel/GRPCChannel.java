package io.buyan.colorgc.client.service.channel;

import io.buyan.colorgc.client.service.channel.builder.StandardChannelBuilder;
import io.grpc.Channel;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NettyChannelBuilder;

/**
 * 对原生 ManagedChannel 的封装
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class GRPCChannel {

    private final ManagedChannel channel;

    public GRPCChannel(String host, int port) throws Exception {
        ManagedChannelBuilder channelBuilder = NettyChannelBuilder.forAddress(host, port);
        channelBuilder = new StandardChannelBuilder().build(channelBuilder);
        this.channel = channelBuilder.build();
    }

    public Channel getChannel() {
        return channel;
    }

    public boolean isTerminated() {
        return channel.isTerminated();
    }

    public void shutdownNow() {
        channel.shutdownNow();
    }

    public boolean isShutdown() {
        return channel.isShutdown();
    }

    public boolean isConnected() {
        return isConnected(false);
    }

    public boolean isConnected(boolean requestConnection) {
        return channel.getState(requestConnection) == ConnectivityState.READY;
    }
}
