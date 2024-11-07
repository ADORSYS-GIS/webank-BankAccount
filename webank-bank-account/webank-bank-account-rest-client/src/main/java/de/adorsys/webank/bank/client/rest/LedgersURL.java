package de.adorsys.webank.bank.client.rest;

public interface LedgersURL {
    // Define the URL using Spring's property placeholders with a fallback value
    String LEDGERS_URL = "${ledgers.url:http://localhost:8088}";
}
