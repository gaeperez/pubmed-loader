package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.JournalEntity;
import es.uvigo.ei.sing.pubmed.repositories.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    @Autowired
    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public Optional<JournalEntity> findByNameAndIsbnAndIssn(String name, String isbn, String issn) {
        return journalRepository.findByNameAndIsbnAndIssn(name, isbn, issn);
    }

    public void save(JournalEntity journalEntity) {
        journalRepository.save(journalEntity);
    }
}
