package me.kobeplane.data;

import com.j256.ormlite.field.DatabaseField;

public class TaskboardsData {

    @DatabaseField(canBeNull = false, generatedId = true)
    private int taskboardId;

    @DatabaseField(canBeNull = false, foreign = true)
    private UserData userId;

    @DatabaseField(canBeNull = false)
    private String name;

    public TaskboardsData() {}

    public int getTaskboardId() {
        return taskboardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
