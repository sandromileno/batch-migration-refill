package br.com.m4u.migration.integration.multirecarga.tim.refill.reload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandro on 24/11/16.
 */
public class RefillReloadsResponse {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("recipient")
    private String recipient;

    @JsonProperty("value")
    private Integer value;

    @JsonProperty("minimum_balance")
    private Integer minimumBalance;

    @JsonProperty("times")
    private Integer times;

    @JsonProperty("client_realm")
    private String clientRealm;

    @JsonProperty("document_number")
    private String documentNumber;

    @JsonProperty("application")
    private String application;

    public RefillReloadsResponse() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Integer minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public String getClientRealm() {
        return clientRealm;
    }

    public void setClientRealm(String clientRealm) {
        this.clientRealm = clientRealm;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
