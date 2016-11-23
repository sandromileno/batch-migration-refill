package br.com.m4u.migration.reload.model;

/**
 * Created by sandro on 01/11/16.
 */
public class RefillReload extends Reload {

    private Integer minimumBalance;
    private Integer times;
    private String documentNumber;

    public  RefillReload() {
    }

    public RefillReload(Integer valor, String channel, String msisdn, Integer minimumBalance, Integer times, String documentNumber) {
        super(valor, channel, msisdn);
        this.minimumBalance = minimumBalance;
        this.times = times;
        this.documentNumber = documentNumber;
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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
}
