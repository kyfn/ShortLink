package org.lbc.shortlink.project.common.convention.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lbc.shortlink.project.common.convention.errorcode.BaseErrorCode;
import org.lbc.shortlink.project.common.convention.exception.AbstractException;
import org.lbc.shortlink.project.common.convention.exception.ServiceException;
import org.lbc.shortlink.project.common.convention.result.Result;
import org.lbc.shortlink.project.common.convention.result.Results;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常处理器
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：12306）获取项目资料
 */
@Component
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截请求参数格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> reqJsonExceptionHandler(HttpServletRequest request, HttpMessageNotReadableException ex) {
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), ex.toString());
        return Results.failure(BaseErrorCode.CLIENT_PARAM_FORMAT_ERROR.code(), BaseErrorCode.CLIENT_PARAM_FORMAT_ERROR.message());
    }

    /**
     * 拦截请求方式错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> reqMethodExceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), ex.toString());
        return Results.failure(BaseErrorCode.CLIENT_REQUEST_METHOD_ERROR.code(), BaseErrorCode.CLIENT_REQUEST_METHOD_ERROR.message());
    }
    /**
     * 拦截参数验证异常
     */
    @SneakyThrows
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result<Void> validExceptionHandler(HttpServletRequest request, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                .map(FieldError::getDefaultMessage)
                .orElse(StrUtil.EMPTY);
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
        return Results.failure(BaseErrorCode.CLIENT_PARAM_ERROR.code(), exceptionStr);
    }


    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result<Void> abstractException(HttpServletRequest request, AbstractException ex) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString(), ex.getCause());
            return Results.failure(ex);
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return Results.failure(ex);
    }

    /**
     * 拦截未捕获异常
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<Void> defaultErrorHandler(HttpServletRequest request, Throwable throwable) {
        log.error("[{}] {} ", request.getMethod(), getUrl(request), throwable);
        String message = throwable.getCause() != null ? throwable.getCause().getMessage() : throwable.getMessage();
        return Results.failure(new ServiceException(message));
    }

    private String getUrl(HttpServletRequest request) {
        if (StrUtil.isEmpty(request.getQueryString())) {
            return request.getRequestURL().toString();
        }
        return request.getRequestURL().toString() + "?" + request.getQueryString();
    }
}
