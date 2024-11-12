package anna.plantguide.service;

import anna.plantguide.model.MedicialPlant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import anna.plantguide.repository.MedicialPlantRepo;

import java.util.List;


@Service
public class MedicialPlantService {
    @Autowired
    private MedicialPlantRepo repository;

    public List<MedicialPlant> getAllPlants() {
        return repository.findAll();
    }
}
