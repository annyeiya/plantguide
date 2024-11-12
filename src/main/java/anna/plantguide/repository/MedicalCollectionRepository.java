package anna.plantguide.repository;

import anna.plantguide.model.MedicalCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalCollectionRepository extends JpaRepository<MedicalCollection, Long> {
}

