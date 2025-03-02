package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CardRepository extends JpaRepository<Card, Long> {

    Card findById(long id);

    Set<Card> findByUser(User user);
}
