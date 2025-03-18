package com.telerikacademy.web.virtual_wallet.services;

import com.telerikacademy.web.virtual_wallet.exceptions.DuplicateEntityException;
import com.telerikacademy.web.virtual_wallet.exceptions.EntityNotFoundException;
import com.telerikacademy.web.virtual_wallet.helpers.TokenGenerator;
import com.telerikacademy.web.virtual_wallet.models.FilterUserOptions;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.repositories.UserRepository;
import com.telerikacademy.web.virtual_wallet.services.email_verification.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<User> getAll(FilterUserOptions filterOptions) {
        return userRepository.getAll(filterOptions);
    }

    @Override
    public long getUserCount() {
        return userRepository.getUserCount();
    }

    @Override
    public User getById(User user, long id) {
        checkIfAdmin(user);
        return userRepository.getById(id);
    }

    @Override
    public User getById(long id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
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
        userRepository.alterAdminPermissions(userToUpdate);
    }

    @Override
    public void alterBlockPermissions(long id, User user, boolean isBlocked) {
        checkIfAdmin(user);

        User userToUpdate = userRepository.getById(id);

        if (isBlocked) {
            userToUpdate.setIsBlocked(true);
        }
        if (!isBlocked) {
            userToUpdate.setIsBlocked(false);
        }
        userRepository.alterBlockPermissions(userToUpdate);
    }

    @Override
    public void create(User user) {
        boolean exists = true;

        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }
        String token = TokenGenerator.generateToken();
        user.setVerificationToken(token);
        user.setAccountVerified(false);
        userRepository.create(user);
        emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
    }

    @Override
    public boolean verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setAccountVerified(true);
            user.setVerificationToken(null);
            userRepository.update(user, user.getId());
            return true;
        }
        return false;
    }

    @Override
    public void update(User user, User userFromHeader, long id) {
        checkIfCreatorOrAdminForUser(userFromHeader, user);

        boolean exists = true;

        try {
            userRepository.getByEmail(user.getEmail());
            if (user.getId() == id) {
                exists = false;
            }
        } catch (EntityNotFoundException e) {
            exists = false;
        }

        if (exists) {
            throw new DuplicateEntityException("User", "email", user.getEmail());
        }

        userRepository.update(user, id);
    }

    @Override
    public void delete(long id, User userFromHeader) {
        User user = getById(id);
        checkIfCreatorOrAdminForUser(userFromHeader, user);
        userRepository.delete(id);
    }

}
