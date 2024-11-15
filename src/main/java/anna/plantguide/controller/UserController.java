package anna.plantguide.controller;

import anna.plantguide.repository.UserRepo;
import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/manage_user")
    public String getUser(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        List<String> userLogin = userRepo.findAllLogin();
        model.addAttribute("logins", userLogin);

        return "manage_user";
    }

    @PostMapping("/manage_user")
    public String addUser(
            @RequestParam(value = "oldname", required = false) String oldname,
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "fio", required = false) String fio,
            HttpSession session,
            Model model) {
        if (Objects.equals(oldname, "")) {
            try {
                userRepo.registerUser(login, password, fio, role);
                model.addAttribute("message", "Пользователь успешно добавлен!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка добавления пользователя: " + e.getMessage());
            }
        } else {
            login = login.isEmpty() ? null : login;
            password = password.isEmpty() ? null : password;
            role = role.isEmpty() ? null : role;
            fio = fio.isEmpty() ? null : fio;
            try {
                Long userId = userRepo.findIdByLogin(oldname);
                userRepo.updateUser(userId, login, password, role, fio);
                model.addAttribute("message", "Пользователь успешно обновлен!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка обновления пользователя: " + e.getMessage());
            }
        }
        return getUser(session, model);
    }
}
