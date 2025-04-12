package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.models.FilterUserDto;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TransactionService transactionService;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, TransactionService transactionService) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.transactionService = transactionService;
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
    public String showFilteredUsers(@ModelAttribute("filterUserOptions") FilterUserDto filterDto, Model model, HttpSession session,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    @RequestParam(defaultValue = "id") String sortBySpecific,
                                    @RequestParam(defaultValue = "ASC") String direction) {
        try {
            FilterUserOptions filterUserOptions = new FilterUserOptions(
                    filterDto.getUsername(),
                    filterDto.getEmail(),
                    filterDto.getPhoneNumber(),
                    null,
                    null
            );
            Page<User> users = userService.getAll(filterUserOptions, page, size, sortBySpecific, direction);
            User user = authenticationHelper.tryGetUser(session);

            model.addAttribute("filterUserOptions", filterDto);
            model.addAttribute("users", users.getContent());
            model.addAttribute("currentPage", users.getNumber());
            model.addAttribute("totalPages", users.getTotalPages());
            model.addAttribute("totalItems", users.getTotalElements());
            model.addAttribute("direction", direction);
            model.addAttribute("size", size);

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

    @GetMapping("/{id}/transactions")
    public String getUserTransactions(
            HttpSession session,
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) Boolean isIncoming,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) boolean isAscending,
            @RequestParam(required = false) String token,
            Model model) {

        User user;
        try{
            User adminUser = authenticationHelper.tryGetUser(session);
            user = userService.getById(adminUser, id);
        } catch (AuthenticationFailureException | UnauthorizedOperationException e) {
            return "AccessDenied";
        }

        Page<Transaction> paginatedTransactions;
        try {
            if (recipient != null && recipient.isEmpty()) {
                recipient = null;
            }
            if (sortBy == null) {
                sortBy = "amount";
            }

            List<Transaction> transactions = transactionService.filterTransactions(startDate, endDate, recipient,
                    isIncoming, user);

            paginatedTransactions = transactionService.sortTransactionsWithPagination(transactions, sortBy, isAscending, page, size);

            if(paginatedTransactions.getContent().isEmpty()){
                return "NoTransactions";
            }

        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("recipient", recipient);
        model.addAttribute("transactions", paginatedTransactions);
        model.addAttribute("isIncoming", isIncoming);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("isAscending", isAscending);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paginatedTransactions.getTotalPages());
        model.addAttribute("size", size);

        return "TransactionsView";
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
