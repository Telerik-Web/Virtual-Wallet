package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "APIs for managing transactions")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionRestController(TransactionService transactionService, UserService userService, UserMapper userMapper, AuthenticationHelper authenticationHelper) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping("/{phone}")
    public Transaction sendByPhone(@RequestHeader HttpHeaders headers,
                                   @PathVariable String phone,
                                   @RequestBody Transaction transaction) {
        User recipient = userService.getByPhoneNumber(phone);
        User sender = authenticationHelper.tryGetUser(headers);
        return transactionService.transferFunds(sender, recipient, transaction.getAmount());
    }

    @PostMapping("/send")
    public Transaction sendByPhone(@RequestHeader HttpHeaders headers,
                                   @RequestParam String type,
                                   @RequestParam String value,
                                   @RequestBody Transaction transaction) {
        User recipient;
        User sender;
        switch (type) {
            case "phone":
                recipient = userService.getByPhoneNumber(value);
                sender = authenticationHelper.tryGetUser(headers);
                break;
            case "email":
                recipient = userService.getByEmail(value);
                sender = authenticationHelper.tryGetUser(headers);
                break;
            case "username":
                recipient = userService.getByUsername(value);
                sender = authenticationHelper.tryGetUser(headers);
                break;
            default:
                recipient = null;
                sender = null;
                break;
        }

        return transactionService.transferFunds(sender, recipient, transaction.getAmount());
    }
}
