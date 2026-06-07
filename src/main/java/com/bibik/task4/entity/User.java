package com.bibik.task4.entity;

public class User extends AbstractEntity {
    private String login;
    private String password;
    private String email;
    private String role;
    private String locale;

    public User() {}

    public User(int id, String login, String password, String email, String role, String locale) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
        this.role = role;
        this.locale = locale;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
}
