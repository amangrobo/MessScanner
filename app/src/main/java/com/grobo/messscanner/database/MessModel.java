package com.grobo.messscanner.database;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Keep
@Entity(tableName = "mess")
public class MessModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int mess;

    private String name;
    private String token;
    private String roll;
    private boolean taken;

    @Ignore
    public MessModel() {}

    public MessModel(String roll, String name, int mess, String token) {
        this.roll = roll;
        this.name = name;
        this.mess = mess;
        this.token = token;
    }



    public int getMess() {
        return mess;
    }

    public void setMess(int mess) {
        this.mess = mess;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}
