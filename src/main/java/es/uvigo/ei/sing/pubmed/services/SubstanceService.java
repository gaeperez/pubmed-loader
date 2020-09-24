package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.SubstanceEntity;
import es.uvigo.ei.sing.pubmed.repositories.SubstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubstanceService {

    private final SubstanceRepository substanceRepository;

    @Autowired
    public SubstanceService(SubstanceRepository substanceRepository) {
        this.substanceRepository = substanceRepository;
    }

    public Optional<SubstanceEntity> findByName(String name) {
        return substanceRepository.findByName(name);
    }

    public void save(SubstanceEntity substanceEntity) {
        substanceRepository.save(substanceEntity);
    }
}
