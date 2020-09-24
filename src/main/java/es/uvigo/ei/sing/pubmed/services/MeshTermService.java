package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.MeshTermEntity;
import es.uvigo.ei.sing.pubmed.repositories.MeshTermRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MeshTermService {

    private final MeshTermRepository meshTermRepository;

    @Autowired
    public MeshTermService(MeshTermRepository meshTermRepository) {
        this.meshTermRepository = meshTermRepository;
    }

    public Optional<MeshTermEntity> findByDescriptorName(String descName) {
        return meshTermRepository.findByDescriptorName(descName);
    }

    public void save(MeshTermEntity meshTermEntity) {
        meshTermRepository.save(meshTermEntity);
    }
}
