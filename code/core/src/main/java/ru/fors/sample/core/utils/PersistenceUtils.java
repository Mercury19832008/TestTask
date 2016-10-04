package ru.fors.sample.core.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistenceUtils {

    public static boolean isLazy(Class clazz, String property) {
        boolean isLazy = false;
        boolean isForceFetch = false;
        while (!clazz.equals(Object.class)) {
            Field field = null;
            try {
                field = clazz.getDeclaredField(property);
            } catch (NoSuchFieldException ignored) {
            }
            if (field != null && field.getAnnotations() != null)
                for (Annotation annotation : field.getAnnotations()) {
                    if (annotation instanceof ForceFetch)
                        isForceFetch = true;
                    if ((annotation instanceof OneToMany && ((OneToMany) annotation).fetch().equals(FetchType.LAZY)) ||
                            (annotation instanceof ManyToMany && ((ManyToMany) annotation).fetch().equals(FetchType.LAZY)))
                        isLazy = true;
                }
            clazz = clazz.getSuperclass();
        }
        return !isForceFetch && isLazy;
    }

    public static String getEntityName(Class clazz) {
        while (!clazz.equals(Object.class)) {
            for (Annotation annotation : clazz.getAnnotations()) {
                if (annotation instanceof Table)
                    return StringUtils.hasText(((Table) annotation).name()) ? ((Table) annotation).name() : clazz.getSimpleName();
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static void forceFetch(List list) {
        forceFetch(list, new HashSet());
    }

    public static void forceFetch(Object object) {
        forceFetch(object, new HashSet());
    }

    private static void forceFetch(List list, Set cache) {
        if (list == null)
            return;
        for (Object item : list)
            forceFetch(item, cache);
    }

    @SuppressWarnings({"ConstantConditions"})
    private static void forceFetch(Object object, Set cache) {
        if (!isDto(object))
            return;
        if (cache.contains(object))
            return;
        cache.add(object);
        Class objClass = object.getClass();
        while (!objClass.equals(Object.class)) { //вверх по иерархии
            for (Field field : objClass.getDeclaredFields()) {
                if (isDto(field.getType()))
                    try {
                        PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(objClass, field.getName());
                        if (propertyDescriptor != null) {
                            Method readMethod = propertyDescriptor.getReadMethod();
                            if (readMethod != null)
                                forceFetch(readMethod.invoke(object), cache);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (Collection.class.isAssignableFrom(field.getType()))
                    if (!isLazy(objClass, field.getName()))
                        try {
                            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(objClass, field.getName());
                            if (propertyDescriptor != null) {
                                Collection list = (Collection) propertyDescriptor.getReadMethod().invoke(object);
                                if (list != null && !list.isEmpty())
                                    for (Object item : list) {
                                        if (isDto(item.getClass()))
                                            forceFetch(item, cache);
                                        else
                                            break;
                                    }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }
            objClass = objClass.getSuperclass();
        }
    }

    private static boolean isDto(Object object) {
        return object != null && isDto(object.getClass());
    }

    private static boolean isDto(Class clazz) {
        return clazz != null && !clazz.getName().startsWith("java.lang.");
    }

    public static void fetchLazy(Object object) {
        fetchLazy(object, new HashSet());
    }

    @SuppressWarnings({"ConstantConditions"})
    private static void fetchLazy(Object object, Set cache) {
        if (!isDto(object))
            return;
        if (cache.contains(object))
            return;
        cache.add(object);
        Class objClass = object.getClass();
        while (!objClass.equals(Object.class)) { //вверх по иерархии
            for (Field field : objClass.getDeclaredFields()) {
                PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(objClass, field.getName());
                if (propertyDescriptor == null)
                    continue;
                Method readMethod = propertyDescriptor.getReadMethod();
                if (isDto(field.getType()))
                    try {
                        if (readMethod != null)
                            fetchLazy(readMethod.invoke(object), cache);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                if (Collection.class.isAssignableFrom(field.getType())) {
                    try {
                        Collection list = (Collection) readMethod.invoke(object);
                        if (list != null && !list.isEmpty())
                            for (Object item : list) {
                                if (isDto(item.getClass()))
                                    fetchLazy(item, cache);
                                else
                                    break;
                            }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            objClass = objClass.getSuperclass();
        }
    }

    public static int getFieldLength(Class clazz, String aField) {
        int index = aField.indexOf('.');
        String field = aField.substring(0, index > 0 ? index : aField.length());
        while (!clazz.equals(Object.class)) {
            try {
                Field declaredField = clazz.getDeclaredField(field);
                if (String.class.isAssignableFrom(declaredField.getType()))
                    for (Annotation annotation : clazz.getDeclaredField(field).getAnnotations()) {
                        if (annotation instanceof Column)
                            return ((Column) annotation).length(); //в аннтоации по умолчанию 255! если в мапинге нет уточнения то будет 255
                    }
                else if (index > 0)
                    return getFieldLength(declaredField.getType(), aField.substring(index));
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return 0;
    }

    public static <T extends Annotation> T getAnnotation(Object object, Class objectClass, String field, Class<T> annotationClazz) {
        if (object != null)
            objectClass = object.getClass();
        int index = field.indexOf(".");
        if (index > 0) {
            return getAnnotation(getPropertyValue(object, field.substring(0, index)),
                    getPropertyType(objectClass,
                    field.substring(0, index)),
                    field.substring(index + 1), annotationClazz);
        } else {
            T annotation = null;
            do {
                try {
                    annotation = objectClass.getDeclaredField(field).getAnnotation(annotationClazz);
                } catch (NoSuchFieldException ignored) {
                }
                if (annotation == null) {
                    PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(objectClass, field);
                    if (descriptor != null && descriptor.getReadMethod() != null)
                        annotation = descriptor.getReadMethod().getAnnotation(annotationClazz);
                    if (annotation == null && descriptor != null && descriptor.getWriteMethod() != null)
                        annotation = descriptor.getWriteMethod().getAnnotation(annotationClazz);
                }
                objectClass = objectClass.getSuperclass();
            } while (annotation == null && objectClass != Object.class);
            return annotation;
        }
    }

    public static Class getPropertyType(Class clazz, String field) {
        while (!clazz.equals(Object.class)) {
            try {
                return BeanUtils.getPropertyDescriptor(clazz, field).getReadMethod().getReturnType();
            } catch (Exception ignored) {
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public static Object getPropertyValue(Object object, String field) {
        if (object != null)
            try {
                return BeanUtils.getPropertyDescriptor(object.getClass(), field).getReadMethod().invoke(object);
            } catch (Exception ignored) {
            }
        return null;
    }
}
