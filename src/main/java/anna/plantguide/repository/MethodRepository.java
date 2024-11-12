package anna.plantguide.repository;

import anna.plantguide.model.MedicalCollection;
import anna.plantguide.model.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MethodRepository extends JpaRepository<Method, Long> {
    List<Method> findByCollection(MedicalCollection collection);
}

