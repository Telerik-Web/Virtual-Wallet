package com.telerikacademy.web.virtual_wallet.repositories;


import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

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
    public User getById(int id) {
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
    public void update(User user, int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
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
        if(filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")){
            orderBy = String.format("%s desc", orderBy);
        }

        return orderBy;
    }
}
