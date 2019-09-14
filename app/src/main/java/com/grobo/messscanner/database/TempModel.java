package com.grobo.messscanner.database;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class TempModel {

//    private List<Long> days = new ArrayList<>();
    private boolean full;
    private List<Integer> meals = new ArrayList<>();

    public TempModel() {
    }

//    public List<Long> getDays() {
//        return days;
//    }
//
//    public void setDays(List<Long> days) {
//        this.days = days;
//    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public List<Integer> getMeals() {
        return meals;
    }

    public void setMeals(List<Integer> meals) {
        this.meals = meals;
    }
}
