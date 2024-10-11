package org.example.client.impl;

import org.example.client.MailServiceClient;
import org.example.dto.request.MailServiceRequestDto;
import org.example.model.AppUser;
import org.example.util.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MailServiceClientImpl implements MailServiceClient {

    private final String mailServiceAddress;

    private final String mailServiceActivationEndpoint;

    private final RestTemplate restTemplate;

    private final CryptoTool cryptoTool;

    public MailServiceClientImpl(
            @Value("${service.mail.url}")
            String mailServiceAddress,
            @Value("${service.mail.activation-endpoint}")
            String mailServiceActivationEndpoint,
            RestTemplate restTemplate,
            CryptoTool cryptoTool
    ) {
        this.mailServiceActivationEndpoint = mailServiceActivationEndpoint;
        this.mailServiceAddress = mailServiceAddress;
        this.restTemplate = restTemplate;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String sendMailActivationRequest(AppUser appUser, String email) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MailServiceRequestDto> request = new HttpEntity<>(
                new MailServiceRequestDto(cryptoTool.hashOf(appUser.getId()), email),
                headers);

        return restTemplate.postForObject(
                mailServiceAddress + mailServiceActivationEndpoint,
                request,
                String.class);
    }
}
