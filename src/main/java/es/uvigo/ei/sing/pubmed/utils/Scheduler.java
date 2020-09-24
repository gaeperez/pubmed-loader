package es.uvigo.ei.sing.pubmed.utils;

import es.uvigo.ei.sing.pubmed.controllers.AppController;
import es.uvigo.ei.sing.pubmed.entities.QueryEntity;
import es.uvigo.ei.sing.pubmed.services.QueryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@Log4j2
@Configuration
public class Scheduler {

    private final AppController appController;
    private final QueryService queryService;

    @Autowired
    public Scheduler(AppController appController, QueryService queryService) {
        this.appController = appController;
        this.queryService = queryService;
    }

    // Wait 10 minutes to start again when all the queries finish
    @Scheduled(fixedDelay = 600000)
    public void runController() {
        // Retrieved all non suspended queries from database
        Iterable<QueryEntity> queryEntities = queryService.findByIsSuspendedFalse();
        // Iterate the queries
        for (QueryEntity queryEntity : queryEntities) {
            try {
                appController.run(queryEntity);
            } catch (Exception e) {
                e.printStackTrace();
                // Log the possible unknown exceptions
                log.error(AppConstants.STR_ERROR_RETRIEVAL, queryEntity.getValue(), e.getMessage());
            } finally {
                try {
                    // Waiting among queries
                    log.info(AppConstants.STR_INFO_WAIT, AppConstants.APP_SECONDS_SUCCESS);
                    TimeUnit.SECONDS.sleep(AppConstants.APP_SECONDS_SUCCESS);
                } catch (InterruptedException e) {
                    log.error(AppConstants.STR_ERROR_SLEEP);
                }
            }
        }
    }
}
