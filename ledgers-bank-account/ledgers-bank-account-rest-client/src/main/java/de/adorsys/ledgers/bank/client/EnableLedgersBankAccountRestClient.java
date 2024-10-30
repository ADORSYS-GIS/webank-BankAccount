package de.adorsys.ledgers.bank.client;

import org.springframework.context.annotation.*;

import java.lang.annotation.*;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = {java.lang.annotation.ElementType.TYPE})
@Documented
@Import({
        LedgersBankAccountRestClientConfiguration.class,
})
public @interface EnableLedgersBankAccountRestClient {
}
