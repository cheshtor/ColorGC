package io.buyan.colorgc.common.utils;

/**
 * 字符串工具类
 *
 * @author Pengyu Gan
 * CreateDate 2021/7/19
 */
public class StringUtils {

    public static boolean isEmpty(CharSequence charSequence) {
        return null == charSequence || charSequence.length() == 0;
    }

}
