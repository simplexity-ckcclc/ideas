/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config;

import com.ckcclc.springboot.exception.BusinessException;
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

//@ControllerAdvice(annotations=RestController.class)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> getError(Exception exception, WebRequest request, HttpServletRequest servletRequest){
        logger.warn("[Exception Advice] caught in {}", servletRequest.getRequestURI(), exception);
        if (exception instanceof BusinessException) {
            return new ResponseEntity<>("Business Exception: " + ((BusinessException) exception).getErrorCode().name(), HttpStatus.OK);
        }
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info("request description:{}", request.getDescription(true), ex);
        return super.handleExceptionInternal(ex, ex.getMessage(), headers, status, request);
    }

}