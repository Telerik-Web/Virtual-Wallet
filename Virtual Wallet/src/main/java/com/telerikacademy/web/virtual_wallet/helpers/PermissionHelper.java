package com.telerikacademy.web.virtual_wallet.helpers;

import com.telerikacademy.web.virtual_wallet.exceptions.UnauthorizedOperationException;
import com.telerikacademy.web.virtual_wallet.models.Card;
import com.telerikacademy.web.virtual_wallet.models.User;
import org.springframework.web.multipart.MultipartFile;

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

    public static void checkIfCreatorOrAdminForCard(User user, Card card) {
        if (!(user.getId() == card.getUser().getId() || user.getIsAdmin())) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/jpeg")
                || contentType.equals("image/png") || contentType.equals("image/gif"));
    }
}
