package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.User;

public class PermissionHelper {

    private static final String AUTHORIZATION_PERMISSION_ERROR = "You don't have the permission to do this.";
    private static final String BLOCKED_USER_ERROR = "You are blocked and cannot perform that operation";

    public static void checkIfAdmin(User user) {
        if (!user.getIsAdmin()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static void checkIfCreatorOrAdminForUser(User user, User userToUpdate) {
        if (!(user.getId() == userToUpdate.getId() || user.getIsAdmin())) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static void checkIfBlocked(User user) {
        if (user.getIsBlocked()) {
            throw new UnauthorizedOperationException(BLOCKED_USER_ERROR);
        }
    }
}
