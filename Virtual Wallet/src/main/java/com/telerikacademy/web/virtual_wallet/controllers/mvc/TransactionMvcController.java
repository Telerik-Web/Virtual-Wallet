package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.TransactionFilter;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.CardService;
import com.telerikacademy.web.virtual_wallet.services.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionMvcController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final CardService cardService;

    public TransactionMvcController(TransactionService transactionService,
                                    UserService userService,
                                    AuthenticationHelper authenticationHelper,
                                    CardService cardService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.cardService = cardService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

//    @GetMapping("/all")
//    public String getFilteredTransactions(HttpSession session, Model model) {
//        User user = authenticationHelper.tryGetUser(session);
//        model.addAttribute("transactions", transactionService.getAllTransactionsForUser(user.getId()));
//        return "TransactionsView";
//    }

    @GetMapping("/all")
    public String showAllTransactions(
            HttpSession session,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) Boolean isIncoming,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) boolean isAscending,
            Model model) {
        User user = authenticationHelper.tryGetUser(session);
        if (recipient != null && recipient.isEmpty()) {
            recipient = null;
        }
        if(sortBy == null) {
            sortBy = "amount";
        }
        List<Transaction> transactions = transactionService.filterTransactions(startDate, endDate, recipient, isIncoming, user);

        List<Transaction> transactionDTOList2  = transactionService.sortTransactions2(transactions, sortBy, isAscending);
//
//        System.out.println("Start Date: " + startDate);
//        System.out.println("End Date: " + endDate);
//        System.out.println("Recipient: " + recipient);
//        System.out.println("Is Incoming: " + isIncoming);
//        System.out.println();
//        System.out.println("Filtered Transactions Count: " + transactions.size());

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("recipient", recipient);
        model.addAttribute("transactions", transactionDTOList2);
        model.addAttribute("isIncoming", isIncoming);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("isAscending", isAscending);

        return "TransactionsView";
    }

//    @GetMapping("/all")
//    public String getFilteredTransactions(
//            @RequestHeader HttpSession session,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
//            @RequestParam(required = false) String recipient,
//            @RequestParam(required = false) Boolean isIncoming,
//            Model model) {
//
//        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate) : null;
//        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate) : null;
//
//        User user = authenticationHelper.tryGetUser(session);
//
//        List<Transaction> filteredTransactions =
//                transactionService.filterTransactions(startDate, endDate, recipient, isIncoming, user);
//        List<TransactionDTO> TransactionDTOList = filteredTransactions.stream().map(transaction -> new TransactionDTO(
//                transaction.getSender().getId() != user.getId(),
//                transaction.getSender().getId(),
//                transaction.getRecipient().getId(),
//                transaction.getAmount(),
//                transaction.getStatus(),
//                transaction.getCreatedAt()
//        )).toList();
//        model.addAttribute("transactions", filteredTransactions);
//
//        return "TransactionsView";
//    }
//
//    @GetMapping("/sort")
//    public String sorttransactions(@RequestParam List<TransactionDTO> transactions,
//                                   @RequestParam String sortBy,
//                                   @RequestParam boolean ascending,
//                                   Model model) {
//        List<TransactionDTO> sortedtransactions = transactionService.sortTransactions(transactions, sortBy, ascending);
//        model.addAttribute("transactions", sortedtransactions);
//        return "TransactionsView";
//    }
}
