package anna.plantguide.service;


import anna.plantguide.model.MedicalCollection;
import anna.plantguide.repository.CompositionCollectRepository;
import anna.plantguide.repository.DiseaseRepository;
import anna.plantguide.repository.MedicalCollectionRepository;
import anna.plantguide.repository.MethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MedicalCollectionService {
    @Autowired
    private MedicalCollectionRepository collectionRepository;

    @Autowired
    private CompositionCollectRepository compositionCollectRepository;

    @Autowired
    private MethodRepository methodRepository;

    @Autowired
    private DiseaseRepository diseaseRepository;

    public List<MedicalCollection> getAllCollections() {
        return collectionRepository.findAll();
    }
}
