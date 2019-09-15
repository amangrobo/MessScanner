package com.grobo.messscanner.database;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Keep
@Entity(tableName = "mess")
public class MessModel {

    @PrimaryKey
    @NonNull
    @Expose
    @SerializedName("_id")
    private String id = "";
    @Expose
    @SerializedName("student")
    @Embedded(prefix = "student_")
    private Student student;
    @Expose
    @SerializedName("messChoice")
    private int messChoice;

    @Expose
    @SerializedName("breakfast")
    private List<Long> breakfast = new ArrayList<>();
    @Expose
    @SerializedName("lunch")
    private List<Long> lunch = new ArrayList<>();
    @Expose
    @SerializedName("snacks")
    private List<Long> snacks = new ArrayList<>();
    @Expose
    @SerializedName("dinner")
    private List<Long> dinner = new ArrayList<>();

    @Expose
    @SerializedName("fullday")
    private List<Long> fullDay = new ArrayList<>();

    private List<String> taken = new ArrayList<>();

    public MessModel() {}

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getMessChoice() {
        return messChoice;
    }

    public void setMessChoice(int messChoice) {
        this.messChoice = messChoice;
    }

    public List<Long> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(List<Long> breakfast) {
        this.breakfast = breakfast;
    }

    public List<Long> getLunch() {
        return lunch;
    }

    public void setLunch(List<Long> lunch) {
        this.lunch = lunch;
    }

    public List<Long> getSnacks() {
        return snacks;
    }

    public void setSnacks(List<Long> snacks) {
        this.snacks = snacks;
    }

    public List<Long> getDinner() {
        return dinner;
    }

    public void setDinner(List<Long> dinner) {
        this.dinner = dinner;
    }

    public List<Long> getFullDay() {
        return fullDay;
    }

    public void setFullDay(List<Long> fullDay) {
        this.fullDay = fullDay;
    }

    public List<String> getTaken() {
        return taken;
    }

    public void setTaken(List<String> taken) {
        this.taken = taken;
    }

    @Keep
    public class MessSuper {

        @Expose
        @SerializedName("mess")
        private List<MessModel> messModels;

        public List<MessModel> getMessModels() {
            return messModels;
        }

        public void setMessModels(List<MessModel> messModels) {
            this.messModels = messModels;
        }
    }

}
