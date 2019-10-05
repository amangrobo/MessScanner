package com.grobo.messscanner.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MessDao {

    @Query("select * from mess where token like :token")
    MessModel loadUserByToken(String token);

    @Query("select * from mess")
    List<MessModel> loadAllUsers();

    @Insert(onConflict = REPLACE)
    void insertUser(MessModel messModel);

    @Query("DELETE FROM mess")
    void deleteAllUsers();

    @Query("SELECT COUNT(*) FROM mess")
    int getUserCount();

}
