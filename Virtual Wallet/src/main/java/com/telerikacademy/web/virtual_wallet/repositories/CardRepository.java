package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAll();

    Card findById(long id);

    List<Card> findByUser(User user);

    @Query("SELECT COUNT(c) > 0 FROM Card c WHERE c.user = :user AND c.cardNumber = :cardNumber")
    boolean existsByUserAndNumber(@Param("user") User user, @Param("cardNumber") String cardNumber);
}
