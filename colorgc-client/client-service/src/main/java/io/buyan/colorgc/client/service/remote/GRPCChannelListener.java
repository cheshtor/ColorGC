package io.buyan.colorgc.client.service.remote;

/**
 * GRPC 连接状态监听器
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public interface GRPCChannelListener {

    /**
     * GRPC 连接状态发生变化事件触发
     * @param status GRPC 连接状态
     */
    void statusChanged(GRPCChannelStatus status);

}
