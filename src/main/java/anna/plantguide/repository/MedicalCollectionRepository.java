package anna.plantguide.repository;

import anna.plantguide.model.MedicalCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MedicalCollectionRepository extends JpaRepository<MedicalCollection, Long> {

    @Query("SELECT c.name FROM MedicalCollection c")
    List<String> findAllCollectionName();

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM MedicalCollection c WHERE c.id = :id")
//    void deleteMedicalCollection(@Param("id") Long id);
}

