package org.ylab.homework.homework_1.model;

import java.util.List;

public class User {
    private int id;
    private String username;
    private String password;
    private Role role;
    private List<Training> trainings;

    public User(int id,String username, String password, Role role, List<Training> trainings) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.trainings = trainings;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}