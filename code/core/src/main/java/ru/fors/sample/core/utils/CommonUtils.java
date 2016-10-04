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

import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: Lebedev A. S.
 * Date: 19.02.16
 */
public class CommonUtils {

    public static <T> T cloneObject(T orig) {
        T obj;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            obj = (T) in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static <T> List<T> listSubtract(final List<T> list1, final List<T> list2) {
        ArrayList<T> result = new ArrayList<T>(list1);
        for (T object : list2) {
            result.remove(object);
        }
        return result;
    }

    public static <T> T cloneCleanObject(T orig) {
        Class<?> tClass = orig.getClass();
        try {
            return (T) tClass.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String asChar(Boolean value) {
        return value == null ? null : value ? "Y" : "N";
    }

    public static Date startOfTheDay(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date endOfTheDay(Date date) {
        if (date == null) return null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Double round(Double value, int precision) {
        if (value == null) {
            return value;
        }
        MathContext mathContext = new MathContext(0);
        BigDecimal pow = new BigDecimal(Math.pow(10, precision));
        BigDecimal valueDecimal = new BigDecimal(value);
        valueDecimal = valueDecimal.multiply(pow, mathContext).round(mathContext);
        return valueDecimal.divide(pow).doubleValue();
    }



    public static String nvl(String s) {
        return s == null ? "" : s;
    }

    public static String nvlPrefix(String prefix, String s) {
        return s == null ? "" : (prefix + s);
    }

    public static String nvlPrefixSuffix(String prefix, String value, String suffix) {
        return value != null ? prefix + value + suffix : "";
    }

    public static <T> T nvl(T obj, T ifNull) {
        return obj == null ? ifNull : obj;
    }

    public static boolean equals(Object a, Object b) {
        return a == null ? b == null : a.equals(b);
    }



    public static String toString(Object o) {
        return o == null ? null : o.toString();
    }

    public static Long parseLong(String s) {
        return StringUtils.hasText(s) ? Long.parseLong(s) : null;
    }


}
