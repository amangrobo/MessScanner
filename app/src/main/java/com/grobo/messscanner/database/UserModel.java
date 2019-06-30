package com.grobo.messscanner.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.List;

@Entity
public class UserModel {

    @PrimaryKey
    @NonNull
    private String studentMongoId = "-1";
    private String instituteId;
    private int mess;
    private String name;
    private List<String> foodData = new ArrayList<>();  //16_7_19_1_0    day_month_year_mealNumber_status

    //status: 0 for food not taken, 1 for food taken and -1 for food cancelled

    @Ignore
    public UserModel() {}

    public UserModel (String studentMongoId, String instituteId, int mess, String name) {
        this.studentMongoId = studentMongoId;
        this.instituteId = instituteId;
        this.name = name;
        this.mess = mess;
    }

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

    @NonNull
    public String getStudentMongoId() {
        return studentMongoId;
    }

    public void setStudentMongoId(@NonNull String studentMongoId) {
        this.studentMongoId = studentMongoId;
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
}
