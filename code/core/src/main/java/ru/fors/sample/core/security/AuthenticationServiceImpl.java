package ru.fors.sample.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.fors.sample.core.UserService;
import ru.fors.sample.core.exception.BadUserNameOrPassword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 18:13
 */
@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(String username, String passwordHash)
            throws AuthenticationException {
        try {
            Long id = userService.getUserIdByLogin(username);
            if (id == null) {
                String errMsg = "Нет пользователя " + username;
                logger.info(errMsg);
                throw new BadUserNameOrPassword(errMsg);
            }

            if (!isCheckPassword(id, passwordHash)) {
                String errMsg = "Неверный пароль для пользователя " + username;
                logger.info(errMsg);
                throw new BadUserNameOrPassword(errMsg);
            }

            return new SampleAuthenticationToken(username, null,
                    Collections.singleton(new SimpleGrantedAuthority(USER_ROLE)), id);

        } catch (IncorrectResultSizeDataAccessException e) {
            String errMsg = "Нет пользователя " + username;
            logger.info(errMsg);
            throw new BadUserNameOrPassword(errMsg);
        } catch (Exception se) {
            logger.error(se.getMessage());
            throw new AuthenticationServiceException(String.format("Ошибка при попытке авторизации: %s", se.getMessage()));
        }
    }

    private boolean isCheckPassword(Long userId, String passwordHash) throws Exception {
        boolean checkedPassword = false;
        BufferedReader br = null;

        try {
            String currentLine;
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("fileWithIdPwd.properties")));

            while ((currentLine = br.readLine()) != null) {
                currentLine = currentLine.trim();

                Long id = Long.valueOf(currentLine.substring(0, currentLine.indexOf(" ")));
                if (ObjectUtils.nullSafeEquals(userId, id)) {
                    String passwordHashByFile = currentLine.substring(currentLine.lastIndexOf(" ") + 1);
                    if (ObjectUtils.nullSafeEquals(passwordHash, passwordHashByFile)) {
                        checkedPassword = true;
                        break;
                    }
                }
            }

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        return checkedPassword;
    }

}
