package com.example.mybatis.model;

public class TestModel {
    private int id;
    private String text;

    public TestModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TestModel{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
