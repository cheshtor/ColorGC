package io.buyan.colorgc.common.utils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * {Description}
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class ConfigLoader {

    public static <T> void load(InputStream propertiesStream, Class<T> configClass) {
        try {
            Properties properties = new Properties();
            properties.load(propertiesStream);
            T config = configClass.newInstance();
            if (properties.isEmpty()) {
                return;
            }
            Field[] configFields = configClass.getDeclaredFields();
            for (Field configField : configFields) {
                String fieldName = configField.getName();
                Object fieldValue = properties.getProperty(fieldName.toLowerCase());
                if (null != fieldValue) {
                    configField.setAccessible(true);
                    Class<?> fieldType = configField.getType();
                    if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                        fieldValue = Long.valueOf(fieldValue.toString());
                    }
                    if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                        fieldValue = Integer.valueOf(fieldValue.toString());
                    }
                    if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                        fieldValue = Short.valueOf(fieldValue.toString());
                    }
                    if (fieldType.equals(Byte.class) || fieldType.equals(byte.class)) {
                        fieldValue = Byte.valueOf(fieldValue.toString());
                    }
                    if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                        fieldValue = Boolean.valueOf(fieldValue.toString());
                    }
                    configField.set(config, fieldValue);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Config properties file parse failed.", e);
        }
    }

}
