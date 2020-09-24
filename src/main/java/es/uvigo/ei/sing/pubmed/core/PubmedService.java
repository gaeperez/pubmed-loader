package es.uvigo.ei.sing.pubmed.core;

import es.uvigo.ei.sing.pubmed.core.eutils.EntrezSearch;
import es.uvigo.ei.sing.pubmed.entities.*;
import es.uvigo.ei.sing.pubmed.services.DocumentService;
import es.uvigo.ei.sing.pubmed.services.QueryService;
import es.uvigo.ei.sing.pubmed.utils.AppConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log4j2
public class PubmedService {

    private final ThreadPoolTaskExecutor pubmedExecutor;
    private final ApplicationContext applicationContext;
    private final DocumentService documentService;
    private final QueryService queryService;

    @Autowired
    public PubmedService(ThreadPoolTaskExecutor pubmedExecutor, ApplicationContext applicationContext,
                         DocumentService documentService, QueryService queryService) {
        this.pubmedExecutor = pubmedExecutor;
        this.applicationContext = applicationContext;
        this.documentService = documentService;
        this.queryService = queryService;
    }

    public Set<String> retrieveIDs(String query, String type, String sortMode, int retStart, int retMax) {
        Set<String> ids = new HashSet<>();

        try {
            if (type.equalsIgnoreCase(AppConstants.MODE_RET_QUERY))
                ids = getIDsFromQuery(query, sortMode, retStart, retMax);
            else if (type.equalsIgnoreCase(AppConstants.MODE_RET_IDS))
                ids = Stream.of(query.split(",")).collect(Collectors.toSet());
            else
                log.warn(AppConstants.STR_WARN_MODE, query, type);
        } catch (IOException e) {
            log.warn(AppConstants.STR_WARN_NO_IDS, query);
        } catch (Exception e) {
            log.error(AppConstants.STR_ERROR_RETRIEVAL, query, e);
        }

        return ids;
    }

    private Set<String> getIDsFromQuery(String query, String sortMode, int retStart, int retMax) throws IOException {
        // Search e-utils engine
        EntrezSearch search = new EntrezSearch();

        // Set parameters
        search.setDatabase(AppConstants.PUBMED_DATABASE);
        search.setTerm(query);
        // Number of retrievals
        search.setMaxRetrieval(retMax);
        // Init index
        search.setRetStart(retStart);
        search.setSort(sortMode);
        search.doQuery();

        // Retrieve IDs from query
        return Stream.of(search.getIds().split(",")).collect(Collectors.toSet());
    }

    public List<DocumentEntity> getFilesFromIDs(Collection<String> ids) {
        List<DocumentEntity> listOfDocuments = new ArrayList<>();

        try {
            // List of futures
            List<Future<DocumentEntity>> futures = new ArrayList<>();

            // Synchronized variables to the threads (avoid uniqueness restrictions in the DB)
            Map<String, DocumentEntity> mapDocumentEntities = Collections.synchronizedMap(new HashMap());
            Map<String, AuthorEntity> mapAuthorEntities = Collections.synchronizedMap(new HashMap());
            Map<String, AffiliationEntity> mapAffiliationEntities = Collections.synchronizedMap(new HashMap());
            Map<String, FundingEntity> mapFundingEntities = Collections.synchronizedMap(new HashMap());
            Map<String, PublicationTypeEntity> mapPublicationTypeEntities = Collections.synchronizedMap(new HashMap());
            Map<String, SubstanceEntity> mapSubstanceEntities = Collections.synchronizedMap(new HashMap());
            Map<String, MeshTermEntity> mapMeshTermEntities = Collections.synchronizedMap(new HashMap());
            Map<String, KeywordEntity> mapKeywordEntities = Collections.synchronizedMap(new HashMap());
            Map<String, ReferenceEntity> mapReferenceEntities = Collections.synchronizedMap(new HashMap());
            Map<String, JournalEntity> mapJournalEntities = Collections.synchronizedMap(new HashMap());

            // Add and execute Callable tasks
            for (String id : ids) {
                // Check if document already exists in the database
                Optional<DocumentEntity> possibleDocument = documentService.findByExternalId(id);

                if (!possibleDocument.isPresent()) {
                    PubmedWorker pubmedWorker = applicationContext.getBean(PubmedWorker.class);
                    pubmedWorker.setSavedID(id);
                    pubmedWorker.setMapDocumentEntities(mapDocumentEntities);
                    pubmedWorker.setMapAuthorEntities(mapAuthorEntities);
                    pubmedWorker.setMapAffiliationEntities(mapAffiliationEntities);
                    pubmedWorker.setMapFundingEntities(mapFundingEntities);
                    pubmedWorker.setMapPublicationTypeEntities(mapPublicationTypeEntities);
                    pubmedWorker.setMapSubstanceEntities(mapSubstanceEntities);
                    pubmedWorker.setMapMeshTermEntities(mapMeshTermEntities);
                    pubmedWorker.setMapKeywordEntities(mapKeywordEntities);
                    pubmedWorker.setMapReferenceEntities(mapReferenceEntities);
                    pubmedWorker.setMapJournalEntities(mapJournalEntities);

                    futures.add(pubmedExecutor.submit(pubmedWorker));
                } else {
                    DocumentEntity documentEntity = possibleDocument.get();
                    if (!queryService.isDocumentPresent(documentEntity))
                        // Add the existent document to the query
                        listOfDocuments.add(possibleDocument.get());
                }
            }

            // Get future results
            for (Future<DocumentEntity> fut : futures) {
                DocumentEntity documentEntity = fut.get();

                // Check if document is correct
                if (documentEntity != null)
                    listOfDocuments.add(fut.get());
            }
        } catch (Exception e) {
            log.error(AppConstants.STR_ERROR_UNEXPECTED, e);
        }

        return listOfDocuments;
    }
}