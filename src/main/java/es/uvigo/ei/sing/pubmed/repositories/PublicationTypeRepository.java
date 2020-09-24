package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.PublicationTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PublicationTypeRepository extends CrudRepository<PublicationTypeEntity, Integer> {
    Optional<PublicationTypeEntity> findByName(String name);
}
