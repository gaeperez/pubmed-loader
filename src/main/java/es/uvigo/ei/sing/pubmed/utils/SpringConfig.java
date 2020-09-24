package es.uvigo.ei.sing.pubmed.utils;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import org.apache.commons.text.similarity.CosineDistance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class SpringConfig {

    @Bean
    public NormalizedLevenshtein normalizedLevenshtein() {
        // Calculate the distance between the texts. 0 is equal
        return new NormalizedLevenshtein();
    }

    @Bean
    public CosineDistance cosineDistance() {
        // Calculate the distance between the texts. 0 is equal
        return new CosineDistance();
    }

    @Bean
    public ThreadPoolTaskExecutor pubmedExecutor() {
        // Limit the Executor to 3 threads (API limitations)
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(3);
        executor.setAllowCoreThreadTimeOut(true);
        executor.setThreadNamePrefix("PubMed_Worker");
        executor.initialize();

        return executor;
    }
}
