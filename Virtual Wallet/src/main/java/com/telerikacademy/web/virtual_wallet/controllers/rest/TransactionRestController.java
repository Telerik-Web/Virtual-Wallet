package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.mappers.UserMapper;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "APIs for managing transactions")
public class TransactionRestController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public TransactionRestController(TransactionService transactionService,
                                     UserService userService,
                                     AuthenticationHelper authenticationHelper) {
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
    public Transaction sendMoney(@RequestHeader HttpHeaders headers,
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

    @GetMapping("/filter")
    public List<TransactionDTO> getFilteredTransactions(
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) Boolean isIncoming,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate) : null;
        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate) : null;

        User user = authenticationHelper.tryGetUser(headers);

        List<Transaction> filteredTransactions =
                transactionService.filterTransactions(start, end, recipient, isIncoming, user);
        List<TransactionDTO> TransactionDTOList = filteredTransactions.stream().map(transaction -> new TransactionDTO(
                transaction.getSender().getId() != user.getId(),
                transaction.getSender().getId(),
                transaction.getRecipient().getId(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        )).toList();

        return transactionService.sortTransactions(TransactionDTOList, sortBy, ascending);
    }
//    public List<TransactionDTO> getUserTransactions(@RequestHeader HttpHeaders headers) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(headers);
//        } catch (EntityNotFoundException e) {
//            user = null;
//        }
//        List<Transaction> transactions = transactionService.getAllTransactionsForUser(user.getId());
//
//        return transactions.stream().map(transaction -> new TransactionDTO(
//                transaction.getSender().getId(),
//                transaction.getRecipient().getId(),
//                transaction.getAmount(),
//                transaction.getStatus(),
//                transaction.getCreatedAt()
//        )).collect(Collectors.toList());
//    }
}
