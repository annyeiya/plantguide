package anna.plantguide.repository;

import anna.plantguide.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    @Query("SELECT d.name FROM Disease d")
    List<String> findAllDiseaseNames();
}