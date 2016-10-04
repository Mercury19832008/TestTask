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

import org.joda.time.DateTime;

import java.sql.Time;
import java.time.*;
import java.util.Date;

/**
 * Created by Maxim Kropachev
 * 14.04.2016
 */
public final class DateUtils {
    private DateUtils() {
    }

    public static String formatDate(Date date) {
        return new DateTime(date).toString("dd.MM.yyyy");
    }

    public static long getYear(Date date) {
        return new DateTime(date).getYear();
    }

    public static int getMonth(Date date) {
        return new DateTime(date).getMonthOfYear();
    }

    public static Date toDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static Date toDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    public static LocalTime toLocalTime(Time time) {
        if (time == null) {
            return null;
        }
        return time.toLocalTime();
    }

    public static LocalDate toLocalDate(java.sql.Date date) {
        if (date == null) {
            return null;
        }
        return date.toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(java.sql.Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime();
    }

}
