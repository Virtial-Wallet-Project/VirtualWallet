package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.helpers.UserMapper;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;


@Controller
public class UserMvcController {

    private final UserService userService;
    private final CreditCardService creditCardService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    @Autowired
    public UserMvcController(UserService userService, CreditCardService creditCardService, AuthenticationHelper authenticationHelper, UserMapper userMapper) {
        this.userService = userService;
        this.creditCardService = creditCardService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @GetMapping("/account")
    public String showAccountPage(HttpSession session, Model model) {
        String currentUsername = (String) session.getAttribute("currentUser");
        if (currentUsername == null) {
            return "redirect:/auth/login";
        }

        User user = userService.getByUsername(currentUsername);

        CreditCard creditCard = creditCardService.getByUserId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("creditCard", creditCard);

        return "profile-page";
    }

    @PostMapping("/account/update")
    public String updateAccount(@Valid @ModelAttribute("user") User user,
                                BindingResult errors,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            int userId = loggedInUser.getUserId();

            if (!loggedInUser.isAdmin() && userId != loggedInUser.getUserId()) {
                throw new UnauthorizedOperationException("You can only update your own account.");
            }

            User updatedUser = userMapper.createUpdatedUserFromDto(user, userId);
            userService.updateUser(updatedUser, loggedInUser);

            redirectAttributes.addFlashAttribute("success", "Your account has been successfully updated!");

            return "redirect:/account";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("email", "user.exists", e.getMessage());
            return "profile-page";  // Re-render the profile page with errors
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "page-not-found";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }



    @PostMapping("/account/delete")
    public String deleteUser(@RequestParam("userId") int userId, HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            userService.deleteUser(loggedUser, userId);
            redirectAttributes.addFlashAttribute("success", "Your account has been deleted successfully.");
            return "redirect:/auth/logout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/account";
        }
    }

    @GetMapping("/add-credit-card")
    public String showCreditCardForm(Model model) {

        model.addAttribute("creditCard", new CreditCard());
        return "add-credit-card-page";
    }

    @PostMapping("/add-credit-card")
    public String addCreditCard(@Valid @ModelAttribute CreditCard creditCard,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        User user = authenticationHelper.tryGetUser(session);
        try {
            if (creditCard.getExpirationDate() == null) {
                throw new IllegalArgumentException("Expiration date is required.");
            }

            creditCardService.createCard(user, creditCard);

            return "redirect:/account";
        } catch (DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid expiration date format. Use YYYY-MM.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/add-credit-card";
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
