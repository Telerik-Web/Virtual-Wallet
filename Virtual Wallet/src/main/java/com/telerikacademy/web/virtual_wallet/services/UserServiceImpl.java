package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.helpers.TokenGenerator;
import com.telerikacademy.web.virtual_wallet.helpers.UserSpecifications;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import com.telerikacademy.web.virtual_wallet.services.email_verification.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.telerikacademy.web.virtual_wallet.helpers.PermissionHelper.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public Page<User> getAll(FilterUserOptions filterOptions, int page, int size, String sortBy, String direction) {

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        // Build dynamic filters
        Specification<User> spec = UserSpecifications.withFilters(filterOptions);


        return userRepository.findAll(spec, pageable);
    }

    @Override
    public long getUserCount() {
        return userRepository.count();
    }


    @Override
    public User getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
    }

    @Override
    public User getById(User user, long id) {
        checkIfAdmin(user);
        return getById(id);
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }
        return user;
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new EntityNotFoundException("User", "phone number", phoneNumber);
        }
        return user;
    }

    @Override
    public void alterAdminPermissions(long id, User user, boolean isAdmin) {
        checkIfAdmin(user);

        User userToUpdate = getById(id);

        if (isAdmin) {
            userToUpdate.setIsAdmin(true);
        } else {
            userToUpdate.setIsAdmin(false);
        }
        userRepository.save(userToUpdate);
    }

    @Override
    public void alterBlockPermissions(long id, User user, boolean isBlocked) {
        checkIfAdmin(user);

        User userToUpdate = getById(id);

        if (isBlocked) {
            userToUpdate.setIsBlocked(true);
        }
        if (!isBlocked) {
            userToUpdate.setIsBlocked(false);
        }
        userRepository.save(userToUpdate);
    }

    @Override
    public void create(User user) {
        boolean exists = true;

        try {
            userRepository.findByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        String token = TokenGenerator.generateToken();
        user.setVerificationToken(token);
        user.setAccountVerified(false);
        userRepository.save(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
    }

    @Override
    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setAccountVerified(true);
            user.setVerificationToken(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public void update(User user, User userFromHeader, long id) {
        checkIfCreatorOrAdminForUser(userFromHeader, user);

        boolean exists = true;

        try {
            userRepository.findByEmail(user.getEmail());
            if (user.getId() == id) {
                exists = false;
            }
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.save(user);
    }

    @Override
    public void delete(long id, User userFromHeader) {
        User user = getById(id);
        checkIfCreatorOrAdminForUser(userFromHeader, user);
        userRepository.delete(user);
    }

}
