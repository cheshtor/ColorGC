package io.buyan.colorgc.client.service.conf;

import lombok.Data;

/**
 * ColorGC 配置
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
@Data
public class Config {

    public static class NodeInfo {
        public static String SERVICE_CODE = null;
    }

    /**
     * 服务端地址列表，以逗号分隔
     */
    public static String SERVER_LIST;

    /**
     * 尝试与服务端建立连接次数
     */
    public static Integer RECONNECT_COUNT;

    /**
     * 心跳周期
     */
    public static Long HEARTBEAT_PERIOD;

    /**
     * 当前节点的服务名称
     */
    public static String SERVICE_NAME;

    /**
     * GC 信息上周频率
     */
    public static Long GC_REPORT_PERIOD;

}
