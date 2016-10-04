package ru.fors.sample.core.dto;

import java.io.Serializable;

/**
 * Author: Lebedev Aleksandr
 * Date: 24.02.16
 */
public class UserAuth implements Serializable {

    private String password;
    private String username;

    public UserAuth() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
