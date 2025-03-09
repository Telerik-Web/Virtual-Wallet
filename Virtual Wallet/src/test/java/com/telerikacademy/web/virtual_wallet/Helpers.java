package com.telerikacademy.web.virtual_wallet;

import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;

import java.time.LocalDateTime;

public class Helpers {

    public static User createMockUser() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("john_doe");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPhoneNumber("1234567890");
        user1.setIsAdmin(true);
        user1.setIsBlocked(false);
        user1.setBalance(100.0);
        return user1;
    }

    public static User createMockUser2() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("jane_smith");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setPhoneNumber("0987654321");
        user2.setIsAdmin(false);
        user2.setIsBlocked(true);
        user2.setBalance(50.0);
        return user2;
    }

    public static Transaction createMockTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setSender(createMockUser());
        transaction.setRecipient(createMockUser2());
        transaction.setCreatedAt(LocalDateTime.of(2025, 3, 9, 17, 26));
        return transaction;
    }

    public static Transaction createMockTransaction2() {
        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setAmount(200.0);
        transaction.setSender(createMockUser());
        transaction.setRecipient(createMockUser2());
        transaction.setCreatedAt(LocalDateTime.of(2025, 4, 9, 17, 26));
        return transaction;
    }
}
