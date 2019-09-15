package com.grobo.messscanner.database;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Student {

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("_id")
    private String id;
    @Expose
    @SerializedName("instituteId")
    private String instituteId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }
}