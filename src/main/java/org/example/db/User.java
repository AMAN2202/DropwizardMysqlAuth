package org.example.db;

import java.security.Principal;
import java.util.Set;

public class User implements Principal {
    private String email;
    private String password;

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    private  Set<String> roles;

    public String getEmail() {
        return email;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    public Set<String> getRoles() {
        return roles;
    }


    @Override
    public String getName() {
        return email;
    }
}
