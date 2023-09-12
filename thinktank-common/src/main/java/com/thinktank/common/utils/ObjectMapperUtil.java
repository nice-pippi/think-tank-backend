package com.thinktank.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thinktank.common.exception.ThinkTankException;
import org.apache.commons.lang3.StringUtils;


/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 13:54
 * @Description: ObjectMapper工具类
 * @Version: 1.0
 */
public class ObjectMapperUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将任意对象转化为JSON
     *
     * @param object 任意对象
     * @return
     */
    public static String toJSON(Object object) {
        try {
            if (object == null) {
                throw new ThinkTankException("传递的参数object为null,请认真检查");
            }
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ThinkTankException("传递的对象不支持json转化/检查是否有get/set方法");
        }
    }


    /**
     * 将任意的JSON串转化为对象  传递什么类型转化什么对象
     *
     * @param json   JSON字符串
     * @param target 类
     * @param <T>    泛型
     * @return
     */
    public static <T> T toObject(String json, Class<T> target) {
        if (StringUtils.isEmpty(json) || target == null) {
            throw new ThinkTankException("传递的字符串或类不能为null");
        }
        try {
            return OBJECT_MAPPER.readValue(json, target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ThinkTankException("json转化异常");
        }
    }
}
