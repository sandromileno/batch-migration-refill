package br.com.m4u.migration.integration.multirecarga.tim.refill.reload;

import br.com.m4u.migration.reload.model.RefillReload;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Created by sandro on 24/11/16.
 */
public class FindRefillReloadsResponse {

    private HttpStatus statusCode;

    private String message;

    private List<RefillReloadsResponse> refills;

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

    public boolean hasRefillReload() {
        return (refills != null && !refills.isEmpty());
    }

    public boolean wasChanged(RefillReload refillReload) {
        for (RefillReloadsResponse refill : refills) {

        }
        return false;
    }
}
