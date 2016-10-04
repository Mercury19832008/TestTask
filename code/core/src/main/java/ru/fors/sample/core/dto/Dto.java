package ru.fors.sample.core.dto;

import java.io.Serializable;

/**
 * Author: Alexey Perminov
 * Date: 01.08.2010
 * Time: 10:17:45
 */
public abstract class Dto implements Serializable {
    public abstract Long getId();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Dto && getId() != null)
            return getId().equals(((Dto) obj).getId());
        return super.equals(obj);
    }
}
