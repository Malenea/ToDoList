package com.malenea.todolist;

/**
 * Created by conan on 14/01/17.
 */

public class TaskClass {
    private int taskId;
    private String taskTitle;

    private String taskDesc;

    private int taskYear;
    private int taskMonth;
    private int taskDay;
    private int taskHour;
    private int taskMinute;

    private int taskStatus;

    TaskClass() {
    }

    TaskClass(TaskClass task) {
        this.taskId = task.getTaskId();
        this.taskTitle = task.getTaskTitle();
        this.taskDesc = task.getTaskDesc();
        this.taskYear = task.getTaskYear();
        this.taskMonth = task.getTaskMonth();
        this.taskDay = task.getTaskDay();
        this.taskHour = task.getTaskHour();
        this.taskMinute = task.getTaskMinute();
        this.taskStatus = task.getTaskStatus();
    }

    public void setTaskId(int id) {
        this.taskId = id;
    }
    public void setTaskTitle(String task) {
        this.taskTitle = task;
    }
    public void setTaskDesc(String desc) {
        this.taskDesc = desc;
    }
    public void setTaskYear(int year) {
        this.taskYear = year;
    }
    public void setTaskMonth(int month) {
        this.taskMonth = month;
    }
    public void setTaskDay(int day) {
        this.taskDay = day;
    }
    public void setTaskHour(int hour) {
        this.taskHour = hour;
    }
    public void setTaskMinute(int minute) {
        this.taskMinute = minute;
    }
    public void setTaskStatus(int status) {
        this.taskStatus = status;
    }

    public int getTaskId() {
        return taskId;
    }
    public String getTaskTitle() {
        return taskTitle;
    }
    public String getTaskDesc() {
        return taskDesc;
    }
    public int getTaskYear() {
        return taskYear;
    }
    public int getTaskMonth() {
        return taskMonth;
    }
    public int getTaskDay() {
        return taskDay;
    }
    public int getTaskHour() {
        return taskHour;
    }
    public int getTaskMinute() {
        return taskMinute;
    }
    public int getTaskStatus() {
        return taskStatus;
    }

}
