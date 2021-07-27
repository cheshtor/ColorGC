package io.buyan.colorgc.client.service.conf;

import io.buyan.colorgc.common.utils.ConfigLoader;

import java.io.InputStream;

/**
 * 配置信息初始化工具
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class ConfigInitializer {

    public static void init() {
        InputStream inputStream = ConfigInitializer.class.getClassLoader().getResourceAsStream("colorgc.properties");
        ConfigLoader.load(inputStream, Config.class);
    }

}
