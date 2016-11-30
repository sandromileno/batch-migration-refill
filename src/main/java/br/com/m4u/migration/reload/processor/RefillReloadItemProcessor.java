package br.com.m4u.migration.reload.processor;

import br.com.m4u.migration.integration.multirecarga.tim.customer.CustomerService;
import br.com.m4u.migration.integration.multirecarga.tim.customer.FindCustomerResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.CreateRefillReloadResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.FindRefillReloadsResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.RefillReloadService;
import br.com.m4u.migration.reload.builder.RefillReloadBuilder;
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

    @Autowired
    private RefillReloadService refillService;

    @Override
    public RefillReloadResponseProcessor process(RefillReload refillReload) throws Exception {

        log.info("Processando cliente {} com valor {} no canal {}", refillReload.getMsisdn(), refillReload.getAmount(), refillReload.getChannel());
        log.info("Iniciando busca do cliente {} no multirecarga", refillReload.getMsisdn());
        FindCustomerResponse customerResponse = customerService.findCustomer(refillReload.getMsisdn());
        log.info("Resposta da busca do cliente {} no multirecarga - encontrado {}", refillReload.getMsisdn(), customerResponse.wasSuccessful());
        RefillReloadResponseProcessor refillReloadResponseProcessor = null;
        if (customerResponse.wasSuccessful()) {
            FindRefillReloadsResponse findRefillReloadsResponse = refillService.findRefillReload(customerResponse.getCustomer().getUuid(), refillReload.getChannel());
            log.info("Verificando se cliente {} possui recarga refil", refillReload.getMsisdn());
            if (findRefillReloadsResponse.hasRefillReload()) {

            } else {
                log.info("Iniciando cadastro do cliente {} no wilykat", refillReload.getMsisdn());
                CreateRefillReloadResponse createRefillReloadResponse = refillService.createRefillReload(RefillReloadBuilder.build(refillReload, customerResponse.getCustomer().getDocumentNumber(), customerResponse.getCustomer().getUuid()), refillReload.getChannel(), customerResponse.getCustomer().getUuid());

                if (createRefillReloadResponse.wasSuccessful()) {
                    log.info("Cliente {} com valor {} no canal {} cadastrado com sucesso", refillReload.getMsisdn(), refillReload.getAmount(), refillReload.getChannel());
                }
            }
        }
        return refillReloadResponseProcessor;
    }
}