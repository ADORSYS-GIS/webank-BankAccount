/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.rest.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import de.adorsys.webank.bank.api.exception.AccountRestException;
import de.adorsys.webank.bank.api.exception.AccountHttpStatusResolver;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class ExceptionAdvisor {
    private static final String MESSAGE = "message";
    private static final String DEV_MESSAGE = "devMessage";
    private static final String CODE = "code";
    private static final String ERROR_CODE = "errorCode";
    private static final String DATE_TIME = "dateTime";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> globalExceptionHandler(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, String> body = getHandlerContent(status, null, null, "Something went wrong during execution of your request.");
        log.error("INTERNAL SERVER ERROR", ex);
        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler(AccountRestException.class)
    public ResponseEntity<Map<String,String>> handleDepositModuleException(AccountRestException ex) {
        HttpStatus status = AccountHttpStatusResolver.resolveHttpStatusByCode(ex.getErrorCode());
        Map<String, String> body = getHandlerContent(status, ex.getErrorCode().name(), null, ex.getDevMsg());
        log.error(ex.getDevMsg());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String,String>> handleFeignException(FeignException ex) {
        HttpStatus status = HttpStatus.CONFLICT;
        Map<String, String> body = getHandlerContent(status, "Internal Rest Exception!", null, "Something went wrong during server internal interaction. \nPlease consult your bank for details.");
        log.error(ex.contentUTF8());
        return new ResponseEntity<>(body, status);
    }


    //TODO Consider a separate Class for this with a builder?
    private Map<String, String> getHandlerContent(HttpStatus status, String errorCode, String message, String devMessage) {
        Map<String, String> error = new HashMap<>();
        error.put(CODE, String.valueOf(status.value()));
        Optional.ofNullable(errorCode).ifPresent(e -> error.put(ERROR_CODE, e));
        error.put(MESSAGE, message);
        error.put(DEV_MESSAGE, devMessage);
        error.put(DATE_TIME, LocalDateTime.now().toString());
        return error;
    }
}