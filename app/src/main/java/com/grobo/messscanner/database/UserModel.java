package com.grobo.messscanner.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class UserModel {

    @PrimaryKey
    @NonNull
    private String instituteId = "-1";
    private String mess;
    private String name;
    private List<String> foodData;  //16_7_19_1_0    day_month_year_mealNumber_status

    //status: 0 for food not taken, 1 for food taken and -1 for food cancelled

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

    public String getMess() {
        return mess;
    }

    public String getName() {
        return name;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public List<String> getFoodData() {
        return foodData;
    }

    public void setFoodData(List<String> foodData) {
        this.foodData = foodData;
    }
}
