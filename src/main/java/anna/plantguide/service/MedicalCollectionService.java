package anna.plantguide.service;


import anna.plantguide.model.MedicalCollection;
import anna.plantguide.repository.CompositionCollectRepository;
import anna.plantguide.repository.DiseaseRepository;
import anna.plantguide.repository.MedicalCollectionRepository;
import anna.plantguide.repository.MethodRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MedicalCollectionService {
    @Autowired
    private MedicalCollectionRepository collectionRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public List<MedicalCollection> getAllCollections() {
        return collectionRepository.findAll();
    }

    public List<String> getAllCollectionName() {
        return collectionRepository.findAllCollectionName();
    }

    public Long findIdByName(String name) {
        String hql = "SELECT c.id FROM MedicalCollection c WHERE c.name =:name";
        Session session = entityManager.unwrap(Session.class);
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    public void addCollection(Long userId, String name, Long deseaseId, String metodApplic,
                              String releaseForm, Integer countPlant, String plantArray) {
//        String sql = "call public.insert_medical_collection(?, ?, ?, ?, ?, ?, ?)";
        try {
//            jdbcTemplate.update(sql, name, userId, deseaseId, metodApplic,
//                                countPlant, plantArray, releaseForm);
            //......... i stay it with no comments
            String sql = "call public.insert_medical_collection(" + "'" + name + "'" + ", " + userId + ", "
                    + deseaseId + ", " + "'" + metodApplic + "'" + ", "
                    + countPlant + ", " + "'" + plantArray + "'" + ", " + "'" + releaseForm + "'" + ");";
            jdbcTemplate.update(sql);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void updateCollection(Long id, Long userId, String name, Long deseaseId, String metodApplic,
                              String releaseForm, Integer countPlant, String plantArray) {
        try {
            //......... i stay it too with no comments
            String sql = "call public.update_medical_collection(" + id + ", " + "'" +
                    name + "'" + ", " +  userId + ", " + deseaseId + ", "
                    + "'" + metodApplic + "'" + ", " + countPlant + ", " +
                    "'" + plantArray + "'::jsonb" + ", " + "'" + releaseForm + "'" + ");";
            jdbcTemplate.update(sql);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void deleteCollection(String name) {
        Long id = findIdByName(name);
//        collectionRepository.deleteMedicalCollection(id);
        String sql = "DELETE FROM medicial_collection WHERE id = ?";
        try {
            jdbcTemplate.update(sql, id);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
