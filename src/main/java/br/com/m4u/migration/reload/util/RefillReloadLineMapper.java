package br.com.m4u.migration.reload.util;

import br.com.m4u.migration.reload.model.RefillReload;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;

/**
 * Created by sandro on 07/11/16.
 */
public class RefillReloadLineMapper extends DefaultLineMapper<RefillReload> {

    @Override
    public RefillReload mapLine(String line, int lineNumber) throws Exception {
        return super.mapLine(line, lineNumber);
    }

    @Override
    public void setFieldSetMapper(FieldSetMapper<RefillReload> fieldSetMapper) {
        super.setFieldSetMapper(fieldSetMapper);
    }
}
