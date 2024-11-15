package anna.plantguide.service;


import anna.plantguide.model.MedicalCollection;
import anna.plantguide.repository.CompositionCollectRepository;
import anna.plantguide.repository.DiseaseRepository;
import anna.plantguide.repository.MedicalCollectionRepository;
import anna.plantguide.repository.MethodRepository;
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

    public List<MedicalCollection> getAllCollections() {
        return collectionRepository.findAll();
    }

    public void addCollection(Long userId, String name, Long deseaseId, String metodApplic,
                              String releaseForm, Integer countPlant, String plantArray) {
//        String sql = "call public.insert_medical_collection(?, ?, ?, ?, ?, ?, ?)";
        try {
//            jdbcTemplate.update(sql, name, userId, deseaseId, metodApplic,
//                                countPlant, plantArray, releaseForm);
            //......... i stay it with no comments
            String sql = "call public.insert_medical_collection(" + "'" + name + "'" + ", " + userId.toString() + ", "
                    + deseaseId.toString() + ", " + "'" + metodApplic + "'" + ", "
                    + countPlant.toString() + ", " + "'" + plantArray + "'" + ", " + "'" + releaseForm + "'" + ");";
            jdbcTemplate.update(sql);
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException("Ошибка при добавление расстения", e);
        }
    }
}
