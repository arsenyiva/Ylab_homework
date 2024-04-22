package org.ylab.homework.homework_2.model;

/**
 * Класс, представляющий тип тренировки.
 */
public class TrainingType {
    private String name;
    private int id;

    public TrainingType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public TrainingType(String name) {
        this.name = name;
    }

    public TrainingType() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}