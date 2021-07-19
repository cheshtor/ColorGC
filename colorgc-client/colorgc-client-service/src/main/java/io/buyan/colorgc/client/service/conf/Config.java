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

    /**
     * 服务端地址列表，以逗号分隔
     */
    public static String SERVER_LIST;

    /**
     * 尝试与服务端建立连接次数
     */
    public static Integer RECONNECT_COUNT;

}
