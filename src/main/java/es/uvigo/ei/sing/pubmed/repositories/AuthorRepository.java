package es.uvigo.ei.sing.pubmed.repositories;

import es.uvigo.ei.sing.pubmed.entities.AuthorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<AuthorEntity, Integer> {
    Optional<AuthorEntity> findByLastNameAndForeNameAndInitials(String lastName, String foreName, String initials);

    Optional<AuthorEntity> findByHash(String hash);
}
