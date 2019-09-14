package com.grobo.messscanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("select * from usermodel where email like :id")
    UserModel loadUserById(String id);

    @Query("select * from usermodel")
    List<UserModel> loadAllUsers();

    @Insert(onConflict = REPLACE)
    void insertUser(UserModel userModel);

    @Update
    void updateUser(UserModel userModel);

    @Query("DELETE FROM usermodel")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM usermodel where email = :id")
    int getUserCount(String id);

}
