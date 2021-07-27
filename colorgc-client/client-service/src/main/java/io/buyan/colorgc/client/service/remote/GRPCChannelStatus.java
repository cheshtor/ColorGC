package io.buyan.colorgc.client.service.remote;

/**
 * GRPC 连接状态枚举
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public enum GRPCChannelStatus {

    /**
     * 已连接
     */
    CONNECTED,

    /**
     * 连接断开
     */
    DISCONNECTED;

}
