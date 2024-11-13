package anna.plantguide.controller;

import anna.plantguide.model.MedicialPlant;
import anna.plantguide.repository.UserRepo;
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

@Controller
public class MedicialPlantController {
    @Autowired
    private MedicialPlantService plantService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/plants")
    public String getAllPlants(HttpSession session, Model model) {
        List<MedicialPlant> plants = plantService.getAllPlants();
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        for (MedicialPlant plant : plants) {
            String userLogin = userService.getUserLoginById(plant.getUser().getId().longValue());
            plant.setUserLogin(userLogin);
        }

        model.addAttribute("plants", plants);
        return "plantList";
    }

    @GetMapping("/manage_plant")
    public String getManagePlant(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);
        return "manage_plant";
    }

    @PostMapping("/manage_plant")
    public String addMedicalPlant(
            @RequestParam("name") String name,
            @RequestParam("contrand") String contrand,
            @RequestParam(value = "descript", required = false) String descript,
            @RequestParam(value = "gatherngPlace", required = false) String gatherngPlace,
            HttpSession session,
            Model model) {
        try {
            String userLogin = (String) session.getAttribute("userLogin");
            Long userId = userRepo.findIdByLogin(userLogin);
            plantService.addMedicalPlant(name, contrand, userId, descript, gatherngPlace);
            model.addAttribute("message", "Растение успешно добавлено!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка добавления растения: " + e.getMessage());
        }
        return "manage_plant";
    }
}
