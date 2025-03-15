package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.filtering.DTOs.FilterTransactionDto;
import com.example.virtualwallet.DTOs.TransferDto;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.service.CreditCardService;
import com.example.virtualwallet.service.DepositService;
import com.example.virtualwallet.service.TransactionService;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/wallet")
public class TransactionMvcController {

    private final UserService userService;
    private final CreditCardService creditCardService;
    private final TransactionService transactionService;
    private final DepositService depositService;

    @Autowired
    public TransactionMvcController(UserService userService, CreditCardService creditCardService, TransactionService transactionService, DepositService depositService) {
        this.userService = userService;
        this.creditCardService = creditCardService;
        this.transactionService = transactionService;
        this.depositService = depositService;
    }

    @GetMapping
    public String showWalletPage(HttpSession session, Model model, @ModelAttribute("message") String message,
                                 @ModelAttribute("error") String error) {
        Object userObj = session.getAttribute("currentUser");

        if (userObj == null) {
            return "redirect:/auth/login";
        }

        User user;
        if (userObj instanceof String) {
            user = userService.getByUsername((String) userObj);
            session.setAttribute("currentUser", user);
        } else {
            user = (User) userObj;
        }

        model.addAttribute("balance", user.getBalance());
        model.addAttribute("message", message);
        model.addAttribute("error", error);

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

        List<CreditCard> userCards = creditCardService.getByUserId(user.getUserId());

        if (userCards == null || userCards.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "You need to add a credit card before depositing money.");
            return "redirect:/account";
        }

        CreditCard userCard = userCards.get(0);

        String resultMessage = depositService.depositMoney(user, userCard, amount);
        redirectAttributes.addFlashAttribute("message", resultMessage);

        return "redirect:/wallet";
    }


    @GetMapping("/transactions/")
    public String showAllTransactions(@ModelAttribute("filterTransactionsUserDto") FilterTransactionDto filterTransactionDto,
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/auth/login";
        }

        List<Transaction> transactions = transactionService.getAll(
                new FilterTransactionOptions(
                        null, null,null,null,null,null, null
                ),
                page, size, user
        );

        model.addAttribute("transactions", transactions);
        model.addAttribute("currentUserPage", page);
        model.addAttribute("pageUserSize", size);
        model.addAttribute("filterTransactionsUserDto", filterTransactionDto);
        model.addAttribute("isAuthenticated", true);

        return "user-wallet";
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model, HttpSession session) {
        User sender = (User) session.getAttribute("currentUser");
        if (sender == null) {
            return "redirect:/auth/login";
        }

        model.addAttribute("transferDto", new TransferDto());
        return "transfer";
    }

    @PostMapping("/transfer")
    public String processTransfer(
            @ModelAttribute("transferDto") TransferDto transferDto,
            HttpSession session,
            Model model) {

        User sender = (User) session.getAttribute("currentUser");
        if (sender == null) {
            return "redirect:/auth/login";
        }

        User recipient;
        try {
            recipient = userService.getByUsernameOrEmailOrPhone(transferDto.getRecipientIdentifier());
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "Recipient not found.");
            return "transfer";
        }

        if (sender.getUserId() == recipient.getUserId()) {
            model.addAttribute("error", "You cannot send money to yourself!");
            return "transfer";
        }

        if (sender.getBalance() < transferDto.getAmount()) {
            model.addAttribute("error", "Insufficient funds!");
            return "transfer";
        }

        model.addAttribute("sender", sender);
        model.addAttribute("recipient", recipient);
        model.addAttribute("amount", transferDto.getAmount());
        return "transfer-confirmation";
    }


    @PostMapping("/transfer/confirm")
    public String confirmTransfer(
            @RequestParam int recipientId,
            @RequestParam double amount,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User sender = (User) session.getAttribute("currentUser");
        if (sender == null) {
            return "redirect:/auth/login";
        }

        User recipient = userService.getById(sender, recipientId);
        if (recipient == null) {
            redirectAttributes.addFlashAttribute("error", "Recipient not found.");
            return "redirect:/wallet/transfer";
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setRecipient(recipient);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionService.createTransaction(sender, recipient.getUserId(), transaction);
        redirectAttributes.addFlashAttribute("success", "Transfer successful!");

        return "redirect:/wallet";
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
