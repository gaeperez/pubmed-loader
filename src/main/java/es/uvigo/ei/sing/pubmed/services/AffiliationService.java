package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.AffiliationEntity;
import es.uvigo.ei.sing.pubmed.repositories.AffiliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AffiliationService {

    private final AffiliationRepository affiliationRepository;

    @Autowired
    public AffiliationService(AffiliationRepository affiliationRepository) {
        this.affiliationRepository = affiliationRepository;
    }

    public Optional<AffiliationEntity> findByName(String name) {
        return affiliationRepository.findByName(name);
    }

    public Optional<AffiliationEntity> findByHash(String name) {
        return affiliationRepository.findByHash(name);
    }

    public void save(AffiliationEntity affiliationEntity) {
        affiliationRepository.save(affiliationEntity);
    }

    public List<AffiliationEntity> findByAffiliationsById(int id) {
        return affiliationRepository.findByAuthors_Id(id);
    }
}
