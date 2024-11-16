package anna.plantguide.repository;

import anna.plantguide.model.MedicialPlant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicialPlantRepo extends JpaRepository<MedicialPlant, Long> {

    @Query("SELECT p.name FROM MedicialPlant p")
    List<String> findAllPlantName();

}
