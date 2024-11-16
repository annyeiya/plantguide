package anna.plantguide.repository;

import anna.plantguide.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.management.ConstructorParameters;
import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Long> {

    @Query("SELECT d.name FROM Disease d")
    List<String> findAllDiseaseNames();

    @Modifying
    @Transactional
    @Query("UPDATE Disease d SET d.name = :name WHERE d.id = :diseaseId")
    void updateDisease(@Param("name") String name, @Param("diseaseId") Long diseaseId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Disease d WHERE d.id = :id")
    void deleteDisease(@Param("id") Long id);
}