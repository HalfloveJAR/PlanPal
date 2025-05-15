package me.kobeplane.data;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

public class TasksData {

    @DatabaseField(canBeNull = false, generatedId = true)
    private int taskId;

    @DatabaseField(canBeNull = false, foreign = true)
    private TaskboardsData taskboardId;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField(canBeNull = false)
    private String priority;

    @DatabaseField(canBeNull = false)
    private boolean done;

    @DatabaseField
    private Date dueDate;

    public TasksData() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean getDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

}
