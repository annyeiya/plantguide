package anna.plantguide.controller;

import anna.plantguide.model.MedicialPlant;
import anna.plantguide.service.MedicialPlantService;

import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MedicialPlantController {
    @Autowired
    private MedicialPlantService plantService;
    @Autowired
    private UserService userService;

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
}
