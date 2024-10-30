package de.adorsys.ledgers.bank.client.rest;
import de.adorsys.ledgers.bank.api.resource.AccountRestAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(path=AccountRestAPI.BASE_PATH)
public interface AccountRestClient extends AccountRestAPI {}