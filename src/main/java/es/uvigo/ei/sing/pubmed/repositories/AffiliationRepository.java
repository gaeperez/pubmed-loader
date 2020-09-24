package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.AffiliationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliationRepository extends CrudRepository<AffiliationEntity, Integer> {
    Optional<AffiliationEntity> findByName(String name);

    Optional<AffiliationEntity> findByHash(String hash);

    List<AffiliationEntity> findByAuthors_Id(int id);
}
