package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    Card findById(long id);

    Set<Card> findByUser(User user);
}
