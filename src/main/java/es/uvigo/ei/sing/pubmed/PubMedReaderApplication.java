package es.uvigo.ei.sing.pubmed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class PubMedReaderApplication {
    public static void main(String[] args) {
        SpringApplication.run(PubMedReaderApplication.class, args);
    }
}
