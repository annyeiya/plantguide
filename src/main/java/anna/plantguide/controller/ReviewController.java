package anna.plantguide.controller;

import anna.plantguide.model.Review;
import anna.plantguide.repository.UserRepo;
import anna.plantguide.service.MedicalCollectionService;
import anna.plantguide.service.ReviewService;
import anna.plantguide.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class ReviewController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private MedicalCollectionService collectionService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/addReview")
    public String getAddReview(HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        List<String> collectionName = collectionService.getAllCollectionName();
        model.addAttribute("collections", collectionName);

        return "addReview";
    }

    @PostMapping("/addReview")
    public String postAddReview(
            @RequestParam("collection") String collection,
            @RequestParam("estimation") Integer estimation,
            @RequestParam(value = "textReview", required = false) String textReview,
            HttpSession session,
            Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        try {
            String userLogin = (String) session.getAttribute("userLogin");
            Long userId = userRepo.findIdByLogin(userLogin);
            Long collectionId = collectionService.findIdByName(collection);
            reviewService.addReview(collectionId, userId, estimation, textReview);
            model.addAttribute("message", "Отзыв успешно записан!");
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка добавления отзыва: " + e.getMessage());
        }
        return getAddReview(session, model);
    }

    @GetMapping("/reviews/{name}")
    public String getReviews(@PathVariable String name, HttpSession session, Model model) {
        String userSessionLogin = (String) session.getAttribute("userLogin");
        model.addAttribute("role", userService.getUserRole(userSessionLogin));
        model.addAttribute("login", userSessionLogin);

        Long idCollection = collectionService.findIdByName(name);
        List<Review> reviews = reviewService.getReviewByCollection(idCollection);
        model.addAttribute("reviews", reviews);
        return "reviews";
    }
}
