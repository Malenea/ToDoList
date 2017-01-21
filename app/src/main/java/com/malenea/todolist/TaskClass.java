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
    private int taskHourBegin;
    private int taskMinuteBegin;
    private int taskHourEnd;
    private int taskMinuteEnd;
    private Long taskCalId;

    private int taskCat;

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
        this.taskHourBegin = task.getTaskHourBegin();
        this.taskMinuteBegin = task.getTaskMinuteBegin();
        this.taskHourEnd = task.getTaskHourEnd();
        this.taskMinuteEnd = task.getTaskMinuteEnd();
        this.taskCalId = task.getTaskCalId();
        this.taskCat = task.getTaskCat();
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
    public void setTaskHourBegin(int hour) {
        this.taskHourBegin = hour;
    }
    public void setTaskMinuteBegin(int minute) {
        this.taskMinuteBegin = minute;
    }
    public void setTaskHourEnd(int hour) {
        this.taskHourEnd = hour;
    }
    public void setTaskMinuteEnd(int minute) {
        this.taskMinuteEnd = minute;
    }
    public void setTaskCalId(Long id) {
        this.taskCalId = id;
    }
    public void setTaskCat(int cat) {
        this.taskCat = cat;
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
    public int getTaskHourBegin() {
        return taskHourBegin;
    }
    public int getTaskMinuteBegin() {
        return taskMinuteBegin;
    }
    public int getTaskHourEnd() {
        return taskHourEnd;
    }
    public int getTaskMinuteEnd() {
        return taskMinuteEnd;
    }
    public Long getTaskCalId() {
        return taskCalId;
    }
    public int getTaskCat() {
        return taskCat;
    }
    public int getTaskStatus() {
        return taskStatus;
    }

}
