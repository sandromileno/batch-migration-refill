package br.com.m4u.migration.reload.processor;

import br.com.m4u.migration.integration.multirecarga.tim.customer.CustomerService;
import br.com.m4u.migration.integration.multirecarga.tim.customer.FindCustomerResponse;
import br.com.m4u.migration.integration.multirecarga.tim.customer.FindDependentResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.ChangeRefillResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.CreateRefillReloadResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.FindRefillReloadsResponse;
import br.com.m4u.migration.integration.multirecarga.tim.refill.reload.RefillReloadService;
import br.com.m4u.migration.reload.builder.RefillReloadBuilder;
import br.com.m4u.migration.reload.enums.MigrationStatusEnum;
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
        if (customerResponse.wasSuccessful() && refillReload.getMsisdn().equals(refillReload.getDependent())) {
            log.info("Verificando se cliente {} possui recarga refil", refillReload.getMsisdn());
            FindRefillReloadsResponse findRefillReloadsResponse = refillService.findRefillReload(customerResponse.getCustomer().getUuid(), refillReload.getChannel());
            if (findRefillReloadsResponse.hasRefillReload(refillReload)) {
                log.info("Cliente {} possui recarga refil", refillReload.getMsisdn());
                if (findRefillReloadsResponse.wasChanged(refillReload)) {
                    log.info("Cliente {} possui recarga refil para ser alterada", refillReload.getMsisdn());
                    ChangeRefillResponse changeRefillResponse = refillService.changeRefillReload(RefillReloadBuilder.buildChangeRefillReloadRequest(refillReload), refillReload.getChannel(), findRefillReloadsResponse.getRefillReloadsResponse(refillReload).getUuid());
                    if (changeRefillResponse.wasSuccessful()) {
                        refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.MIGRATED.getStatus());
                        refillReloadResponseProcessor = convertToResponseProcessor(changeRefillResponse, refillReloadResponseProcessor);
                        log.info("Recarga refill {} do cliente {} alterada com sucesso", changeRefillResponse.getUuid(), changeRefillResponse.getRecipient());
                    } else {
                        refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("Falha ao alterar recarga refil do dependente %1s", refillReload.getDependent()));
                        refillReloadResponseProcessor.setRecipient(refillReload.getDependent());
                        log.error("Falha ao alterar recarga refil do cliente %1s", refillReload.getMsisdn());
                    }
                } else {
                    refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("Cliente %1s ja possui recarga refill com as mesmas configuracoes", refillReload.getMsisdn()));
                    refillReloadResponseProcessor.setRecipient(refillReload.getDependent());
                }
            } else {
                log.info("Iniciando cadastro do cliente {} no recarga refill", refillReload.getMsisdn());
                CreateRefillReloadResponse createRefillReloadResponse = refillService.createRefillReload(RefillReloadBuilder.build(refillReload, customerResponse.getCustomer().getDocumentNumber(), customerResponse.getCustomer().getUuid(), refillReload.getMsisdn()), refillReload.getChannel(), customerResponse.getCustomer().getUuid());
                if (createRefillReloadResponse.wasSuccessful()) {
                    log.info("Cliente {} : valor {} : canal {} : saldo {} : recebedor {} : vezes: {} - cadastrado com sucesso", createRefillReloadResponse.getCustomerId(),
                            createRefillReloadResponse.getValue(),
                            createRefillReloadResponse.getApplication(),
                            createRefillReloadResponse.getMinimumBalance(),
                            createRefillReloadResponse.getRecipient(),
                            createRefillReloadResponse.getTimes());
                    refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.MIGRATED.getStatus());
                    refillReloadResponseProcessor = convertToResponseProcessor(createRefillReloadResponse, refillReloadResponseProcessor);

                } else {
                    refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("Falha ao cadastrar refil para o cliente %1s", refillReload.getMsisdn()));
                    refillReloadResponseProcessor = convertToResponseProcessor(refillReload, refillReloadResponseProcessor);
                    log.error("Problemas no cadastro refil do cliente {}", refillReload.getMsisdn());
                }
            }
        } else if (customerResponse.wasSuccessful() && !refillReload.getMsisdn().equals(refillReload.getDependent())) {
            log.info("Verificando se dependente {} e do cliente {}", refillReload.getDependent(), refillReload.getMsisdn());
            FindDependentResponse findDependentResponse = customerService.findDependente(refillReload.getMsisdn(), refillReload.getDependent(), refillReload.getChannel());
            if (findDependentResponse.wasSuccessful()) {
                FindRefillReloadsResponse findRefillReloadsResponse = refillService.findRefillReload(customerResponse.getCustomer().getUuid(), refillReload.getChannel());
                if (findRefillReloadsResponse.dependentHasRefillReload(refillReload.getDependent())) {
                    if (findRefillReloadsResponse.dependentWasChanged(refillReload)) {
                        ChangeRefillResponse changeRefillDependentResponse = refillService.changeRefillReload(RefillReloadBuilder.buildChangeRefillReloadRequest(refillReload), refillReload.getChannel(), findRefillReloadsResponse.getRefillReloadsResponse(refillReload.getDependent()).getUuid());
                        if (changeRefillDependentResponse.wasSuccessful()) {
                            refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.MIGRATED.getStatus());
                            refillReloadResponseProcessor = convertToResponseProcessor(changeRefillDependentResponse, refillReloadResponseProcessor);
                            log.info("Recarga refill {} do dependente {} alterada com sucesso", changeRefillDependentResponse.getUuid(), changeRefillDependentResponse.getRecipient());
                        } else {
                            refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus());
                            log.info("Falha ao alterar recarga refil do dependente", refillReload.getDependent());
                        }
                    } else {
                        refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("Dependente %1s ja possui refil com mesmas configuracoes", refillReload.getDependent()));
                        refillReloadResponseProcessor.setRecipient(refillReload.getDependent());
                    }
                } else {
                    log.info("Iniciando cadastro de recarga refil para o dependete {}", refillReload.getDependent());
                    CreateRefillReloadResponse createRefillReloadResponse = refillService.createRefillReload(RefillReloadBuilder.build(refillReload, customerResponse.getCustomer().getDocumentNumber(), customerResponse.getCustomer().getUuid(), refillReload.getDependent()), refillReload.getChannel(), customerResponse.getCustomer().getUuid());
                    if (createRefillReloadResponse.wasSuccessful()) {
                        log.info("Cliente {} : valor {} : canal {} : saldo {} : recebedor {} : vezes: {} - cadastrado com sucesso", createRefillReloadResponse.getCustomerId(),
                                createRefillReloadResponse.getValue(),
                                createRefillReloadResponse.getApplication(),
                                createRefillReloadResponse.getMinimumBalance(),
                                createRefillReloadResponse.getTimes());
                        refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.MIGRATED.getStatus());
                        refillReloadResponseProcessor = convertToResponseProcessor(createRefillReloadResponse, refillReloadResponseProcessor);

                    } else {
                        refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus());
                        log.error("Problemas no cadastro do dependente {}", refillReload.getMsisdn());
                    }
                }
            } else {
                log.error("Msisdn {} nao e dependente do cliente {}", refillReload.getDependent(), refillReload.getMsisdn());
                refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("msisdn %1s nao e dependente do cliente %2s", refillReload.getDependent(), refillReload.getMsisdn()));
                refillReloadResponseProcessor = convertToResponseProcessor(refillReload, refillReloadResponseProcessor);
            }

        } else {
            refillReloadResponseProcessor = new RefillReloadResponseProcessor(MigrationStatusEnum.NOT_MIGRATED.getStatus(), String.format("Cliente %1s nao cadastrado", refillReload.getDependent()));
            refillReloadResponseProcessor = convertToResponseProcessor(refillReload, refillReloadResponseProcessor);
            log.info("Cliente {} nao esta cadastrado", refillReload.getMsisdn());

        }
        return refillReloadResponseProcessor;
    }

    private RefillReloadResponseProcessor convertToResponseProcessor(RefillReload refillReload, RefillReloadResponseProcessor refillReloadResponseProcessor) {
        refillReloadResponseProcessor.setTimes(refillReload.getTimes());
        refillReloadResponseProcessor.setAmount(refillReload.getAmount());
        refillReloadResponseProcessor.setRecipient(refillReload.getDependent());
        refillReloadResponseProcessor.setMinimumBalance(refillReload.getMinimumBalance());
        refillReloadResponseProcessor.setDocumentNumber(refillReload.getDocumentNumber());
        return refillReloadResponseProcessor;
    }

    private RefillReloadResponseProcessor convertToResponseProcessor(ChangeRefillResponse response, RefillReloadResponseProcessor refillReloadResponseProcessor) {
        refillReloadResponseProcessor.setTimes(response.getTimes());
        refillReloadResponseProcessor.setAmount(response.getValue());
        refillReloadResponseProcessor.setRecipient(response.getRecipient());
        refillReloadResponseProcessor.setMinimumBalance(response.getMinimumBalance());
        refillReloadResponseProcessor.setDocumentNumber(response.getDocumentNumber());
        return refillReloadResponseProcessor;
    }

    private RefillReloadResponseProcessor convertToResponseProcessor(CreateRefillReloadResponse response, RefillReloadResponseProcessor refillReloadResponseProcessor) {
        refillReloadResponseProcessor.setDocumentNumber(response.getDocumentNumber());
        refillReloadResponseProcessor.setAmount(response.getValue());
        refillReloadResponseProcessor.setTimes(response.getTimes());
        refillReloadResponseProcessor.setMinimumBalance(response.getMinimumBalance());
        refillReloadResponseProcessor.setRecipient(response.getRecipient());
        return refillReloadResponseProcessor;
    }
}