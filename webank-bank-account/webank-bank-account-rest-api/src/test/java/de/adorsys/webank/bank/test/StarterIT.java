package de.adorsys.webank.bank.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SampelApplication.class)
public class StarterIT {

    @Test
    void shall_boot_without_error(){
        assertTrue(true);
    }
}
