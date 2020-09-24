package es.uvigo.ei.sing.pubmed.controllers;

import es.uvigo.ei.sing.pubmed.core.PubmedService;
import es.uvigo.ei.sing.pubmed.entities.DocumentEntity;
import es.uvigo.ei.sing.pubmed.entities.QueryEntity;
import es.uvigo.ei.sing.pubmed.services.QueryService;
import es.uvigo.ei.sing.pubmed.utils.AppConstants;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Controller
public class AppController {

    private final QueryService queryService;
    private final PubmedService pubmedService;

    @Autowired
    public AppController(QueryService queryService, PubmedService pubmedService) {
        this.queryService = queryService;
        this.pubmedService = pubmedService;
    }

    @Synchronized
    public int run(QueryEntity queryEntity) {
        int timeToWait = AppConstants.APP_SECONDS_SUCCESS;

        // Get query properties
        String value = queryEntity.getValue();
        String type = String.valueOf(queryEntity.getType());
        String sortMode = String.valueOf(queryEntity.getSortMode());
        int retStart;
        int retMax = queryEntity.getRetMax();
        int limitPages = queryEntity.getLimitPages();
        int currentPage = queryEntity.getCurrentPage();

        Set<String> ids;
        boolean lastInsert;
        do {
            // Get the index to start downloading the IDs
            retStart = queryEntity.getRetStart();

            // Get the public document IDs from the query
            ids = pubmedService.retrieveIDs(value, type, sortMode, retStart, retMax);

            Set<DocumentEntity> documentEntities = new HashSet<>();
            if (!ids.isEmpty()) {
                // Retrieve the sublist of IDs from the public databases
                documentEntities = new HashSet<>(pubmedService.getFilesFromIDs(ids));

                if (type.equalsIgnoreCase(AppConstants.MODE_RET_IDS))
                    // If the query type is IDS, all documents are retrieved at once (do the loop only one time)
                    currentPage = limitPages;
                else
                    // Keep looking for new IDs in the following pages, may be new articles
                    // or articles that are not linked to this query
                    currentPage++;
            } else
                // There is no more IDs available in PubMed for the current query (last page reached)
                currentPage = limitPages;

            // Check if it is the last insert
            lastInsert = currentPage == limitPages;

            // Add the sublist of retrieved documents to the query and save them. Update query information.
            queryEntity = queryService.save(queryEntity, documentEntities, currentPage, lastInsert);
        } while (currentPage < limitPages);

        return timeToWait;
    }
}
