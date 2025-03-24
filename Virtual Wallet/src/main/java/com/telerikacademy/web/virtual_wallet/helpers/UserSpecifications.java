package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

public class UserSpecifications {

    public static Specification<User> withFilters(FilterUserOptions filterOptions) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterOptions.getUsername().isPresent()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + filterOptions.getUsername().get() + "%"));
            }

            if (filterOptions.getEmail().isPresent()) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + filterOptions.getEmail().get() + "%"));
            }

            if (filterOptions.getPhoneNumber().isPresent()) {
                predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + filterOptions.getPhoneNumber().get() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
