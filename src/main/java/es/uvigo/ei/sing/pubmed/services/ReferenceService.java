package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.ReferenceEntity;
import es.uvigo.ei.sing.pubmed.repositories.ReferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReferenceService {

    private final ReferenceRepository referenceRepository;

    @Autowired
    public ReferenceService(ReferenceRepository referenceRepository) {
        this.referenceRepository = referenceRepository;
    }

    public Optional<ReferenceEntity> findByExternalId(String externalId) {
        return referenceRepository.findByExternalId(externalId);
    }

    public void save(ReferenceEntity referenceEntity) {
        referenceRepository.save(referenceEntity);
    }
}
