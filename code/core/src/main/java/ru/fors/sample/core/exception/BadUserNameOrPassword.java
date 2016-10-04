package ru.fors.sample.core.exception;

import org.springframework.security.authentication.BadCredentialsException;

/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 21:55
 */
public class BadUserNameOrPassword extends BadCredentialsException {

    public BadUserNameOrPassword(String message){
        super(message);
    }
}
