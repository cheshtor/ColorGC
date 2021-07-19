package io.buyan.colorgc.client.service.conf;

import io.buyan.colorgc.common.utils.ConfigLoader;

import java.io.InputStream;

/**
 * {Description}
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
