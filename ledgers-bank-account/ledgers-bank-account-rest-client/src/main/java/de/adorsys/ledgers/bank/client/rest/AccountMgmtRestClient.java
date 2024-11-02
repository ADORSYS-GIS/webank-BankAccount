package de.adorsys.ledgers.bank.client.rest;
import  de.adorsys.ledgers.bank.api.resource.AccountMgmResourceAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ledgersAccountMgmtRestClient", url = AccountMgmResourceAPI.BASE_PATH, path = LedgersURL.LEDGERS_URL)
public interface AccountMgmtRestClient extends AccountMgmResourceAPI {}

