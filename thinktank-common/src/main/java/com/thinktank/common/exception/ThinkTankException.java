package com.thinktank.common.exception;

/**
 * @Author: 弘
 * @CreateTime: 2023年09⽉08⽇ 14:08
 * @Description: 自定义异常
 * @Version: 1.0
 */
public class ThinkTankException extends RuntimeException {
    public ThinkTankException(String message) {
        super(message);
    }
}
