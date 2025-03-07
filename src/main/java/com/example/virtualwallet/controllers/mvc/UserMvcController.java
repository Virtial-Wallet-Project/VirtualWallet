package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserMvcController {

    private final UserService userService;

    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String showAccountPage(HttpSession session, Model model) {
        String currentUsername = (String) session.getAttribute("currentUser");
        if (currentUsername == null) {
            return "redirect:/auth/login";
        }

        User user = userService.getByUsername(currentUsername);
        model.addAttribute("user", user);
        return "profile-page";
    }

        @PostMapping("/account/update")
        public String updateAccount(@Valid @ModelAttribute("user") User user,
                                    BindingResult bindingResult,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
            if (bindingResult.hasErrors()) {
                return "profile-page";
            }

            String currentUsername = (String) session.getAttribute("currentUser");
            if (currentUsername == null) {
                return "redirect:/auth/login";
            }

            User currentUser = userService.getByUsername(currentUsername);

            if (currentUser.getUserId() != (user.getUserId())) {
                redirectAttributes.addFlashAttribute("error", "You can only update your own account.");
                return "redirect:/account";
            }

            try {
                userService.updateUser(user, currentUser);
                redirectAttributes.addFlashAttribute("success", "Your account has been successfully updated.");
                return "redirect:/account";
            } catch (DuplicateEntityException | InvalidOperationException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/account";
            }
        }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
