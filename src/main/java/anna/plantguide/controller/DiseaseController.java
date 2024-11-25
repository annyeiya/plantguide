package anna.plantguide.controller;

import anna.plantguide.repository.UserRepo;
import anna.plantguide.service.DiseaseService;
import anna.plantguide.service.MedicialPlantService;
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
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private UserService userService;

    @GetMapping("/manage_disease")
    public String getDisease(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        List<String> diseaseNames = diseaseService.getAllDiseaseNames();
        model.addAttribute("names", diseaseNames);

        return "manage_disease";
    }

    @PostMapping("/manage_disease")
    public String addDisease(
            @RequestParam(value = "oldname", required = false) String oldname,
            @RequestParam("name") String name,
            HttpSession session,
            Model model) {
        if (Objects.equals(oldname, "")) {
            try {
                diseaseService.addDisease(name);
                model.addAttribute("message", "Болезнь успешно добавлено!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка добавления болезни: " + e.getMessage());
            }
        } else {
            //yes it is костыль
            if (name.isEmpty()) return getDisease(session, model);
            try {
                Long idDisease = diseaseService.findIdByName(oldname);
                diseaseService.updateDisease(idDisease, name);
                model.addAttribute("message", "Болезнь успешна обновленна!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка обновления болезни: " + e.getMessage());
            }
        }
        return getDisease(session, model);
    }
}
