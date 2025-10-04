package com.example.coachprueba.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE nombreCompleto = :nombreCompleto AND contraseña = :contraseña")
    User login(String nombreCompleto, String contraseña);

    @Query("SELECT * FROM users WHERE id = :userId")
    User getUserById(int userId);

    @Update
    void updateUser(User user);

    @Query("SELECT COUNT(*) FROM users WHERE nombreCompleto = :nombreCompleto")
    int checkUserExists(String nombreCompleto);
}
