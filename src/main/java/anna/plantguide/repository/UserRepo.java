package anna.plantguide.repository;

import anna.plantguide.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepo {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String findLoginById(Long id) {
        String hql = "SELECT u.login FROM User u WHERE u.id =:id";
        Session session = entityManager.unwrap(Session.class);
        Query<String> query = session.createQuery(hql, String.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    public Optional<User> findByLogin(String login) {
        String hql = "FROM User u WHERE u.login =:login";
        Session session = entityManager.unwrap(Session.class);
        Query<User> query = session.createQuery(hql, User.class);
        query.setParameter("login", login);
        return query.getResultList().stream().findFirst();
    }

    public void registerUser(String login, String password, String fio, String role) {
        try {
            String sql = "INSERT INTO users (login, password, fio, role) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, login, password, fio, role);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при регистрации пользователя", e);
        }
    }
}
