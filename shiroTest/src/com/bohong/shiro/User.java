package com.bohong.shiro;

/**
 * Created by lenovo on 2019-2-25.
 */
public class User {
    private String username;
    private String password;
    private String perms;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }
}
