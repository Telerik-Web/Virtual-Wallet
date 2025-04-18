package com.telerikacademy.web.virtual_wallet.controllers.rest;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.helpers.AuthenticationHelper;
import com.telerikacademy.web.virtual_wallet.models.*;
import com.telerikacademy.web.virtual_wallet.models.dtos.TransactionDTO;
import com.telerikacademy.web.virtual_wallet.services.contracts.TransactionService;
import com.telerikacademy.web.virtual_wallet.services.contracts.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

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

    @Operation(summary = "Send money by phone", description = "Transfers funds to a recipient using their phone number.")
    @PostMapping("/{phone}")
    @SecurityRequirement(name = "authHeader")
    public Transaction sendByPhone(@RequestHeader HttpHeaders headers,
                                   @PathVariable String phone,
                                   @RequestBody Transaction transaction) {
        User recipient = userService.getByPhoneNumber(phone);
        User sender = authenticationHelper.tryGetUser(headers);
        return transactionService.transferFunds(sender, recipient, transaction.getAmount());
    }

    @Operation(summary = "Send money", description = "Transfers funds to a recipient using phone, email, or username.")
    @PostMapping("/send")
    @SecurityRequirement(name = "authHeader")
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type parameter");
        }
        return transactionService.transferFunds(sender, recipient, transaction.getAmount());
    }

    @Operation(summary = "Filter transactions", description = "Filters transactions based on date, recipient, and other criteria.")
    @GetMapping("/filter")
    @SecurityRequirement(name = "authHeader")
    public List<TransactionDTO> getFilteredTransactions(
            @RequestHeader HttpHeaders headers,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String recipient,
            @RequestParam(required = false) Boolean isIncoming,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        LocalDateTime start = (startDate != null) ? OffsetDateTime.parse(startDate).toLocalDateTime() : null;
        LocalDateTime end = (endDate != null) ? OffsetDateTime.parse(endDate).toLocalDateTime() : null;

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

    @Operation(summary = "Deposit money", description = "Deposits money to a user’s balance from a card.")
    @PostMapping("/deposit")
    @SecurityRequirement(name = "authHeader")
    public ResponseEntity<String> withdrawMoney(@RequestBody CreateTransactionRequest transaction,
                                                @RequestHeader HttpHeaders headers) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(headers);
        } catch (AuthenticationFailureException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Random random = new Random();
        boolean isSuccess = random.nextBoolean();
        int count = 0;
        for (Card card : user.getCards()) {
            if (card.getCardNumber().equals(transaction.getCardNumber())) {
                count++;
            }
        }
        if (user.getCards().isEmpty() || count == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed, invalid card number.");
        }
        if (isSuccess) {
            user.setBalance(user.getBalance() + transaction.getAmount());
            userService.update(user, user, user.getId());
            return ResponseEntity.ok("Transaction successful: " +
                    transaction.getAmount() + " withdrawn from card.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed, please try again.");
        }
    }
}
