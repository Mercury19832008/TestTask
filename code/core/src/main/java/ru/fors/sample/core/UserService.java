/*
 * Copyright (c) 2016 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package ru.fors.sample.core;

import ru.fors.sample.core.dto.User;

/**
 * Author: Lebedev Aleksandr
 * Date: 26.02.16
 *
 * Примечание: практически все ф-ции утащены из ПФР проекта (лебедев\шалыгин)
 * , поэтому недостающие части смотреть надо там
 */
public interface UserService {
    User getUserById(long id);

    Long getUserIdByLogin(String login);
}
