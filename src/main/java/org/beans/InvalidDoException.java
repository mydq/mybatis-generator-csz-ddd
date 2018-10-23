package org.beans;

/**
 * @Author: csz
 * @Date: 2018/10/12 14:30
 * @since 1.7
 */
public class InvalidDoException extends RuntimeException {
    private static final long serialVersionUID = -2562929980995878180L;

    public InvalidDoException() {
    }

    public InvalidDoException(String message) {
        super(message);
    }

    public InvalidDoException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDoException(Throwable cause) {
        super(cause);
    }

    protected InvalidDoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
