package com.grobo.messscanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MessDao {

    @Query("select * from mess where student_id like :id")
    MessModel loadUserByMongoId(String id);

    @Query("select * from mess")
    List<MessModel> loadAllUsers();

    @Insert(onConflict = REPLACE)
    void insertUser(MessModel messModel);

    @Update
    void updateUser(MessModel messModel);

    @Query("DELETE FROM mess")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM mess")
    int getUserCount();

}
