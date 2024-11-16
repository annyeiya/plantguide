package anna.plantguide.controller;

import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showLogin() {
        userService.resetRole();
        return "index";
    }

    @GetMapping("/registration")
    public String showRegistration() {
        return "registration";
    }

    @PostMapping("/")
    public String loginUser(@RequestParam(value = "login", required = false) String login,
                                @RequestParam(value = "password", required = false) String password,
                               @RequestParam("action") String action,
                               HttpSession session,
                               Model model) {
        if ("login".equals(action)) {

            boolean isLogin = userService.authenticateUser(login, password);
            session.setAttribute("userLogin", login);
            if (isLogin) {
                userService.setSessionRole(login);
                return "redirect:/plants";
            } else {
                model.addAttribute("error", "Неверный логин или пароль");
                return "index";
            }

        } else if ("register".equals(action)) {

            return "redirect:/registration";

        } else if ("continue".equals(action)) {

            session.setAttribute("userLogin", null);
            userService.setSessionRole(null);
            return "redirect:/plants";

        }
        return "index";
    }

    @PostMapping("/registration")
    public String registerUser(@RequestParam("login") String login,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               @RequestParam(value = "fio", required = false) String fio,
                               HttpSession session,
                               Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "registration";
        }
        try {
            userService.registerUser(login, password, fio, "ОП");
            model.addAttribute("message", "Вы успешно зарегистрированы!");
            session.setAttribute("userLogin", login);
            userService.setSessionRole(login);
            return "redirect:/plants";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registration";
        }
    }
}
