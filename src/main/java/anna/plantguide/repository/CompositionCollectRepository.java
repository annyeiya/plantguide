package anna.plantguide.repository;

import anna.plantguide.model.CompositionCollect;
import anna.plantguide.model.MedicalCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompositionCollectRepository extends JpaRepository<CompositionCollect, Long> {
    List<CompositionCollect> findByCollection(MedicalCollection collection);
}
