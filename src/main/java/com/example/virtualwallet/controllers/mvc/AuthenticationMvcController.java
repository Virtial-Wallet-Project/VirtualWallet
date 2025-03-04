package com.example.virtualwallet.controllers.mvc;

import com.example.virtualwallet.exceptions.AuthenticationFailureException;
import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.helpers.AuthenticationHelper;
import com.example.virtualwallet.helpers.RegisterMapper;
import com.example.virtualwallet.models.LoginDto;
import com.example.virtualwallet.models.RegisterDto;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final UserService userService;
    private final RegisterMapper registerMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationMvcController(UserService userService, RegisterMapper registerMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.registerMapper = registerMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "login-page";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto loginDto
            , BindingResult bindingResult
            , HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login-page";
        }

        try {
            authenticationHelper.verifyAuthentication(loginDto.getUsername(), loginDto.getPassword());
            session.setAttribute("currentUser", loginDto.getUsername());

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
            return "redirect:/auth/login";
        } catch (DuplicateEntityException e){
            bindingResult.rejectValue("username", "username.error", e.getMessage());
            return "register-page";
        }
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
