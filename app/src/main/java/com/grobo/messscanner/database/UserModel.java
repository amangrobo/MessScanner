package com.grobo.messscanner.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
@Entity
public class UserModel {

    @PrimaryKey
    @NonNull
    private String email = "";
    private String instituteId;
    private int mess = 0;
    private String name;
    private List<String> foodData = new ArrayList<>();  //16_7_19_1_0    day_month_year_mealNumber_status
    //status: 0 for food not taken, 1 for food taken and -1 for food cancelled

    public UserModel() {}

    @NonNull
    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(@NonNull String instituteId) {
        this.instituteId = instituteId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFoodData() {
        return foodData;
    }

    public void setFoodData(List<String> foodData) {
        this.foodData = foodData;
    }

    public void setMess(int mess) {
        this.mess = mess;
    }

    public String getName() {
        return name;
    }

    public int getMess() {
        return mess;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }
}
