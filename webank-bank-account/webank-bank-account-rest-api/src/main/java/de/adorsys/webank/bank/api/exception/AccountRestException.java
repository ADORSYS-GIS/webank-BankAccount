package de.adorsys.webank.bank.api.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountRestException extends RuntimeException {
    private final AccountErrorCode errorCode;
    private final String devMsg;
}
