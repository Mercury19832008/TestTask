package ru.fors.sample.core.exception;

/**
 * Author: Alexey Perminov
 * Date: 26.06.2010
 * Time: 14:06:35
 */
public class CoreException extends RuntimeException {

    public CoreException() {
    }

    public CoreException(String message) {
        super(message);
    }

    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CoreException(Throwable cause) {
        super(cause);
    }
}
