package ru.fors.sample.core;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.fors.sample.core.dto.User;
import ru.fors.sample.core.sc.UserSC;

import java.util.List;

/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 18:08
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserById(long id) {
        UserSC sc = new UserSC();
        sc.setId(id);
        return baseDao.find(sc).get(0);
    }

    @Override
    public Long getUserIdByLogin(String login) {
        List<Long> result = baseDao.executeHqlQuery("select id from User where login=?", new Object[]{login});
        return CollectionUtils.isEmpty(result) ? null : result.get(0);
    }
}
