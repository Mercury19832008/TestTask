package ru.fors.sample.core.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface ForceFetch {
    //Используется для пометки lazy - коллекций, для принудительного чтения при загрузке
    //т.к. при поиске через критерии коллекции без lazy искажают результат поиска
}
