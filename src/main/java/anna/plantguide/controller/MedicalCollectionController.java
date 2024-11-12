package anna.plantguide.controller;


import anna.plantguide.model.MedicalCollection;
import anna.plantguide.service.MedicalCollectionService;
import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MedicalCollectionController {

    @Autowired
    private MedicalCollectionService collectionService;

    @Autowired
    private UserService userService;

    @GetMapping("/collections")
    public String showCollections(HttpSession session, Model model) {
        List<MedicalCollection> collections = collectionService.getAllCollections();
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);
        model.addAttribute("collections", collections);
        return "collections";
    }
}
