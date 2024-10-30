package de.adorsys.ledgers.bank.client.rest;
import  de.adorsys.ledgers.bank.api.resource.AccountMgmResourceAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(path = AccountMgmResourceAPI.BASE_PATH)
    public interface AccountMgmtRestClient extends AccountMgmResourceAPI {}

