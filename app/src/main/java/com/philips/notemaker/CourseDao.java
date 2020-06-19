package com.philips.notemaker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    void insert(Course course);

    @Query("DELETE FROM courses_table")
    void deleteAll();

    @Query("SELECT* FROM courses_table")
    LiveData<List<Course>> getAllCourses();
}
