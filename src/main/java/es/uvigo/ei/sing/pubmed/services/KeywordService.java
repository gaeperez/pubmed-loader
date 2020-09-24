package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.KeywordEntity;
import es.uvigo.ei.sing.pubmed.repositories.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public Optional<KeywordEntity> findByName(String name) {
        return keywordRepository.findByName(name);
    }

    public void save(KeywordEntity keywordEntity) {
        keywordRepository.save(keywordEntity);
    }
}
