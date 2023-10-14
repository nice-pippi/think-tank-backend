package com.thinktank.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @Author: 弘
 * @CreateTime: 2023年10⽉14⽇ 12:17
 * @Description: redis缓存工具类
 * @Version: 1.0
 */
public class RedisCacheUtil {
    public static <T> T getObjectByTypeReference(String result, TypeReference<T> typeRef) {
        if ("null".equals(result)) {
            return null;
        }
        return ObjectMapperUtil.toObjectByTypeReference(result, typeRef);
    }

    public static <T> T getObject(String result, Class<T> target) {
        if ("null".equals(result)) {
            return null;
        }
        return ObjectMapperUtil.toObject(result, target);
    }
}
