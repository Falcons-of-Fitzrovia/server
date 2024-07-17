package com.fof.server.exception;

import com.fof.server.model.normal.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ResponseDTO> handleCustomErrorException(CustomErrorException ex) {

        ResponseDTO errorResponse = new ResponseDTO();
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(String.valueOf(HttpStatus.NOT_FOUND.value()));

        

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }

}