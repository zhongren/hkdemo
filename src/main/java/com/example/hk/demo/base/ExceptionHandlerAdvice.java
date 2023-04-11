package com.example.hk.demo.base;


import com.example.hk.demo.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleArgumentNotValidException(MethodArgumentNotValidException exp) {
        log.error(exp.getMessage());
        FieldError error = exp.getBindingResult().getFieldErrors().get(0);
        ResultDto result = new ResultDto(
                ResultDto.FAIL, error.getDefaultMessage(), error.getField());
        return new ResponseEntity<>(JsonUtil.objectToJson(result), HttpStatus.OK);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleBusinessException(BaseException exp) {
        log.error(exp.getMessage());
        ResultDto result = new ResultDto(exp.getCode(), exp.getMessage());
        return new ResponseEntity<>(JsonUtil.objectToJson(result), HttpStatus.OK);
    }

//    @ExceptionHandler(AuthException.class)
//    public ResponseEntity<String> handleAuthInvalidException(AuthException exp) {
//        log.error(exp.getMessage());
//        ResultDto result = new ResultDto(exp.getCode(), exp.getMessage());
//        return new ResponseEntity<>(JsonUtil.objectToJson(result), HttpStatus.OK);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException exp) {
        log.error(exp.getMessage());
        ResultDto result = new ResultDto(ResultDto.FAIL, exp.getMessage());
        return new ResponseEntity<>(JsonUtil.objectToJson(result), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
