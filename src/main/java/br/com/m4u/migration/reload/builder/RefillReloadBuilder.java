package br.com.m4u.migration.reload.builder;

import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.ChangeRefillReloadRequest;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.CreateRefillReloadRequest;
import br.com.m4u.migration.reload.model.RefillReload;

public class RefillReloadBuilder {
    public static CreateRefillReloadRequest build(RefillReload refillReload, String documentNumber, String customerId, String msisdn) {
        CreateRefillReloadRequest request = new CreateRefillReloadRequest();
        request.setRecipient(msisdn);
        request.setMinimumBalance(refillReload.getMinimumBalance());
        request.setTimes(refillReload.getTimes());
        request.setDocumentNumber(documentNumber);
        request.setValue(refillReload.getAmount());
        request.setCustomerId(customerId);
        return request;
    }

    public static ChangeRefillReloadRequest buildChangeRefillReloadRequest(RefillReload refillReload) {
        ChangeRefillReloadRequest request = new ChangeRefillReloadRequest();
        request.setValue(refillReload.getAmount());
        request.setTimes(refillReload.getTimes());
        request.setMinimumBalance(refillReload.getMinimumBalance());
        return request;
    }
}