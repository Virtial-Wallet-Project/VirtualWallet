package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.filtering.DTOs.FilterTransactionDto;
import com.example.virtualwallet.DTOs.LoginDto;
import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.filtering.DTOs.FilterUserDto;
import com.example.virtualwallet.filtering.FilterUserOptions;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.service.TransactionService;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(UserService userService, TransactionService transactionService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "admin-login-page";
    }

    @PostMapping("/login")
    public String handleLogin(HttpSession session, Model model, @ModelAttribute("login") LoginDto loginDto) {
        try {
            authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());

            User user = userService.getByUsername(loginDto.getUsername());

            if (user == null || !user.isAdmin()) {
                model.addAttribute("error", "Access Denied! Only administrators can log in.");
                model.addAttribute("login", new LoginDto());
                return "admin-login-page";
            }

            session.setAttribute("admin", user);
            return "redirect:/admin/dashboard";
        } catch (AuthenticationFailureException e) {
            model.addAttribute("error", "Invalid credentials.");
            model.addAttribute("login", new LoginDto());
            return "admin-login-page";
        }
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        return "admin-page";
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        session.removeAttribute("admin");
        return "redirect:/";
    }

    @GetMapping("/users")
    public String showAllUsers(
            @ModelAttribute("filterDto") FilterUserDto filterUserDto,
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        List<User> users = userService.getAll(new FilterUserOptions(
                        filterUserDto.getUsername(),
                        filterUserDto.getEmail(),
                        filterUserDto.getPhoneNumber(),
                        filterUserDto.getSortBy(),
                        filterUserDto.getSortOrder()),
                page, size, admin);

        model.addAttribute("users", users);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("filterDto", filterUserDto);

        return "admin-users";
    }

    @GetMapping("/transactions")
    public String showAllTransactions(
            @ModelAttribute("filterTransactionsDto") FilterTransactionDto filterTransactionDto,
            HttpSession session,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        filterTransactionDto.setSender(
                Optional.ofNullable(filterTransactionDto.getSender()).filter(s -> !s.isEmpty()).orElse(null)
        );

        filterTransactionDto.setRecipient(
                Optional.ofNullable(filterTransactionDto.getRecipient()).filter(r -> !r.isEmpty()).orElse(null)
        );

        List<Transaction> transactions = transactionService.getAll(
                new FilterTransactionOptions(
                        filterTransactionDto.getUserId(),
                        filterTransactionDto.getSender(),
                        filterTransactionDto.getRecipient(),
                        filterTransactionDto.getStartDate(),
                        filterTransactionDto.getEndDate(),
                        filterTransactionDto.getSortBy(),
                        filterTransactionDto.getSortOrder()
                ),
                page, size, admin
        );



        model.addAttribute("transactions", transactions);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("filterTransactionsDto", filterTransactionDto);

        return "admin-transactions";
    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.blockUser(admin, id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.unblockUser(admin, id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/make-admin")
    public String makeAdmin(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.makeAdmin(admin, id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/remove-admin")
    public String removeAdmin(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.removeAdmin(admin, id);
        return "redirect:/admin/users";
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
