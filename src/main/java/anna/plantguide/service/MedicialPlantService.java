package anna.plantguide.service;

import anna.plantguide.model.MedicialPlant;
import anna.plantguide.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import anna.plantguide.repository.MedicialPlantRepo;

import java.util.List;
import java.util.Optional;


@Service
public class MedicialPlantService {
    @Autowired
    private MedicialPlantRepo repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;


    public List<MedicialPlant> getAllPlants() {
        return repository.findAll();
    }

    public List<String> getAllPlantsName() {
        return repository.findAllPlantName();
    }

    public Optional<MedicialPlant> getPlantByName(String name) {
        String hql = "FROM MedicialPlant p WHERE p.name =:name";
        Session session = entityManager.unwrap(Session.class);
        Query<MedicialPlant> query = session.createQuery(hql, MedicialPlant.class);
        query.setParameter("name", name);
        return query.getResultList().stream().findFirst();
    }

    public Long findIdByName(String name) {
        String hql = "SELECT p.id FROM MedicialPlant p WHERE p.name =:name";
        Session session = entityManager.unwrap(Session.class);
        Query<Long> query = session.createQuery(hql, Long.class);
        query.setParameter("name", name);
        return query.uniqueResult();
    }

    public void updateMedicialPlant(Long userId, Long id, String name, String contrand, String descript, String gatherngPlace) {
        String sql = """
        UPDATE medicial_plant
        SET
            name = COALESCE(?, name),
            descript = COALESCE(?, descript),
            gatherng_place = COALESCE(?, gatherng_place),
            contrand = COALESCE(?, contrand),
            id_user = ?
        WHERE id = ?
        """;

        try {
            int rowsAffected =  jdbcTemplate.update(sql, name, descript, gatherngPlace, contrand, userId.intValue(), id);

            if (rowsAffected == 0) {
                throw new RuntimeException("Растение с указанным id не найдено: " + id);
            }
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException("Ошибка при обновлении данных растения", e);
        }
    }


    public void addMedicalPlant(String name, String contrand, Long userId, String descript, String gatherngPlace) {
        String sql = "CALL public.insert_medcal_plant(?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, name, contrand, userId.intValue(), descript, gatherngPlace);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException("Ошибка при добавление расстения", e);
        }
    }
}
