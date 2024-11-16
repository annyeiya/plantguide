package anna.plantguide.controller;

import anna.plantguide.repository.MedicalCollectionRepository;
import anna.plantguide.repository.MedicialPlantRepo;
import anna.plantguide.repository.UserRepo;
import anna.plantguide.service.DiseaseService;
import anna.plantguide.service.MedicalCollectionService;
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
public class DeleteController {
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private MedicalCollectionService collectionService;
    @Autowired
    private MedicialPlantService plantService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/delete_page")
    public String getDeletePage(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        List<String> plantNames = plantService.getAllPlantsName();
        model.addAttribute("plantNames", plantNames);

        List<String> collectionNames = collectionService.getAllCollectionName();
        model.addAttribute("collectionNames", collectionNames);

        List<String> diseaseNames = diseaseService.getAllDiseaseNames();
        model.addAttribute("diseaseNames", diseaseNames);

        return "delete_page";
    }

    @PostMapping("/delete_page")
    public String postDeletePage(
            @RequestParam("action") String action,
            @RequestParam(value = "plant", required = false) String plant,
            @RequestParam(value = "collection", required = false) String collection,
            @RequestParam(value = "disease", required = false) String disease,
            HttpSession session,
            Model model) {
        try {
            if ("plant".equals(action)) {
                plantService.deleteMedicalPlant(plant);
                model.addAttribute("message", "Растение успешно удален!");
            } else if ("collection".equals(action)) {
                collectionService.deleteCollection(collection);
                model.addAttribute("message", "Сбор успешно удален!");
            } else if ("disease".equals(action)) {
                diseaseService.deleteDisease(disease);
                model.addAttribute("message", "Болезнь успешно удалена!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка обновления сбора: " + e.getMessage());
        }
        return getDeletePage(session, model);
    }
}
