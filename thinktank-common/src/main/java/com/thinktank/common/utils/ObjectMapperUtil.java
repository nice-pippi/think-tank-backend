package com.thinktank.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thinktank.common.exception.ThinkTankException;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;


/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 13:54
 * @Description: ObjectMapper工具类
 * @Version: 1.0
 */
public class ObjectMapperUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // 禁用将日期写入JSON时的时间戳格式
            .registerModule(new JavaTimeModule()) // 注册JavaTimeModule模块，以便支持Java 8中的日期和时间类型（如LocalDateTime）
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); // 设置日期格式为"yyyy-MM-dd HH:mm:ss"，用于序列化和反序列化日期字段

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
     * @param target 要转换的类
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
            throw new ThinkTankException("json转化异常：" + e.getMessage());
        }
    }

    /**
     * 将任意的JSON串转化为对象  传递什么目标类型转化什么对象
     *
     * @param json       JSON字符串
     * @param targetType 目标类型
     * @param <T>        泛型
     * @return
     */
    public static <T> T toObjectByTypeReference(String json, TypeReference<T> targetType) {
        if (StringUtils.isEmpty(json) || targetType == null) {
            throw new IllegalArgumentException("传递的字符串或目标类型不能为空");
        }
        try {
            return OBJECT_MAPPER.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            throw new ThinkTankException("JSON转换异常: " + e.getMessage());
        }
    }
}
