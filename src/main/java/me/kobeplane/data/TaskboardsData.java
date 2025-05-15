package me.kobeplane.data;

import com.j256.ormlite.field.DatabaseField;

public class TaskboardsData {

    @DatabaseField(canBeNull = false, generatedId = true)
    private int taskboardId;

    @DatabaseField(canBeNull = false, foreign = true)
    private UserData userId;

    @DatabaseField(canBeNull = false, unique = true)
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

    public UserData getUserId() {
        return userId;
    }

    public void setUserId(UserData userId) {
        this.userId = userId;
    }

}
