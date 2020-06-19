package com.philips.notemaker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "notes_table", primaryKeys = {"note_title","course_id"})
public class Note {

    public Note(@NonNull String noteTitle, String noteText, @NonNull String courseId) {
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.courseId = courseId;
    }


    @NonNull
    @ColumnInfo(name = "note_title")
    private String noteTitle ;

    @NonNull
    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    @NonNull
    public String getCourseId() {
        return courseId;
    }

    @ColumnInfo(name = "note_text")
    private String noteText ;

    @NonNull
    @ColumnInfo(name = "course_id")
    private String courseId;
}
