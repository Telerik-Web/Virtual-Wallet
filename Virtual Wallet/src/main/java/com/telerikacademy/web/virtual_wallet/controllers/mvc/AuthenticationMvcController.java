package com.telerikacademy.web.virtual_wallet.controllers.mvc;


import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.helpers.CloudinaryHelper;
import com.telerikacademy.web.virtual_wallet.helpers.TokenGenerator;
import com.telerikacademy.web.virtual_wallet.mappers.CardMapper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.models.dtos.CardDTOExpiryDate;
import com.telerikacademy.web.virtual_wallet.models.dtos.LoginDTO;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTO;
import com.telerikacademy.web.virtual_wallet.models.dtos.UserDTOUpdate;
import com.telerikacademy.web.virtual_wallet.services.contracts.CardService;
import com.telerikacademy.web.virtual_wallet.services.contracts.UserService;
import com.telerikacademy.web.virtual_wallet.services.email_verification.EmailServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.telerikacademy.web.virtual_wallet.helpers.PermissionHelper.isValidImageFile;

@Controller
@RequestMapping("/auth")
public class AuthenticationMvcController {

    final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CardService cardService;
    private final CardMapper cardMapper;
    private final EmailServiceImpl emailService;
    private final CloudinaryHelper cloudinaryHelper;

    @Autowired
    public AuthenticationMvcController(AuthenticationHelper authenticationHelper,
                                       UserService userService,
                                       UserMapper userMapper, CardService cardService, CardMapper cardMapper,
                                       EmailServiceImpl emailService, CloudinaryHelper cloudinaryHelper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.cardService = cardService;
        this.cardMapper = cardMapper;
        this.emailService = emailService;
        this.cloudinaryHelper = cloudinaryHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "Login";
    }

    @PostMapping("/login")
    public String processLogin(@Valid @ModelAttribute("login") LoginDTO login,
                               BindingResult errors,
                               HttpSession session,
                               HttpServletResponse response) {
        if (errors.hasErrors()) {
            return "Login";
        }

        User user;
        try {
            user = authenticationHelper.verifyAuthentication(login.getUsername(), login.getPassword());

            if (!user.isAccountVerified()) {
                String token = TokenGenerator.renewToken(user.getVerificationToken());
                user.setVerificationToken(token);
                userService.update(user, user, user.getId());
                emailService.sendVerificationEmail(user.getEmail(), token);
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
            errors.rejectValue("passwordConfirm", "password.mismatch", "Passwords do not match.");
            return "Register";
        }

        try {
            userService.getByPhoneNumber(registerDto.getPhone());
            errors.rejectValue("phone", "phone.duplicate", "Phone number already exists.");
            return "Register";
        } catch (EntityNotFoundException e) {
        }

        try {
            userService.getByEmail(registerDto.getEmail());
            errors.rejectValue("email", "email.duplicate", "Email already exists.");
            return "Register";
        } catch (EntityNotFoundException e) {
        }

        try {
            userService.getByUsername(registerDto.getUsername());
            errors.rejectValue("username", "username.duplicate", "Username already exists.");
            return "Register";
        } catch (EntityNotFoundException e) {
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
            model.addAttribute("image", user.getPhoto());
            return "Account";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @GetMapping("/account/update")
    public String showUpdatePage(Model model,
                                 HttpSession session) {
        try {
            UserDTOUpdate user = userMapper.fromUserToUserDtoUpdate(authenticationHelper.tryGetUser(session));
            model.addAttribute("user", user);
            return "UpdateUser";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/account/update")
    public String showUserUpdateForm(@Valid @ModelAttribute("user") UserDTOUpdate user,
                                     BindingResult errors,
                                     HttpSession session) {
        if (errors.hasErrors()) {
            return "UpdateUser";
        }

        try {
            User updatedUser = userMapper.fromUserDtoUpdateToUser(user, authenticationHelper.tryGetUser(session));

            userService.update(updatedUser, updatedUser, authenticationHelper.tryGetUser(session).getId());
            return "redirect:/auth/account";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("password", "password_error", e.getMessage());
            return "UpdateUser";
        }
    }

    @PostMapping("/account/photo/upload")
    public String uploadProfilePhoto(@RequestParam("profileImage") MultipartFile profileImage,
                                     HttpSession session,
                                     Model model) {

        try {
            User user = authenticationHelper.tryGetUser(session);

            if (profileImage != null && !profileImage.isEmpty()) {
                try {

                    if (!isValidImageFile(profileImage)) {
                        model.addAttribute("error", "Invalid image file. Only JPG, PNG, or GIF files are allowed.");
                        return "Account";
                    }

                    if (profileImage.getSize() > MAX_FILE_SIZE) {
                        model.addAttribute("error", "File is too large! Max size is 5MB.");
                        return "Account";
                    }


                    String imageUrl = cloudinaryHelper.uploadUserProfilePhoto(profileImage, user);


                    user.setPhoto(imageUrl);
                    userService.update(user, user, authenticationHelper.tryGetUser(session).getId());

                } catch (IOException e) {
                    model.addAttribute("error", "An error occurred while uploading the image.");
                    return "Account";
                }
            }

            return "redirect:/auth/account";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/account/photo/remove")
    public String removeProfilePhoto(HttpSession session, Model model) {
        try {

            User user = authenticationHelper.tryGetUser(session);

            user.setPhoto("assets/img/default-user.jpg");
            userService.update(user, user, authenticationHelper.tryGetUser(session).getId());

            return "redirect:/auth/account";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @GetMapping("/account/delete")
    public String deleteAccount(HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        return "redirect:/auth/account/delete/confirm";
    }

    @GetMapping("/account/delete/confirm")
    public String showDeleteAccountConfirm(Model model, HttpSession session) {
        User user = authenticationHelper.tryGetUser(session);
        session.setAttribute("user", user);
        model.addAttribute("user", user);
        return "AccountDeleteConfirm";
    }

    @PostMapping("/account/delete/confirm")
    public String deleteAccountConfirm(HttpSession session) {
        User user = (User) session.getAttribute("user");
        userService.delete(user.getId(), user);
        session.invalidate();
        return "redirect:/";
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
            model.addAttribute("card", new CardDTOExpiryDate());
            return "AddCard";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/account/cards/new")
    public String addCard(@Valid @ModelAttribute("card") CardDTOExpiryDate cardDto,
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
        } catch (DuplicateEntityException e) {
            errors.rejectValue("cardNumber", "duplicate.cardNumber");
            return "AddCard";
        }
    }

    @PostMapping("/delete/card/{id}")
    public String deleteCard(@PathVariable int id,
                             HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
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
