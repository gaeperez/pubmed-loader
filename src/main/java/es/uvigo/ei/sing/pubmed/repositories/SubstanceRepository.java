package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.SubstanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubstanceRepository extends CrudRepository<SubstanceEntity, Integer> {
    Optional<SubstanceEntity> findByName(String name);
}
