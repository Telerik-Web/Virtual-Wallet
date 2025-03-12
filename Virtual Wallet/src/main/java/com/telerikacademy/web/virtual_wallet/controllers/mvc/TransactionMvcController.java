package com.telerikacademy.web.virtual_wallet.controllers.mvc;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.helpers.JwtUtil;
import com.telerikacademy.web.virtual_wallet.helpers.TokenGenerator;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.services.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import com.telerikacademy.web.virtual_wallet.services.email_verification.LargeTransactionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Controller
@RequestMapping("/transactions")
public class TransactionMvcController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final LargeTransactionService largeTransactionService;
    private final JwtUtil jwtUtil;

    public TransactionMvcController(TransactionService transactionService,
                                    UserService userService,
                                    AuthenticationHelper authenticationHelper, LargeTransactionService largeTransactionService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.largeTransactionService = largeTransactionService;
        this.jwtUtil = jwtUtil;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

//    @GetMapping
//    public String showPaginatedTransactions(Model model,
//                                            @RequestParam(defaultValue = "0") int page,
//                                            @RequestParam(defaultValue = "5") int size) {
//        Page<Transaction> productPage = transactionService.getPaginatedTransactions(page, size);
//
//        model.addAttribute("products", productPage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", productPage.getTotalPages());
//
//
//    }

    @GetMapping("/all")
    public String showAllTransactions(
            HttpSession session,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) Boolean isIncoming,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) boolean isAscending,
            Model model) {
        Page<Transaction> paginatedTransactions;
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (recipient != null && recipient.isEmpty()) {
                recipient = null;
            }
            if (sortBy == null) {
                sortBy = "amount";
            }

            List<Transaction> transactions = transactionService.filterTransactions(startDate, endDate, recipient,
                    isIncoming, user);

            paginatedTransactions = transactionService.sortTransactionsWithPagination(transactions, sortBy, isAscending, page, size);
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

    @GetMapping("/new")
    public String showTransactionForm(Model model, HttpSession session) {
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
        model.addAttribute("transaction", new TransactionDTOCreate());
        return "CreateTransaction";
    }

    @PostMapping("/new")
    public String createTransaction(@Valid @ModelAttribute("transaction") TransactionDTOCreate transactionDTOCreate,
                                    BindingResult errors,
                                    HttpSession session,
                                    Model model) {
        try {
            if (errors.hasErrors()) {
                return "CreateTransaction";
            }

            String type = transactionDTOCreate.getType();
            String value = transactionDTOCreate.getValue();

            model.addAttribute("type", transactionDTOCreate.getType());
            model.addAttribute("value", transactionDTOCreate.getValue());
            User recipient;
            switch (type) {
                case "phone number":
                    try {
                        recipient = userService.getByPhoneNumber(value);
                    } catch (EntityNotFoundException e) {
                        errors.rejectValue("value", "error.recipient");
                        return "CreateTransaction";
                    }
                    break;
                case "email":
                    try {
                        recipient = userService.getByEmail(value);
                    } catch (EntityNotFoundException e) {
                        errors.rejectValue("value", "error.recipient");
                        return "CreateTransaction";
                    }
                    break;
                case "username":
                    try {
                        recipient = userService.getByUsername(value);
                    } catch (EntityNotFoundException e) {
                        errors.rejectValue("value", "error.recipient");
                        return "CreateTransaction";
                    }
                    break;
                default:
                    recipient = null;
                    break;
            }

            User user;
            try {
                user = authenticationHelper.tryGetUser(session);
            } catch (AuthenticationFailureException e) {
                return "AccessDenied";
            } catch (EntityNotFoundException e) {
                return "CreateTransaction";
            }

            if (user.getBalance() < Double.parseDouble(transactionDTOCreate.getAmount())) {
                errors.rejectValue("amount", "error.amount.over");
                return "CreateTransaction";
            }

            session.setAttribute("transaction", transactionDTOCreate);
            session.setAttribute("amount", transactionDTOCreate.getAmount());
            session.setAttribute("recipient", recipient);
            return "redirect:/transactions/new/confirm";
        } catch (EntityNotFoundException e) {
            errors.rejectValue("amount", "error.amount");
            return "CreateTransaction";
        }

    }

    @GetMapping("/new/confirm")
    public String confirmTransaction(Model model,
                                     HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
        double amount = Double.parseDouble(session.getAttribute("amount").toString());
        User recipient = (User) session.getAttribute("recipient");
        TransactionDTOCreate transaction = (TransactionDTOCreate) session.getAttribute("transaction");
        model.addAttribute("amount", amount);
        model.addAttribute("recipient", recipient);
        model.addAttribute("transaction", transaction);
        return "TransactionConfirm";
    }

    @PostMapping("/new/confirm")
    public String completeTransaction(HttpSession session) {
        try {
            User sender = authenticationHelper.tryGetUser(session);
            User recipient = (User) session.getAttribute("recipient");
            double amount = Double.parseDouble(session.getAttribute("amount").toString());

//            if (amount >= 10000) {
//                String token = TokenGenerator.generateToken();
//                sender.setVerificationToken(token);
//                userService.update(sender, sender, sender.getId());
//                largeTransactionService.sendVerificationEmail(sender.getEmail(), token);
//
//                largeTransactionService.sendVerificationEmail(sender.getEmail(), token);
//                session.setAttribute("pendingTransaction", new PendingTransaction(sender, recipient, amount));
//                return "ConfirmTransaction";
//            }

            if (amount >= 10000) {
                String token = jwtUtil.generateToken(sender, recipient.getId(), amount);
                sender.setVerificationToken(token);
                userService.update(sender, sender, sender.getId());
                largeTransactionService.sendVerificationEmail(sender.getEmail(), token);
                return "ConfirmTransaction";
            }

            transactionService.transferFunds(sender, recipient, amount);
            return "redirect:/transactions/all";
        } catch (Exception e) {
            return "redirect:/transactions/new";
        }
    }

    @GetMapping("/verify/transaction")
    public String confirmTransaction(@RequestParam String token, HttpSession session) {
//        try {
//            if (TokenGenerator.isTokenExpired(token)) {
//                return "AccessDenied";
//            }

        User sender = authenticationHelper.tryGetUser(session);

        String userToken = sender.getVerificationToken();
        if(!userToken.equals(token)) {
            return "TokenFail";
        }

        Long recipientId = jwtUtil.getRecipientIdFromToken(token);
        User recipient = userService.getById(recipientId);
        Double amount = jwtUtil.getAmountFromToken(token);

        transactionService.transferFunds(sender, recipient, amount);

//            User sender = authenticationHelper.tryGetUser(session);
//
//            if (!sender.getVerificationToken().equals(token)) {
//                return "TokenFail";
//            }
//
//            PendingTransaction pendingTransaction = (PendingTransaction) session.getAttribute("pendingTransaction");
//            if (pendingTransaction == null) {
//                return "TransactionNotFound";
//            }
//
//            transactionService.transferFunds(pendingTransaction.getSender(),
//                    pendingTransaction.getRecipient(),
//                    pendingTransaction.getAmount());

        sender.setVerificationToken(null);
        userService.update(sender, sender, sender.getId());
        session.removeAttribute("pendingTransaction");

        return "redirect:/transactions/all";
//        } catch (Exception e) {
//            return "redirect:/transactions/new";
//        }
    }

    @GetMapping("/deposit")
    public String showDepositForm(Model model,
                                  HttpSession session) {
        model.addAttribute("deposit", new CreateTransactionRequest());
        try {
            authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        }
        return "NewDeposit";
    }

    @PostMapping("/deposit")
    public String createDeposit(@Valid @ModelAttribute("deposit") CreateTransactionRequest createDeposit,
                                BindingResult errors,
                                HttpSession session) {
        if (errors.hasErrors()) {
            return "NewDeposit";
        }

        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailureException e) {
            return "AccessDenied";
        } catch (EntityNotFoundException e) {
            return "NewDeposit";
        }

        int count = 0;
        for (Card card : user.getCards()) {
            if (card.getCardNumber().equals(createDeposit.getCardNumber())) {
                count++;
            }
        }
        if (user.getCards().isEmpty() || count == 0) {
            return "NoCards";
        }
        Random random = new Random();
        boolean isSuccess = random.nextBoolean();
        if (!isSuccess) {
            return "FailedDeposit";
        }
        user.setBalance(user.getBalance() + createDeposit.getAmount());
        userService.update(user, user, user.getId());
        return "redirect:/";
    }

//    @GetMapping("/deposits/all")
//    public String showAllDeposits(
//            HttpSession session,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
//            @RequestParam(required = false) String recipient,
//            @RequestParam(required = false) Boolean isIncoming,
//            @RequestParam(required = false) String sortBy,
//            @RequestParam(required = false) boolean isAscending,
//            Model model) {
//        User user = authenticationHelper.tryGetUser(session);
//        if (recipient != null && recipient.isEmpty()) {
//            recipient = null;
//        }
//        if (sortBy == null) {
//            sortBy = "amount";
//        }
//        List<Transaction> transactions = transactionService.filterTransactions(startDate, endDate, recipient,
//                isIncoming, user);
//
//        List<Transaction> transactionDTOList2 = transactionService.sortTransactions2(transactions, sortBy, isAscending);
//
//        model.addAttribute("startDate", startDate);
//        model.addAttribute("endDate", endDate);
//        model.addAttribute("recipient", recipient);
//        model.addAttribute("transactions", transactionDTOList2);
//        model.addAttribute("isIncoming", isIncoming);
//        model.addAttribute("sortBy", sortBy);
//        model.addAttribute("isAscending", isAscending);
//
//        return "redirect:/";
//    }
}
