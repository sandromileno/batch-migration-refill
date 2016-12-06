package br.com.m4u.migration.reload.util;

import br.com.m4u.migration.reload.enums.ChannelEnum;
import br.com.m4u.migration.reload.model.RefillReload;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * Created by sandro on 07/11/16.
 */
@Component
public class RefillReloadFieldSetMapper implements FieldSetMapper<RefillReload> {
    @Override
    public RefillReload mapFieldSet(FieldSet fieldSet) throws BindException {
        RefillReload refillReload = new RefillReload();
        refillReload.setMsisdn(fieldSet.readString("msisdn"));
        refillReload.setAmount(fieldSet.readInt("amount")*100);
        refillReload.setChannel(ChannelEnum.getChannel(fieldSet.readString("channel")));
        refillReload.setTimes(fieldSet.readInt("times"));
        refillReload.setMinimumBalance(fieldSet.readInt("minimumBalance")*100);
        refillReload.setDependent(fieldSet.readString("dependent"));
        return refillReload;
    }
}
