package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.PublicationTypeEntity;
import es.uvigo.ei.sing.pubmed.repositories.PublicationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PublicationTypeService {

    private final PublicationTypeRepository publicationTypeRepository;

    @Autowired
    public PublicationTypeService(PublicationTypeRepository publicationTypeRepository) {
        this.publicationTypeRepository = publicationTypeRepository;
    }

    public Optional<PublicationTypeEntity> findByName(String name) {
        return publicationTypeRepository.findByName(name);
    }

    public void save(PublicationTypeEntity publicationTypeEntity) {
        publicationTypeRepository.save(publicationTypeEntity);
    }
}
