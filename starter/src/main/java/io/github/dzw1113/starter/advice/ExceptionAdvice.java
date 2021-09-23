package io.github.dzw1113.starter.advice;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.dzw1113.common.model.HttpResult;

/**
 * @description:
 * @author: dzw
 * @date: 2021/09/16 11:19
 **/

@ControllerAdvice
public class ExceptionAdvice extends SimpleMappingExceptionResolver {
   
    String errorCode = "E500";
    
    @ExceptionHandler(Exception.class)
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView model = new ModelAndView();
        if (isAjAX(request)) {
            // 返回客户端信息
            try {
                // 如果已经输出到页面了，不再输出
                if (!response.isCommitted()) {
                    response.reset();
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    // 设置返回编码信息
                    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                    PrintWriter out = response.getWriter();
                    out.print(new ObjectMapper().writeValueAsString(HttpResult.error(errorCode, getExceptionErrmsg(ex))));
                    response.setStatus(200);
                }
            } catch (Exception e) {
                return model;
            }
        }
        return model;
    }
    
    
    /**
     * 获取错误信息
     *
     * @param ex
     * @return
     */
    private String getExceptionErrmsg(Exception ex) {
        String errMsg = "未知错误";
        if (ex instanceof NullPointerException) {
            errMsg = "Null异常错误信息";
        } else {
            errMsg = ex.getMessage();
        }
        return errMsg;
    }
    
    private static boolean isAjAX(HttpServletRequest request) {
        String requestHeadType = request.getHeader("X-Requested-With");
        return Objects.equals(requestHeadType, "XMLHttpRequest");
    }
}