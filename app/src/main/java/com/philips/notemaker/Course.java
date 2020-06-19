package com.philips.notemaker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses_table")
public class Course {
    public Course(@NonNull String courseId, @NonNull String courseTitle) {
        this.courseId = courseId;
        this.courseTitle = courseTitle;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "course_id" )
    String courseId ;

    @NonNull
    @ColumnInfo(name = "course_title")
    String courseTitle ;

    @NonNull
    @Override
    public String toString() {
        return courseTitle;
    }
}
