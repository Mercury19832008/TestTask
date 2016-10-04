package ru.fors.sample.core.exception;

/**
 * Author: Alexey Perminov
 * Date: 25.07.2010
 * Time: 11:44:28
 */
public class ObjectNotFoundException extends CoreException {
    public ObjectNotFoundException() {
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(Throwable cause) {
        super(cause);
    }
}
