package com.thinktank.common.exception;

import com.thinktank.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 14:01
 * @Description: 全局异常处理
 * @Version: 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R<String> exception(Exception e) {
        log.error("系统异常：{}", e.getMessage());
        return R.error("系统出现异常了...");
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public R<String> runtimeException(RuntimeException e) {
        log.error("系统运行时出现了异常：{}", e.getMessage());
        return R.error("系统运行时出现了异常...");
    }
}
