package com.grobo.messscanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("select * from usermodel where instituteId = :id")
    UserModel loadUserByInstituteId(String id);

    @Insert(onConflict = REPLACE)
    void insertUser(UserModel userModel);

    @Query("DELETE FROM usermodel")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM usermodel where instituteId = :id")
    int getUserCount(String id);

}
