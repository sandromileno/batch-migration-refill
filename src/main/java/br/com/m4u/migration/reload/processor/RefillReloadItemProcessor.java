package br.com.m4u.migration.reload.processor;

import br.com.m4u.migration.integration.multirecarga.tim.customer.CustomerService;
import br.com.m4u.migration.integration.multirecarga.tim.customer.FindCustomerResponse;
import br.com.m4u.migration.reload.model.RefillReload;
import br.com.m4u.migration.reload.post.processor.RefillReloadResponseProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by sandro on 03/11/16.
 */
public class RefillReloadItemProcessor implements ItemProcessor<RefillReload, RefillReloadResponseProcessor> {

    private static final Logger log = LoggerFactory.getLogger(RefillReloadItemProcessor.class);

    @Autowired
    private CustomerService customerService;

    @Override
    public RefillReloadResponseProcessor process(RefillReload refillReload) throws Exception {

        log.info("Processando cliente {} com valor {} no canal {}", refillReload.getMsisdn(), refillReload.getAmount(), refillReload.getChannel());
        log.info("Iniciando busca do cliente {} no multirecarga", refillReload.getMsisdn());
        FindCustomerResponse customerResponse = customerService.findCustomer(refillReload.getMsisdn());
        log.info("Resposta da busca do cliente {} no multirecarga - encontrado {}", refillReload.getMsisdn(), customerResponse.wasSuccessful());
        RefillReloadResponseProcessor refillReloadResponseProcessor = null;
        return refillReloadResponseProcessor;
    }
}