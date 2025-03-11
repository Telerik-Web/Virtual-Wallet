package com.telerikacademy.web.virtual_wallet.controllers.mvc;


import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.helpers.TokenGenerator;
import com.telerikacademy.web.virtual_wallet.mappers.CardMapper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.services.CardService;
import com.telerikacademy.web.virtual_wallet.services.email_verification.EmailServiceImpl;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final EmailServiceImpl emailService;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper,
                                       UserService userService,
                                       UserMapper userMapper, CardService cardService, CardMapper cardMapper, EmailServiceImpl emailService) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.emailService = emailService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("login", new LogInDto());
        return "Login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("login") LogInDto login,
                               BindingResult errors,
                               HttpSession session) {
        if (errors.hasErrors()) {
            return "Login";
        }

        User user;
        try {
            user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());
            if (!user.isAccountVerified()) {
                String token = TokenGenerator.renewToken(user.getVerificationToken());
//                System.out.println(user.getVerificationToken() + " 1");
//                user.setVerificationToken(null);
                user.setVerificationToken(token);
                userService.update(user, user, user.getId());
                //System.out.println(user.getVerificationToken() + " 2");
                emailService.sendVerificationEmail(user.getEmail(), token);
                //System.out.println(token + " 3");
                return "VerifyEmail";
            }
            session.setAttribute("currentUser", user.getUsername());
            session.setAttribute("isAdmin", user.getIsAdmin());
            session.setAttribute("cards", user.getCards());
            return "redirect:/";
        } catch (AuthenticationFailureException e) {
            errors.reject("Invalid authentication");
            return "Login";
        } catch (EntityNotFoundException e) {
            errors.reject("invalid.credentials", "Invalid username or password");
            return "Login";
        }
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("register", new UserDTO());
        return "Register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("register") UserDTO registerDto,
                                  BindingResult errors) {
        if (errors.hasErrors()) {
            return "Register";
        }

        if (!registerDto.getPassword().equals(registerDto.getPasswordConfirm())) {
            return "Register";
        }

        try {
            User user = userMapper.fromUserDto(registerDto);
            userService.create(user);
            return "VerifyEmail";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("username", "duplicate.username",
                    "Username is already taken!");
            return "Register";
        }
    }

    @GetMapping("/account")
    public String showAccountPage(Model model,
                                  HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            UserDTO userDto = userMapper.fromUsertoUserDto(user);
            model.addAttribute("user", userDto);
            return "Account";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @GetMapping("/account/update")
    public String showUpdatePage(Model model,
                                 HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("user", user);
            return "UpdateUser";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/account/update")
    public String showUserUpdateForm(@Valid @ModelAttribute("user") User user,
                                     BindingResult errors,
                                     HttpSession session) {
        if (errors.hasErrors()) {
            return "UpdateUser";
        }

        try {
            user.setId(authenticationHelper.tryGetUser(session).getId());
            user.setIsAdmin(authenticationHelper.tryGetUser(session).getIsAdmin());
            user.setIsBlocked(authenticationHelper.tryGetUser(session).getIsBlocked());
            userService.update(user, user, authenticationHelper.tryGetUser(session).getId());
            return "redirect:/auth/account";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("password", "password_error", e.getMessage());
            return "UpdateUser";
        }
    }

    @GetMapping("/account/cards")
    public String showAccountCards(Model model,
                                   HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            UserDTO userDto = userMapper.fromUsertoUserDto(user);
            model.addAttribute("user", userDto);
            model.addAttribute("cards", user.getCards());
            return "UserCards";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @GetMapping("/account/cards/new")
    public String showCardForm(Model model,
                               HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            UserDTO userDto = userMapper.fromUsertoUserDto(user);
            model.addAttribute("user", userDto);
            model.addAttribute("card", new CardDTO2());
            return "AddCard";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/account/cards/new")
    public String addCard(@Valid @ModelAttribute("card") CardDTO2 cardDto,
                          BindingResult errors,
                          Model model,
                          HttpSession session) {

        if (errors.hasErrors()) {
            try {
                User user = authenticationHelper.tryGetUser(session);
                model.addAttribute("user", userMapper.fromUsertoUserDto(user));
            } catch (AuthenticationFailureException e) {
                return "AccessDenied";
            }
            return "AddCard";
        }

        try {
            User user = authenticationHelper.tryGetUser(session);
            Card card = cardMapper.fromDTO2(cardDto);
            cardService.create(card, user);

            List<Card> updatedCards = cardService.getCardsByUserId(user.getId(), user);
            session.setAttribute("cards", updatedCards);
            return "redirect:/auth/account/cards";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable int id,
                             HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        Card card = cardService.getById(id, user);
        cardService.delete(card, user);
        if (cardService.getCardsByUserId(user.getId(), user).isEmpty()) {
            session.removeAttribute("cards");
        }
        return "redirect:/auth/account/cards";

    }

    @GetMapping("/logout")
    public String showLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        System.out.println(token.toString());
        if (TokenGenerator.isTokenExpired(token)) {
            return "TokenFail";
        }
        boolean isVerified = userService.verifyUser(token);
        if (isVerified) {
            return "VerifiedEmail";
        } else {
            return "TokenFail";
        }
    }
}
