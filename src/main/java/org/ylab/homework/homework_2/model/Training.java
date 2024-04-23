package org.ylab.homework.homework_2.model;

import java.time.LocalDate;

/**
 * Класс, представляющий тренировку.
 */
public class Training {
    private int id;
    private LocalDate date;
    private TrainingType type;
    private int durationMinutes;
    private int caloriesBurned;
    private String additionalInfo;
    private User user;

    public Training(int id, LocalDate date, TrainingType type, int durationMinutes, int caloriesBurned, String additionalInfo, User user) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.durationMinutes = durationMinutes;
        this.caloriesBurned = caloriesBurned;
        this.additionalInfo = additionalInfo;
        this.user = user;
    }

    public Training() {
    }

    public LocalDate getDate() {
        return date;
    }

    public TrainingType getType() {
        return type;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getCaloriesBurned() {
        return caloriesBurned;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setCaloriesBurned(int caloriesBurned) {
        this.caloriesBurned = caloriesBurned;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

