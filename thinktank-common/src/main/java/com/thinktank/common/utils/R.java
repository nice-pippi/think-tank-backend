package com.thinktank.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 14:04
 * @Description: 统一返回结果类型
 * @Version: 1.0
 */
@Data
public class R<T> implements Serializable {
    private Integer status; //状态：200为成功

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.status = 200;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.status = 500;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
