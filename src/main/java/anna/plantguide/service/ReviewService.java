package anna.plantguide.service;

import anna.plantguide.model.Review;
import anna.plantguide.repository.MedicialPlantRepo;
import anna.plantguide.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Review> getReviewByCollection(Long collectId) {
        return reviewRepository.findReviewByCollection(collectId);
    }

    public void addReview(Long collectionId, Long userId, Integer estimation, String textReview) {
        String sql = "CALL public.insert_review(?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, collectionId.intValue(), userId.intValue(), estimation, textReview);
            //TODO just fink abut the fun fact - intValue()
        } catch (DataAccessException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
