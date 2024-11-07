/*
 * Copyright (c) 2018-2024 adorsys GmbH and Co. KG
 * All rights are reserved.
 */

package de.adorsys.webank.bank.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;


@Slf4j
class HttpStatusResolverTest {
    @Test
    void testAccountStatusResolverCoverage() {
        List<AccountErrorCode> codes = Arrays.asList(AccountErrorCode.values());
        codes.forEach(c -> {
            boolean isNull = AccountHttpStatusResolver.resolveHttpStatusByCode(c) == null;
            if (isNull) {
                log.error("{} is missing in Resolver", c.name());
            }
            assertFalse(isNull);
        });
    }

}
