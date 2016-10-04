package ru.fors.sample.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Author: Lebedev Aleksandr
 * Date: 24.02.16
 */
public interface AuthenticationService {
    String USER_ROLE = "USER_ROLE";

    /**
     * Аутентификация
     *
     * @param username     пользователь
     * @param passwordHash Пароль пользователя (хэш)
     * @return успех аутентификации
     */
    Authentication authenticate(String username, String passwordHash)
            throws AuthenticationException;


}
