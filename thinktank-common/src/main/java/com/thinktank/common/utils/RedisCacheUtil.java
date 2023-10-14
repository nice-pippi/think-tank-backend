package com.thinktank.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉14⽇ 12:17
 * @Description: redis缓存工具类
 * @Version: 1.0
 */
public class RedisCacheUtil {
    public static <T> T getObjectByTypeReference(Object object, TypeReference<T> typeRef) {
        String json = object.toString();
        if ("null".equals(json)) {
            return null;
        }
        return ObjectMapperUtil.toObjectByTypeReference(json, typeRef);
    }

    public static <T> T getObject(Object object, Class<T> target) {
        String json = object.toString();
        if ("null".equals(json)) {
            return null;
        }
        return ObjectMapperUtil.toObject(json, target);
    }
}
