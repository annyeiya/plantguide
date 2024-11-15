package anna.plantguide.service;

import anna.plantguide.repository.DiseaseRepository;
import anna.plantguide.repository.MedicialPlantRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseService {
    @Autowired
    private DiseaseRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    public Long findIdByName(String name) {
        String hql = "SELECT d.id FROM Disease d WHERE d.name =:name";
        Session session = entityManager.unwrap(Session.class);
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    @Autowired
    private DiseaseRepository diseaseRepository;

    public List<String> getAllDiseaseNames() {
        return diseaseRepository.findAllDiseaseNames();
    }
}
