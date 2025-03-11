package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
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
            @RequestParam(defaultValue = "10") int size) {

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

//
//    @GetMapping("/transactions")
//    public String showAllTransactions(@ModelAttribute("filterTransactionDto") FilterTransactionDto filterTransactionDto,
//                                      HttpSession session, Model model,
//                                      @RequestParam(defaultValue = "0") int page,
//                                      @RequestParam(defaultValue = "10") int size) {
//        User admin = (User) session.getAttribute("admin");
//        if (admin == null || !admin.isAdmin()) {
//            return "redirect:/admin/login";
//        }
//
//        Page<Transaction> transactions = transactionService.getAll(
//                new FilterTransactionOptions(
//                        filterTransactionDto.getUserId(),
//                        filterTransactionDto.getSenderId(),
//                        filterTransactionDto.getRecipientId(),
//                        filterTransactionDto.getStartDate(),
//                        filterTransactionDto.getEndDate(),
//                        filterTransactionDto.getSortBy(),
//                        filterTransactionDto.getSortOrder()
//                ),
//        );
//
//        model.addAttribute("transactions", transactions);
//        model.addAttribute("filterTransactionDto", new FilterTransactionDto()); // For filtering form
//        return "admin-transactions";
//    }

    @PostMapping("/users/{id}/block")
    public String blockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.blockUser(admin, id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/unblock")
    public String unblockUser(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.unblockUser(admin, id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/make-admin")
    public String makeAdmin(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.makeAdmin(admin, id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/users/{id}/remove-admin")
    public String removeAdmin(@PathVariable int id, HttpSession session) {
        User admin = (User) session.getAttribute("admin");

        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        userService.removeAdmin(admin, id);
        return "redirect:/admin/dashboard";
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
