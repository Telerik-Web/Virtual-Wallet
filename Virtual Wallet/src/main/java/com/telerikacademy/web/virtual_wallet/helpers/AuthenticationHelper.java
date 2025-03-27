package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.exceptions.AuthenticationFailureException;
import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.User;
import com.telerikacademy.web.virtual_wallet.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION = "Authorization";
    public static final String AUTHENTICATION_ERROR = "The requested resource requires authentication.";
    public static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
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

            if(!user.getPassword().equals(password)){
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
            if (!user.getPassword().equals(password)) {
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
