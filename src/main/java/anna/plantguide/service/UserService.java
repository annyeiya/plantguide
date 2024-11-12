package anna.plantguide.service;

import anna.plantguide.model.User;
import anna.plantguide.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    public String getUserLoginById(Long id) {
        return userRepository.findLoginById(id);
    }

    public boolean authenticateUser(String login, String password) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPassword().equals(password);
        } else {
            return false;
        }
    }

    public void setSessionRole(String login) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String role = user.getRole();
            String sql = "SET ROLE ";
            if ("ОП".equals(role)) {
                sql += "simpleuser";
            } else if ("Э".equals(role)) {
                sql += "expert";
            } else if ("А".equals(role)) {
                sql += "administrator";
            }
            try {
                jdbcTemplate.execute(sql);
                System.out.println("Роль изменена на " + role);
            } catch (Exception e) {
                System.err.println("Ошибка при изменении роли: " + e.getMessage());
            }
        }
    }

    public void resetRole() {
        String sql = "RESET ROLE";
        try {
            jdbcTemplate.execute(sql);
            System.out.println("Роль сброшена");
        } catch (Exception e) {
            System.err.println("Ошибка при изменении роли: " + e.getMessage());
        }
    }

    public String getUserRole(String login) {
        Optional<User> userOpt = userRepository.findByLogin(login);
        return userOpt.isPresent() ? userOpt.get().getRole() : "";
    }

    public boolean userExists(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public void registerUser(String login, String password, String fio, String role) {
        if (userExists(login)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }
        try {
            userRepository.registerUser(login, password, fio, role);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка при регистрации пользователя", e);
        }
    }
}
