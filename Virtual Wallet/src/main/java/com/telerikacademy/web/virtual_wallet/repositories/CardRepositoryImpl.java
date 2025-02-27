package com.telerikacademy.web.virtual_wallet.repositories;

import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CardRepositoryImpl {

    private final SessionFactory sessionFactory;

    @Autowired
    public CardRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void create(Card card) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(card);
            session.getTransaction().commit();
        }
    }
}
