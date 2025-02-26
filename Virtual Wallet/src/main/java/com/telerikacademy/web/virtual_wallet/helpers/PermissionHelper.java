package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.User;

public class PermissionHelper {

    private static final String AUTHORIZATION_PERMISSION_ERROR = "You don't have the permission to do this.";

    public static void checkIfAdmin(User user) {
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static void checkIfCreatorOrAdminForUser(User user, User userToUpdate) {
        if (!(user.getId() == userToUpdate.getId() || user.isAdmin())) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }
}
