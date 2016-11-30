package br.com.m4u.migration.integration.multirecarga.tim.refill.reload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by sandro on 22/11/16.
 */
@Service
public class RefillReloadService {

    @Autowired
    private RestTemplate restClient;

    @Autowired
    private Environment env;

    public CreateRefillReloadResponse createRefillReload(CreateRefillReloadRequest request, String channel, String customerId) {
        ResponseEntity<CreateRefillReloadResponse> responseEntity = null;
        CreateRefillReloadResponse response = null;
        try {
            HttpEntity<CreateRefillReloadRequest> createRefillRequest = createRequest(request, channel);
            responseEntity = restClient.exchange(env.getProperty("tim.endpoint.create.refill.reaload"), HttpMethod.POST, createRefillRequest, CreateRefillReloadResponse.class);
            response = responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            response = new CreateRefillReloadResponse(ex.getStatusCode(), ex.getMessage());
        } catch (Exception e) {
            response = new CreateRefillReloadResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return response;
    }

    public FindRefillReloadsResponse findRefillReload(String customerId, String channel) {
        ResponseEntity<RefillReloadsResponse[]> responseEntity = null;
        FindRefillReloadsResponse response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Client-Realm-Id", "tim_recarga");
            headers.add("Application-Id", channel);
            headers.add("Service-Provider-Id", "tim");
            HttpEntity entity = new HttpEntity(headers);
            responseEntity = restClient.exchange(env.getProperty("tim.endpoint.fronted.find.refill.reload"), HttpMethod.GET, entity, RefillReloadsResponse[].class, customerId);
            response = new FindRefillReloadsResponse();
            response.setRefills(new ArrayList<RefillReloadsResponse>(Arrays.asList(responseEntity.getBody())));
        } catch (HttpClientErrorException ex) {
            response = new FindRefillReloadsResponse(ex.getStatusCode(), ex.getMessage());
        } catch (Exception e) {
            response = new FindRefillReloadsResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return response;
    }

    public ChangeRefillResponse changeRefillReload(ChangeRefillReloadRequest request, String channel, String refillId) {
        ChangeRefillResponse response = null;
        try {
            ResponseEntity<ChangeRefillResponse> responseEntity = restClient.exchange(env.getProperty("tim.endpoint.fronted.change.refill.reload"), HttpMethod.PUT, changeRequest(request, channel), ChangeRefillResponse.class, refillId);
            response = responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            response = new ChangeRefillResponse(ex.getStatusCode(), ex.getMessage());
        } catch (Exception e) {
            response = new ChangeRefillResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return response;
    }

    private HttpEntity<CreateRefillReloadRequest> createRequest(CreateRefillReloadRequest request, String channel) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Client-Realm-Id", "tim_recarga");
        headers.add("Application-Id", channel);
        headers.add("Service-Provider-Id", "tim");
        HttpEntity<CreateRefillReloadRequest> requestEntity = new HttpEntity<CreateRefillReloadRequest>(request, headers);
        return requestEntity;
    }

    private HttpEntity<ChangeRefillReloadRequest> changeRequest(ChangeRefillReloadRequest request, String channel) {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Client-Realm-Id", "tim_recarga");
        headers.add("Application-Id", channel);
        headers.add("Service-Provider-Id", "tim");
        HttpEntity<ChangeRefillReloadRequest> requestEntity = new HttpEntity<ChangeRefillReloadRequest>(request, headers);
        return requestEntity;
    }
}