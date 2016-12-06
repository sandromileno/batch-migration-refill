package br.com.m4u.migration.integration.multirecarga.tim.refill.reload;

import br.com.m4u.migration.reload.model.RefillReload;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandro on 24/11/16.
 */
public class FindRefillReloadsResponse {

    private HttpStatus statusCode;

    private String message;

    private List<RefillReloadsResponse> refills = new ArrayList<RefillReloadsResponse>();

    public FindRefillReloadsResponse() {
    }

   public FindRefillReloadsResponse(HttpStatus statusCode, String message) {
       this.statusCode = statusCode;
       this.message = message;
   }

    public List<RefillReloadsResponse> getRefills() {
        return refills;
    }

    public void setRefills(List<RefillReloadsResponse> refills) {
        this.refills = refills;
    }

    public boolean hasRefillReload(RefillReload refillReload) {
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(refillReload.getMsisdn())) {
                return true;
            }
        }
        return false;
    }

    public boolean dependentHasRefillReload(String msisdn) {
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(msisdn)) {
                return true;
            }
        }
        return false;
    }

    public boolean wasChanged(RefillReload refillReload) {
        RefillReloadsResponse refillFound = null;
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(refillReload.getMsisdn())) {
                refillFound = refill;
                break;
            }
        }

        if (refillFound.getMinimumBalance().equals(refillReload.getMinimumBalance()) &&
                refillFound.getTimes().equals(refillReload.getTimes()) && refillFound.getValue().equals(refillReload.getAmount())){
            return false;
        } else {
            return true;
        }
    }

    public boolean dependentWasChanged(RefillReload refillReload) {
        RefillReloadsResponse refillFound = null;
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(refillReload.getDependent())) {
                refillFound = refill;
                break;
            }
        }

        if (refillFound.getMinimumBalance().equals(refillReload.getMinimumBalance()) &&
                refillFound.getTimes().equals(refillReload.getTimes()) && refillFound.getValue().equals(refillReload.getAmount())){
            return false;
        } else {
            return true;
        }
    }

    public RefillReloadsResponse getRefillReloadsResponse(RefillReload refillReload) {
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(refillReload.getMsisdn())) {
                return refill;
            }
        }
        return null;
    }

    public RefillReloadsResponse getRefillReloadsResponse(String msisdn) {
        for (RefillReloadsResponse refill : refills) {
            if (refill.getRecipient().equals(msisdn)) {
                return refill;
            }
        }
        return null;
    }

}
