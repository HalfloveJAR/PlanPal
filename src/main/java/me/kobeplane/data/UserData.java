package me.kobeplane.data;

import com.j256.ormlite.field.DatabaseField;

public class UserData {

    @DatabaseField(canBeNull = false, generatedId = true)
    private int userId;

    @DatabaseField(canBeNull = false)
    private String email;

    public UserData() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

}
