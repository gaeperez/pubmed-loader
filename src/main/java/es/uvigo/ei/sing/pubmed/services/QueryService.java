package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.DocumentEntity;
import es.uvigo.ei.sing.pubmed.entities.QueryEntity;
import es.uvigo.ei.sing.pubmed.repositories.QueryRepository;
import es.uvigo.ei.sing.pubmed.utils.AppConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
public class QueryService {

    private final QueryRepository queryRepository;

    @Autowired
    public QueryService(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public void save(QueryEntity queryEntity) {
        queryRepository.save(queryEntity);
    }

    @Transactional
    public QueryEntity save(QueryEntity queryEntity, Set<DocumentEntity> documentEntities, int currentPage, boolean lastInsert) {
        String message;

        if (!documentEntities.isEmpty()) {
            message = String.format(AppConstants.STR_INFO_SAVING_DOCS, documentEntities.size());
            queryEntity.getDocuments().addAll(documentEntities);
        } else
            message = AppConstants.STR_INFO_NOT_SAVING_DOCS;

        if (lastInsert) {
            queryEntity.incrementTimesExecuted();
            queryEntity.setCurrentPage(0);
        } else
            queryEntity.setCurrentPage(currentPage);
        queryEntity.setUpdated(LocalDateTime.now());
        queryEntity.setMessage(message);

        try {
            log.info(message);
            queryEntity = queryRepository.save(queryEntity);
        } catch (Exception e) {
            message = String.format(AppConstants.STR_ERROR_SAVING_DOCS, e);
            queryEntity.setMessage(message);

            log.error(message);
        }

        return queryEntity;
    }

    public void saveAll(Set<QueryEntity> queryEntities) {
        queryRepository.saveAll(queryEntities);
    }

    public Optional<QueryEntity> findById(int id) {
        return queryRepository.findById(id);
    }

    public Iterable<QueryEntity> findAll() {
        return queryRepository.findAll();
    }

    public Iterable<QueryEntity> findByIsSuspendedFalse() {
        return queryRepository.findByIsSuspendedFalse();
    }

    public boolean isDocumentPresent(DocumentEntity documentEntity) {
        return queryRepository.existsByDocuments(documentEntity);
    }
}
