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

package ru.fors.sample.core.utils;

/**
 * Author: Lebedev Aleksandr
 * Date: 01.03.16
 */
public final class BooleanUtils {
    private BooleanUtils() {
        //hide constructor for utility class
    }

    /**
     * Преобразовать значение для сохранения в БД.
     *
     * @param value значение
     * @return преобразованное значение
     */
    public static String toString(Boolean value) {
        return value == null ? null : (value ? "Y" : "N");
    }

    /**
     *  Преобразовать значение для сохранения в БД.
     *
     * @param value значение
     * @return преобразованное значение
     */
    public static Integer toInt(Boolean value) {
        return value == null ? null : (value ? 1 : 0);
    }

    /**
     * Перобразовать значение из БД в boolean.
     *
     * @param value значение
     * @return преобразованное значение
     */
    public static Boolean toBoolean(String value) {
        if (value == null) {
            return null;
        }

        switch (value) {
            case "N":
                return false;
            case "Y":
                return true;
            default:
                throw new IllegalArgumentException("Bad value for boolean field " + value);
        }
    }
}
