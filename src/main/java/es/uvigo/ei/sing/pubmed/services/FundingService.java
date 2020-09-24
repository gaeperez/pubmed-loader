package es.uvigo.ei.sing.pubmed.services;

import es.uvigo.ei.sing.pubmed.entities.FundingEntity;
import es.uvigo.ei.sing.pubmed.repositories.FundingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FundingService {

    private final FundingRepository fundingRepository;

    @Autowired
    public FundingService(FundingRepository fundingRepository) {
        this.fundingRepository = fundingRepository;
    }

    public Optional<FundingEntity> findByNameAndAgency(String name, String agency) {
        return fundingRepository.findByNameAndAgency(name, agency);
    }

    public Optional<FundingEntity> findByHash(String hash) {
        return fundingRepository.findByHash(hash);
    }

    public void save(FundingEntity fundingEntity) {
        fundingRepository.save(fundingEntity);
    }
}
