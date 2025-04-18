package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.contracts.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION = "Authorization";
    public static final String AUTHENTICATION_ERROR = "The requested resource requires authentication.";
    public static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationHelper(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");

        if (currentUser == null) {
            throw new AuthenticationFailureException(AUTHENTICATION_ERROR);
        }

        return userService.getByUsername(currentUser);
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    AUTHENTICATION_ERROR);
        }

        String userInfo = headers.getFirst(AUTHORIZATION);
        String username = getUsername(userInfo);
        String password = getPassword(userInfo);

        try {

            User user = userService.getByUsername(username);

            if(!passwordEncoder.matches(password, user.getPassword())) {
                throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
            }

            return user;

        } catch (EntityNotFoundException e) {
            throw new UnauthorizedOperationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new AuthenticationFailureException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailureException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");

        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpace);

    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");

        if (firstSpace == -1) {
            throw new UnauthorizedOperationException(AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace + 1);

    }
}
