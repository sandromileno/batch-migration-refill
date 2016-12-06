package br.com.m4u.migration.config;

import br.com.m4u.migration.reload.model.RefillReload;
import br.com.m4u.migration.reload.post.processor.RefillReloadResponseProcessor;
import br.com.m4u.migration.reload.processor.RefillReloadItemProcessor;
import br.com.m4u.migration.reload.util.RefillReloadFieldSetMapper;
import br.com.m4u.migration.reload.util.RefillReloadLineMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sandro on 03/11/16.
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${file_to_import}")
    private String inputFile;

    @Value("${result_file}")
    private String outputFile;

    @Bean
    public ItemReader<RefillReload> reader() {
        FlatFileItemReader<RefillReload> reader = new FlatFileItemReader<RefillReload>();
        reader.setResource(new FileSystemResource(inputFile));
        reader.setLineMapper(new RefillReloadLineMapper() {{
            setFieldSetMapper(new RefillReloadFieldSetMapper());
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "msisdn", "dependent", "amount", "channel", "minimumBalance", "times" });
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<RefillReload, RefillReloadResponseProcessor> processor() {
        return new RefillReloadItemProcessor();
    }

    @Bean
    public ItemWriter<RefillReloadResponseProcessor> writer() {
        FlatFileItemWriter<RefillReloadResponseProcessor> writer = new FlatFileItemWriter<RefillReloadResponseProcessor>();
        writer.setResource(new FileSystemResource(outputFile));
        DelimitedLineAggregator<RefillReloadResponseProcessor> delLineAgg = new DelimitedLineAggregator<RefillReloadResponseProcessor>();
        delLineAgg.setDelimiter(",");
        BeanWrapperFieldExtractor<RefillReloadResponseProcessor> fieldExtractor = new BeanWrapperFieldExtractor<RefillReloadResponseProcessor>();
        fieldExtractor.setNames(new String[] {"status", "responseBody", "recipient", "amount", "minimumBalance", "times", "documentNumber"});
        delLineAgg.setFieldExtractor(fieldExtractor);
        writer.setLineAggregator(delLineAgg);
        return writer;
    }


    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<RefillReload, RefillReloadResponseProcessor> chunk(1)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .throttleLimit(10)
                .build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(20);
        taskExecutor.setCorePoolSize(12);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }
}