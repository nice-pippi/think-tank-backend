package com.thinktank.common.exception;

import com.thinktank.common.utils.R;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: 弘
 * @CreateTime: 2023年08⽉15⽇ 14:01
 * @Description: 全局异常捕获
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

    @ResponseBody
    @ExceptionHandler(ThinkTankException.class)
    public R<String> thinkTankException(ThinkTankException e) {
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 存储错误信息
        BindingResult bindingResult = e.getBindingResult();

        // 1获取参数异常
        List<String> list = bindingResult.getFieldErrors().stream().map(item ->
                item.getDefaultMessage()
        ).collect(Collectors.toList());


        log.error("【系统异常】{}", e.getMessage(), list);
        return R.error(list.toString());
    }

}
