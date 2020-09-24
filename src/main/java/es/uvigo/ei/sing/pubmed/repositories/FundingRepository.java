package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.FundingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundingRepository extends CrudRepository<FundingEntity, Integer> {
    Optional<FundingEntity> findByNameAndAgency(String name, String agency);

    Optional<FundingEntity> findByHash(String hash);
}
