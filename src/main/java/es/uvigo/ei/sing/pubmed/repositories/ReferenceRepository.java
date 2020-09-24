package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.ReferenceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReferenceRepository extends CrudRepository<ReferenceEntity, Integer> {
    Optional<ReferenceEntity> findByExternalId(String externalId);
}
