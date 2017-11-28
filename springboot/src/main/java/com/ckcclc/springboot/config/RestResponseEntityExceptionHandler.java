/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-9
 */

package com.ckcclc.springboot.config;

import com.ckcclc.springboot.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice(annotations=RestController.class)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> getSQLError(Exception exception, WebRequest request, HttpServletRequest servletRequest){
        super.handleException(exception, request);
        System.out.println("invoke");
        System.out.println(servletRequest.getRequestURI());
        return new ResponseEntity<>("Rest Controller Advice Example", HttpStatus.ACCEPTED);
    }
}