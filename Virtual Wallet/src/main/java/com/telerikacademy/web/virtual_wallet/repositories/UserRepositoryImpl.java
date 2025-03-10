package com.telerikacademy.web.virtual_wallet.repositories;


import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll(FilterUserOptions filterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder sb = new StringBuilder("FROM User");
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getUsername().ifPresent(value -> {
                filters.add("username like :username");
                params.put("username", String.format("%%%s%%", value));
            });

            filterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email");
                params.put("email", String.format("%%%s%%", value));
            });

            filterOptions.getPhoneNumber().ifPresent(value -> {
                filters.add("phoneNumber like :phoneNumber");
                params.put("phoneNumber", String.format("%%%s%%", value));
            });

            if (!filters.isEmpty()) {
                sb.append(" WHERE ").append(String.join(" AND ", filters));
            } else {
                sb.append(" order by id asc");
            }

            sb.append(createOrderBy(filterOptions));
            Query<User> query = session.createQuery(sb.toString(), User.class);
            query.setProperties(params);
            return query.list();
        }
    }

    @Override
    public long getUserCount() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.stream().count();
        }
    }

    @Override
    public User getById(long id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> user = session.createQuery("from User where username = :username", User.class);
            user.setParameter("username", username);
            return user
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "email", username));
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> user = session.createQuery("From User where email = :email", User.class);
            user.setParameter("email", email);
            return user
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
        }
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> user = session.createQuery("From User where phoneNumber = :phoneNumber", User.class);
            user.setParameter("phoneNumber", phoneNumber);
            return user
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("User", "phoneNumber", phoneNumber));
        }
    }

    @Override
    public void alterAdminPermissions(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void alterBlock(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user, long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Transactional
    @Override
    public void delete(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            User user = getById(id);
            if (user.getCards() != null) {
                Iterator<Card> cards;
                Hibernate.initialize(cards = user.getCards().iterator());
                while (cards.hasNext()) {
                    cards.next();
                    cards.remove();
                }
            }

            session.remove(getById(id));
            session.getTransaction().commit();
        }
    }

    private String createOrderBy(FilterUserOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }

        String orderBy = switch (filterOptions.getSortBy().get()) {
            case "username" -> "username";
            case "email" -> "email";
            case "phoneNumber" -> "PhoneNumber";
            default -> "";
        };

        orderBy = String.format(" order by %s", orderBy);
        if (filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }

    @Override
    public User findByVerificationToken(String token) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.verificationToken = :token", User.class);
        query.setParameter("token", token);

        return query.getResultStream().findFirst().orElse(null);
    }
}
