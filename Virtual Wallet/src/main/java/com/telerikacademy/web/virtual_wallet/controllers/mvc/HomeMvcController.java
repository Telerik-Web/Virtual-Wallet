package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.contracts.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(TransactionService transactionService,
                             UserService userService,
                             AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            double sumIncomingTransactions = transactionService.filterTransactions(null,
                    null, null, true, user).stream().mapToDouble(Transaction::getAmount).sum();
            double sumOutgoingTransactions = transactionService.filterTransactions(null,
                    null, null, false, user).stream().mapToDouble(Transaction::getAmount).sum();
            model.addAttribute("allTransactions", transactionService.getAllTransactionsForUser(user.getId())
                    .stream().limit(4));
            model.addAttribute("incomingTransactions", sumIncomingTransactions);
            model.addAttribute("outgoingTransactions", sumOutgoingTransactions);
            model.addAttribute("currentUser", user);
            if (populateIsAuthenticated(session)) {
                String currentUsername = (String) session.getAttribute("currentUser");
                model.addAttribute("currentUser", userService.getByUsername(currentUsername));
            }
            return "index";
        } catch (AuthenticationFailureException e) {
            return "redirect:/currency/convert";
        }
    }

    @GetMapping("/about")
    public String showAboutPage() {
        return "AboutUs";
    }

    @GetMapping("/terms")
    public String showTermsPage() {
        return "Terms";
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "ContactUs";
    }

    @GetMapping("/FAQ")
    public String showFAQPage() {
        return "FAQ";
    }

}
