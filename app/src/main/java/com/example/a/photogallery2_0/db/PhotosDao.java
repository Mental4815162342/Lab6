package com.example.a.photogallery2_0.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.a.photogallery2_0.model.Photo;

import java.util.List;

@Dao
public interface PhotosDao {
    @Query("SELECT * FROM Photo")
    List<Photo> LoadAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhoto(Photo photo);

    @Delete
    void deletePhoto(Photo photo);
}

