package io.buyan.colorgc.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;

/**
 * JSON 工具
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/21
 */
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 全部字段序列化
        //对象的所有字段全部列入
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS)
                //creator支持
                .registerModule(new ParameterNamesModule())
                //optional支持
                .registerModule(new Jdk8Module());
        SimpleModule simpleModule = new SimpleModule();
        //定制 Long->String
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        OBJECT_MAPPER.registerModule(simpleModule);
    }

    /**
     * 返回工具的objectMapper，便于业务临时自定义工具行为
     *
     * @return
     */
    public static ObjectMapper getUtilObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json mapping [obj2String] error", e);
            return null;
        }
    }

    /**
     * 对象转有格式的json
     *
     * @param obj 实例
     * @return 格式化的json
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json mapping [obj2StringPretty] error", e);
            return null;
        }
    }

    /**
     * 字符串转对象
     *
     * @param str   json
     * @param clazz 目标类型
     * @return 目标类型实例
     */
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : OBJECT_MAPPER.readValue(str, clazz);
        } catch (Exception e) {
            log.error("json mapping [string2Obj] error", e);
            return null;
        }
    }

    /**
     * 字段符转List之类的集合
     *
     * @param str           json
     * @param typeReference 集合类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(str) || typeReference == null) {
            return null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class) ? str : OBJECT_MAPPER.readValue(str, typeReference));
        } catch (Exception e) {
            log.error("json mapping [string2Obj] error", e);
            return null;
        }
    }

    /**
     * json转集合对象
     *
     * @param str             json
     * @param collectionClass 集合类型
     * @param elementClasses  集合元素类型
     * @return 序列化对象
     */
    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (Exception e) {
            log.error("json mapping [string2Obj] error", e);
            return null;
        }
    }

    /**
     * json -> map
     *
     * @param json json
     * @return map
     * @throws IOException
     */
    public static HashMap<String, String> json2Map(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<HashMap<String, String>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("json mapping [json2Map] error", e);
            return null;
        }
    }

    /**
     * 返回JsonParser api ，用于手动解析json-str
     */
    public static JsonParser str2JsonParser(String str) throws IOException {
        return OBJECT_MAPPER.getFactory().createParser(str);
    }

    /**
     * json -> collection
     *
     * @param str            json
     * @param collectionType 集合类型
     * @param valueType      值类型
     * @param <T>            值类型
     * @return 序列化结果
     * @throws IOException 异常
     */
    public static <T> Object json2Collection(String str, Class<? extends Collection> collectionType, Class<T> valueType) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(collectionType, valueType);
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            log.error("json mapping [json2Collection] error", e);
            return null;
        }
    }

}
