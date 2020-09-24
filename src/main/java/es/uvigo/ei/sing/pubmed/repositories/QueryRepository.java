package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.DocumentEntity;
import es.uvigo.ei.sing.pubmed.entities.QueryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryRepository extends CrudRepository<QueryEntity, Integer> {
    Iterable<QueryEntity> findByIsSuspendedFalse();

    boolean existsByDocuments(DocumentEntity documentEntity);
}
