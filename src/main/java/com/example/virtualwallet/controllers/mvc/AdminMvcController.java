package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.models.FilterUserDto;
import com.example.virtualwallet.models.FilterUserOptions;
import com.example.virtualwallet.models.LoginDto;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AdminMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
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
                model.addAttribute("login", new LoginDto()); // Ensure login object is added back
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

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard(@ModelAttribute("filterUserDto") FilterUserDto filterUserDto, HttpSession session, Model model) {
        User admin = (User) session.getAttribute("admin");
        if (admin == null || !admin.isAdmin()) {
            return "redirect:/admin/login";
        }

        model.addAttribute("users", userService.getAll(new FilterUserOptions(
                filterUserDto.getUsername(),
                filterUserDto.getEmail(),
                filterUserDto.getPhoneNumber(),
                filterUserDto.getSortBy(),
                filterUserDto.getSortOrder()), 5, 4));

        model.addAttribute("filterUserDto", new FilterUserDto());
        model.addAttribute("admin", admin);
        return "admin-page";
    }

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
