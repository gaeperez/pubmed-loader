package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.JournalEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JournalRepository extends CrudRepository<JournalEntity, Integer> {
    Optional<JournalEntity> findByNameAndIsbnAndIssn(String name, String isbn, String issn);
}
