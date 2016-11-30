package br.com.m4u.migration.integration.multirecarga.tim.refill.reload;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by sandro on 24/11/16.
 */
public class ChangeRefillReloadRequest {

    @JsonProperty("value")
    private Integer value;

    @JsonProperty("minimum_balance")
    private Integer minimumBalance;

    @JsonProperty("times")
    private Integer times;

    public ChangeRefillReloadRequest() {
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
}
