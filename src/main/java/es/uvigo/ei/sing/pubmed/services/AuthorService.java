package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.AffiliationEntity;
import es.uvigo.ei.sing.pubmed.entities.AuthorEntity;
import es.uvigo.ei.sing.pubmed.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Iterable<AuthorEntity> findAll() {
        return authorRepository.findAll();
    }

    public Optional<AuthorEntity> findByLastNameAndForeNameAndInitials(String lastName, String foreName, String initials) {
        return authorRepository.findByLastNameAndForeNameAndInitials(lastName, foreName, initials);
    }

    public Optional<AuthorEntity> findByHash(String hash) {
        return authorRepository.findByHash(hash);
    }

    public Set<AffiliationEntity> obtainNewAffiliations(Set<AffiliationEntity> authorAffiliations,
                                                        Set<AffiliationEntity> newAffiliations) {
        Set<AffiliationEntity> toRet = new HashSet<>();

        if (authorAffiliations != null && newAffiliations != null) {
            // If the author does not have any affiliation, set all the new affiliations
            if (authorAffiliations.isEmpty())
                toRet = newAffiliations;
            else if (!newAffiliations.isEmpty())
                // Get only the newAffiliations not contained in authorAffiliations
                toRet = newAffiliations.stream().filter(newAffiliation -> !authorAffiliations.contains(newAffiliation))
                        .collect(Collectors.toSet());
        }

        return toRet;
    }

    public Optional<AuthorEntity> findById(Integer id) {
        return authorRepository.findById(id);
    }

    public void save(AuthorEntity authorEntity) {
        authorRepository.save(authorEntity);
    }

    public void saveAll(Set<AuthorEntity> authorEntities) {
        authorRepository.saveAll(authorEntities);
    }

    public void delete(AuthorEntity authorEntity) {
        authorRepository.delete(authorEntity);
    }
}
