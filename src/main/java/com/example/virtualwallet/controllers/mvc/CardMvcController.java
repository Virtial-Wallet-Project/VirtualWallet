package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeParseException;

@Controller
public class CardMvcController {

    private final CreditCardService creditCardService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public CardMvcController(CreditCardService creditCardService, AuthenticationHelper authenticationHelper) {
        this.creditCardService = creditCardService;
        this.authenticationHelper = authenticationHelper;
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

//    @PostMapping("/delete-card")
//    public String deleteCreditCard(HttpSession httpSession, RedirectAttributes redirectAttributes) {
//        User  user = authenticationHelper.tryGetUser(httpSession);
//
//        try {
//            creditCardService.deleteCard(user);
//            redirectAttributes.addFlashAttribute("successMessage", "Credit card deleted successfully.");
//        } catch (InvalidOperationException e) {
//            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
//        }
//
//        return "redirect:/account";
//    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
