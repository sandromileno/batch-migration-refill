package br.com.m4u.migration.reload.post.processor;

/**
 * Created by sandro on 22/11/16.
 */
public class RefillReloadResponseProcessor {

    private String status;
    private String recipient;
    private Integer amount;
    private Integer minimumBalance;
    private Integer times;
    private String documentNumber;
    private String responseBody;

    public RefillReloadResponseProcessor() {
    }

    public RefillReloadResponseProcessor(String status, String responseBody) {
        this.status = status;
        this.responseBody = responseBody;
    }

    public RefillReloadResponseProcessor(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
