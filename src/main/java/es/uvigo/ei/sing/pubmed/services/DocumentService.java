package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.DocumentEntity;
import es.uvigo.ei.sing.pubmed.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Optional<DocumentEntity> findByExternalId(String externalId) {
        return documentRepository.findByExternalId(externalId);
    }

    public void save(DocumentEntity documentEntity) {
        documentRepository.save(documentEntity);
    }

    public void saveAll(Set<DocumentEntity> documentEntities) {
        documentRepository.saveAll(documentEntities);
    }
}
