package ru.fors.sample.core.dto;

import javax.persistence.Column;
import javax.persistence.Id;
 import javax.persistence.*;
/**
 * @author Mikhail Kokorin
 *         date: 04.10.2016
 *         time: 17:57
 */
@Entity
@Table(name = "users")
public class User extends Dto {
    @Id
    private Long id;
    @Column(name = "login", length = 100)
    private String login;
    @Column(name = "name", length = 255)
    private String name;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
