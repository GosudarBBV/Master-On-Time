package com.master.on.time.master.on.time.exception;

public class UserRoleNotAllowedException extends RuntimeException {
    public UserRoleNotAllowedException() {
        super("User role not allowed to perform this action");
    }

    public UserRoleNotAllowedException(String message) {
        super(message);
    }
}
