/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.sense.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.ckcclc.springboot.sense.controller")
public class SenseExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(SenseExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest servletRequest){
        ErrorCode errorCode = null;
        if (e instanceof ServiceException) {
            errorCode = ((ServiceException) e).getErrorCode();
        } else {
            errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        }

        Response response = new Response(errorCode);
        response.setMsg(e.getMessage());
        response.setRequestUrl(servletRequest.getRequestURI());
        logger.debug("[Response] return:{} for request uri:{} from ip:{}",
                response, servletRequest.getRequestURI(), servletRequest.getRemoteHost());
        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {

        Response response = new Response(ErrorCode.UNKNOWN);
        response.setMsg(ex.getMessage());
        logger.debug("[Response] return:{} for request description:{}",
                response, request.getDescription(true));
        return super.handleExceptionInternal(ex, ex.getMessage(), headers, status, request);
    }
}