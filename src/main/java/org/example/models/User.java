package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;

    private boolean enabled;
    private Set<Role> authorities;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean accountNonExpired;

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
