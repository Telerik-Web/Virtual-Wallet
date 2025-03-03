//package com.telerikacademy.web.virtual_wallet.controllers.mvc;
//
//import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
//import com.telerikacademy.web.virtual_wallet.models.Transaction;
//import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
//import com.telerikacademy.web.virtual_wallet.models.User;
//import com.telerikacademy.web.virtual_wallet.services.CardService;
//import com.telerikacademy.web.virtual_wallet.services.TransactionService;
//import com.telerikacademy.web.virtual_wallet.services.UserService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.http.HttpHeaders;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Controller
//@RequestMapping("/transactions")
//public class TransactionMvcController {
//
//    private final TransactionService transactionService;
//    private final UserService userService;
//    private final AuthenticationHelper authenticationHelper;
//    private final CardService cardService;
//
//    public TransactionMvcController(TransactionService transactionService,
//                                    UserService userService,
//                                    AuthenticationHelper authenticationHelper,
//                                    CardService cardService) {
//        this.transactionService = transactionService;
//        this.userService = userService;
//        this.authenticationHelper = authenticationHelper;
//        this.cardService = cardService;
//    }
//
//    @GetMapping("/all")
//    public String getFilteredTransactions(HttpSession session, Model model) {
//        User user = authenticationHelper.tryGetUser(session);
//        model.addAttribute("transactions", transactionService.getAllTransactionsForUser(user.getId()));
//        return "TransactionsView";
//    }
//}
