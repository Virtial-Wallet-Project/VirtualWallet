package com.example.virtualwallet.helpers;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.exceptions.UnauthorizedOperationException;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;

public class PermissionHelpers {

    private static final String AUTHORIZATION_PERMISSION_ERROR = "Invalid operation. Only an Admin or Creator can modify this entity!";
    private static final String ADMIN_AUTHORIZATION_ERROR = "Invalid operation. You are not an admin!";
    private static final String CREATOR_AUTHORIZATION_ERROR = "Invalid operation. You are not the owner of this card!";
    private static final String BLOCKED_USER_ERROR = "Invalid operation. Your profile has been blocked!";
    private static final String UNVERIFIED_USER_ERROR = "Invalid operation. Your profile is not verified!";

    public static void checkIfAdmin(User user) {
        if (!user.isAdmin()) {
            throw new UnauthorizedOperationException(ADMIN_AUTHORIZATION_ERROR);
        }
    }

    public static void checkIfBlocked(User user) {
        if (user.isBlocked()) {
            throw new UnauthorizedOperationException(BLOCKED_USER_ERROR);
        }
    }

    public static void checkIfCreatorOrAdmin(int entityOwnerId, User modifier) {
        if (entityOwnerId != modifier.getUserId() && !modifier.isAdmin()) {
            throw new UnauthorizedOperationException(AUTHORIZATION_PERMISSION_ERROR);
        }
    }

    public static void checkIfCreator (CreditCard card, User user) {
        if (user.getUserId() != card.getCreatedBy().getUserId()) {
            throw new UnauthorizedOperationException(CREATOR_AUTHORIZATION_ERROR);
        }
    }

    public static void checkIfVerified (User user) {
        if (!user.isAccountVerified()) {
            throw new UnauthorizedOperationException(UNVERIFIED_USER_ERROR);
        }
    }
}
