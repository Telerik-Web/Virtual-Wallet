package com.telerikacademy.web.virtual_wallet;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.Transaction;
import com.telerikacademy.web.virtual_wallet.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Helpers {

    public static User createMockAdminUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("1234567890");
        user.setIsAdmin(true);
        user.setIsBlocked(false);
        user.setBalance(100.0);
        return user;
    }

    public static User createMockBlockedUser() {
        User user = new User();
        user.setId(2L);
        user.setUsername("jane_smith");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setPhoneNumber("0987654321");
        user.setIsAdmin(false);
        user.setIsBlocked(true);
        user.setBalance(50.0);
        return user;
    }

    public static User createMockUser() {
        User user = new User();
        user.setId(3L);
        user.setUsername("mike_smith");
        user.setFirstName("Mike");
        user.setLastName("Smith");
        user.setEmail("mike.smith@example.com");
        user.setPhoneNumber("0927654321");
        user.setIsAdmin(false);
        user.setIsBlocked(false);
        user.setBalance(50.0);
        return user;
    }

    public static Transaction createMockTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(100.0);
        transaction.setSender(createMockAdminUser());
        transaction.setRecipient(createMockBlockedUser());
        transaction.setCreatedAt(LocalDateTime.of(2025, 3, 9, 17, 26));
        return transaction;
    }

    public static Transaction createMockTransaction2() {
        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setAmount(200.0);
        transaction.setSender(createMockAdminUser());
        transaction.setRecipient(createMockBlockedUser());
        transaction.setCreatedAt(LocalDateTime.of(2025, 4, 9, 17, 26));
        return transaction;
    }

    public static Card createMockCard() {
        Card card = new Card();
        card.setId(1L);
        card.setCardNumber("2211222233334444");
        card.setExpirationDate(LocalDate.ofEpochDay(2030- 3 -11));
        card.setUser(createMockAdminUser());
        card.setCheckNumber("111");
        return card;
    }

    public static List<Card> createMockCardsList() {
        List<Card> cardsList = new ArrayList<>();
        cardsList.add(createMockCard());
        return cardsList;
    }
}
