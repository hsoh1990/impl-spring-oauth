package com.wellstone.implspringoauth.common;

import com.wellstone.implspringoauth.common.ResponseData;
import com.wellstone.implspringoauth.common.ResponseDataType;
import com.wellstone.implspringoauth.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exceptionHandle(Exception e) {
        log.error("OAuth Server Global Error=");
        log.error(Utils.getAsString(e));

        ResponseData responseData = ResponseData.builder()
                .message(e.getMessage())
                .type(ResponseDataType.FAILED_CODE)
                .build();

        return new ResponseEntity<>(responseData, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
