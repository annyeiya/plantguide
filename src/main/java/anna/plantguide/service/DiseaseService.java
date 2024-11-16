package anna.plantguide.service;

import anna.plantguide.repository.DiseaseRepository;
import anna.plantguide.repository.MedicialPlantRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiseaseService {
    @Autowired
    private DiseaseRepository repository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private DiseaseRepository diseaseRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Long findIdByName(String name) {
        String hql = "SELECT d.id FROM Disease d WHERE d.name =:name";
        Session session = entityManager.unwrap(Session.class);
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }
    public List<String> getAllDiseaseNames() {
        return diseaseRepository.findAllDiseaseNames();
    }

    public void updateDisease(Long diseaseId, String name) {
        try {
            //не спрашивайте почему тут сделано не так как во всем остальном коде
            diseaseRepository.updateDisease(name, diseaseId);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void addDisease(String name) {
        String sql = "CALL public.insert_desease(?)";
        try {
            jdbcTemplate.update(sql, name);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteDisease(String name) {
        Long id = findIdByName(name);
        diseaseRepository.deleteDisease(id);
    }
}
