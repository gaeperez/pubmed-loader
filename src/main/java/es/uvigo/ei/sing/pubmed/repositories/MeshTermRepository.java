package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.MeshTermEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeshTermRepository extends CrudRepository<MeshTermEntity, Integer> {
    Optional<MeshTermEntity> findByDescriptorName(String descName);
}
