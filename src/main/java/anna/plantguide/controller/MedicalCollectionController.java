package anna.plantguide.controller;


import anna.plantguide.model.MedicalCollection;
import anna.plantguide.model.MedicialPlant;
import anna.plantguide.repository.MedicialPlantRepo;
import anna.plantguide.repository.UserRepo;
import anna.plantguide.service.DiseaseService;
import anna.plantguide.service.MedicalCollectionService;
import anna.plantguide.service.MedicialPlantService;
import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class MedicalCollectionController {

    @Autowired
    private MedicalCollectionService collectionService;
    @Autowired
    private UserService userService;
    @Autowired
    private MedicialPlantService plantService;
    @Autowired
    private DiseaseService diseaseService;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/collections")
    public String showCollections(HttpSession session, Model model) {
        List<MedicalCollection> collections = collectionService.getAllCollections();
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);
        model.addAttribute("collections", collections);
        return "collections";
    }

    @GetMapping("/manage_collection")
    public String getManagePlant(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        List<String> CollectionNames = collectionService.getAllCollectionName();
        model.addAttribute("names", CollectionNames);

        Map<Long, String> plantIdName = new HashMap<>();
        for (MedicialPlant plant : plantService.getAllPlants()) {
            plantIdName.put(plant.getId(), plant.getName());
        }
        model.addAttribute("plantIdName", plantIdName);

        List<String> diseaseNames = diseaseService.getAllDiseaseNames();
        model.addAttribute("diseases", diseaseNames);

        return "manage_collection";
    }

    @PostMapping("/manage_collection")
    public String addMedicalPlant(
            @RequestParam(value = "oldname", required = false) String oldname,
            @RequestParam("name") String name,
            @RequestParam("desease") String desease,
            @RequestParam("metodApplic") String metodApplic,
            @RequestParam(value = "countPlant") String countPlant,
            @RequestParam(value = "releaseForm", required = false) String releaseForm,
            @RequestParam("plantDetail") String plantDetail,
            HttpSession session,
            Model model) {

        if (Objects.equals(oldname, "")) {
            System.out.println(plantDetail);
            try {
//                JSONArray plantArray = new JSONArray();
//                for (PlantDetail detail : plantDetail) {
//                    JSONObject plantObject = new JSONObject();
//                    Long plantId = plantService.findIdByName(detail.getNamePlant());
//                    if (plantId == null) {
//                        throw new RuntimeException("Не найдено растение с именем: " + detail.getNamePlant());
//                    }
//                    plantObject.put("id_plant", plantId);
//                    plantObject.put("time_gatherng", detail.getTimeGatherng());
//                    plantObject.put("part_plant", detail.getPartPlant());
//                    plantArray.put(plantObject);
//                }
                Integer countPlantint = Integer.parseInt(countPlant);
                String userLogin = (String) session.getAttribute("userLogin");
                Long userId = userRepo.findIdByLogin(userLogin);
                Long deseaseId = diseaseService.findIdByName(desease);

                collectionService.addCollection(userId, name, deseaseId, metodApplic, releaseForm, countPlantint, plantDetail);
                model.addAttribute("message", "Сбор успешно добавлено!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка добавления сбора: " + e.getMessage());
            }
        } else {
            try {
                Integer countPlantint = countPlant.isEmpty() ? 0 : Integer.parseInt(countPlant);
                plantDetail = plantDetail.isEmpty() ? "[]" : plantDetail;
                String userLogin = (String) session.getAttribute("userLogin");
                Long userId = userRepo.findIdByLogin(userLogin);
                Long deseaseId = diseaseService.findIdByName(desease);
                Long idCollection = collectionService.findIdByName(oldname);
                collectionService.updateCollection(idCollection, userId, name, deseaseId,
                                metodApplic, releaseForm, countPlantint, plantDetail);
                model.addAttribute("message", "Сбор успешно обновлен!");
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка обновления сбора: " + e.getMessage());
            }
        }
        return getManagePlant(session, model);
    }
}
