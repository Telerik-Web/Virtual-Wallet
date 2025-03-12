package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.models.FilterUserDto;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("currentUser")
    public Optional<User> populateCurrentUser(HttpSession session) {
        if (populateIsAuthenticated(session)) {
            String currentUsername = (String) session.getAttribute("currentUser");
            Optional.ofNullable(userService.getByUsername(currentUsername));
        }
        return Optional.empty();
    }

    @GetMapping("/admin")
    public String showFilteredPosts(@ModelAttribute("filterUserOptions") FilterUserDto filterDto, Model model, HttpSession session) {
        try {
            FilterUserOptions filterUserOptions = new FilterUserOptions(
                    filterDto.getUsername(),
                    filterDto.getEmail(),
                    filterDto.getPhoneNumber(),
                    null,
                    null
            );
            List<User> users = userService.getAll(filterUserOptions);
            User user = authenticationHelper.tryGetUser(session);
            model.addAttribute("users", users);
            model.addAttribute("filterUserOptions", filterDto);

            if (populateIsAuthenticated(session)) {
                String currentUsername = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByUsername(currentUsername));
            }

            if(user.getIsAdmin()){
                return "AdminPortal";
            }

            return "AccessDenied";
        } catch (AuthenticationFailureException e) {
            return "redirect:/auth/login";
        }

    }

    @PostMapping("/{id}/block")
    public String blockUser(@PathVariable int id, HttpSession session) {
        try {
            User adminUser = authenticationHelper.tryGetUser(session);
            if (adminUser.getIsAdmin()) {
                User targetUser = userService.getById(adminUser, id);
                userService.alterBlockPermissions(id, adminUser, !targetUser.getIsBlocked());
                return "redirect:/users/admin";
            }
            return "AccessDenied";
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
    }

    @PostMapping("/{id}/promote")
    public String promoteUser(@PathVariable int id, HttpSession session) {
        try{
            User adminUser = authenticationHelper.tryGetUser(session);
            if (adminUser.getIsAdmin()) {
                User targetUser = userService.getById(adminUser, id);
                userService.alterAdminPermissions(id, adminUser, !targetUser.getIsAdmin());
                return "redirect:/users/admin";
            }
            return "AccessDenied";
        } catch (AuthenticationFailureException e){
            return "AccessDenied";
        }
    }
}
