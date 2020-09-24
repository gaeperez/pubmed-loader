package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.DocumentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends CrudRepository<DocumentEntity, Integer> {
    Optional<DocumentEntity> findByExternalId(String externalId);
}
