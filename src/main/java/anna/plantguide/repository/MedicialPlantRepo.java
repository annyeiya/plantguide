package anna.plantguide.repository;

import anna.plantguide.model.MedicialPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicialPlantRepo extends JpaRepository<MedicialPlant, Long> {
}
