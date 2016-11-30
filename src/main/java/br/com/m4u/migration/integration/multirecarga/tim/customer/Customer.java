package br.com.m4u.migration.integration.multirecarga.tim.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandro on 04/11/16.
 */
public class Customer {

    @JsonProperty("msisdn")
    private String msisdn;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("cpf")
    private String documentNumber;

    public Customer() {
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
