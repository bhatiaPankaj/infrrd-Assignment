package com.fileoperation.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fileoperation.base.common.ReturnType;

@RestControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler{

	 @ExceptionHandler(Exception.class)
	  public final ResponseEntity<ReturnType> handleAllExceptions(Exception ex) {
	   ReturnType returnType = new ReturnType();
	   returnType.setMessage(ex.getMessage());
	    return new ResponseEntity<ReturnType>(returnType, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
}
