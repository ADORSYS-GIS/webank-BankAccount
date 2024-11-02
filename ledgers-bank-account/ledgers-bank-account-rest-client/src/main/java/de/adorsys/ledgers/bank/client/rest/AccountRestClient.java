package de.adorsys.ledgers.bank.client.rest;
import de.adorsys.ledgers.bank.api.resource.AccountRestAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "ledgersAccountRestClient", url = AccountRestAPI.BASE_PATH, path = LedgersURL.LEDGERS_URL)
public interface AccountRestClient extends AccountRestAPI {}