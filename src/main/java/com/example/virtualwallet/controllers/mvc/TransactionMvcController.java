package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.DepositService;
import com.example.virtualwallet.service.TransactionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/wallet")
public class TransactionMvcController {

    private final UserRepository userRepository;
    private final CreditCardService creditCardService;
    private final TransactionService transactionService;
    private final DepositService depositService;

    @Autowired
    public TransactionMvcController(UserRepository userRepository, CreditCardService creditCardService, TransactionService transactionService, DepositService depositService) {
        this.userRepository = userRepository;
        this.creditCardService = creditCardService;
        this.transactionService = transactionService;
        this.depositService = depositService;
    }

    @GetMapping
    public String showWalletPage(HttpSession session, Model model) {
        Object userObj = session.getAttribute("currentUser");

        if (userObj == null) {
            return "redirect:/auth/login";
        }

        User user;
        if (userObj instanceof String) {
            user = userRepository.getByUsername((String) userObj);
            session.setAttribute("currentUser", user);
        } else {
            user = (User) userObj;
        }

        model.addAttribute("balance", user.getBalance());

        return "user-wallet";
    }


    @PostMapping("/deposit")
    public String depositMoney(
            @RequestParam double amount,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/login";
        }

        CreditCard userCard = creditCardService.getByUserId(user.getUserId());

        if (userCard == null) {
            redirectAttributes.addFlashAttribute("error", "You need to add a credit card before depositing money.");
            return "redirect:/account";
        }

        String resultMessage = depositService.depositMoney(user, userCard, amount);
        redirectAttributes.addFlashAttribute("message", resultMessage);

        return "redirect:/wallet";
    }

//    @PostMapping("/transfer")
//    public String transferMoney(@ModelAttribute("transactionDto") TransactionDto transactionDto, HttpSession session) {
//        User sender = (User) session.getAttribute("user");
//
//        if (sender == null) {
//            return "redirect:/login";
//        }
//
//        try {
//            transactionService.createTransaction(sender, transactionDto.getRecipientId(), transactionDto.getAmount(), transactionDto.);
//        } catch (InvalidOperationException | IllegalArgumentException e) {
//            session.setAttribute("errorMessage", e.getMessage());
//            return "redirect:/wallet";
//        }
//
//        return "redirect:/wallet";
//    }

    @GetMapping("/history")
    public String showTransactionHistory(HttpSession session, Model model) {
        Object userObj = session.getAttribute("currentUser");

        if (userObj == null) {
            return "redirect:/auth/login";
        }

        User user = (User) userObj;

        List<Transaction> transactions = transactionService.getAll(null, 0, 10, user);
        model.addAttribute("transactions", transactions);

        return "user-wallet";
    }


    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
