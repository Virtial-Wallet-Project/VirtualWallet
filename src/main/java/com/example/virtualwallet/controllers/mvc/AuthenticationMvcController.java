package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.helpers.TokenGenerator;
import com.example.virtualwallet.mappers.RegisterMapper;
import com.example.virtualwallet.DTOs.LoginDto;
import com.example.virtualwallet.DTOs.RegisterDto;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.EmailVerificationService;
import com.example.virtualwallet.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final UserService userService;
    private final RegisterMapper registerMapper;
    private final EmailVerificationService emailService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationMvcController(UserService userService, RegisterMapper registerMapper, EmailVerificationService emailService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.registerMapper = registerMapper;
        this.emailService = emailService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login-page";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto,
                              BindingResult bindingResult,
                              HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login-page";
        }

        try {
            authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            User user = userService.getByUsername(loginDto.getUsername());

            if (!user.isAccountVerified()) {
                String oldToken = user.getVerificationToken();
                String newToken = TokenGenerator.renewToken(oldToken);

                if (!oldToken.equals(newToken)) {
                    user.setVerificationToken(newToken);
                    userService.updateUser(user, user);
                    emailService.sendVerificationEmail(user.getEmail(), newToken);
                }

                return "verify-email";
            }

            session.setAttribute("currentUser", user);
            return "redirect:/";

        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "error.login", e.getMessage());
            return "login-page";
        }
    }


    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "register-page";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto registerDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "register-page";
        }

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm",
                    "error.register.password",
                    "Password conformation must match password");
            return "register-page";
        }

        try {
            User user = registerMapper.fromDto(registerDto);
            userService.createUser(user);
            return "verify-email";
        } catch (DuplicateEntityException e){
            bindingResult.rejectValue("username", "username.error", e.getMessage());
            return "register-page";
        }
    }

    @GetMapping("/verify")
    public String showVerifyPage(@RequestParam String token) {
        if (TokenGenerator.isTokenExpired(token)){
            return "invalid-token";
        }

        boolean isVerified = userService.verifyUser(token);
        if (isVerified){
            return "verified-page";
        } else {
            return "invalid-token";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
