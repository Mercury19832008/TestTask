package ru.fors.sample.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import ru.fors.sample.core.exception.BadUserNameOrPassword;
import ru.fors.sample.core.security.AuthenticationService;
import ru.fors.sample.core.utils.Md5Utils;

import javax.annotation.Resource;

/**
 * Author: Lebedev Aleksandr
 * Date: 24.02.16
 */
public class DBAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(DBAuthenticationProvider.class);

    @Resource
    private AuthenticationService authenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            String errMsg = "Не заданы имя пользователя или пароль.";
            logger.error(errMsg);
            throw new BadUserNameOrPassword(errMsg);
        }
        String passwordHash = Md5Utils.encryptMD5(password);
        Authentication res;
        try {
            res = authenticationService.authenticate(username, passwordHash);
        } catch (BadUserNameOrPassword se) {
            logger.error(se.getMessage());
            throw se;
        }

        logger.info("Успешно залогинились!");
        return res;
    }

    @Override
    public boolean supports(Class authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }



}
