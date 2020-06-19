package com.philips.notemaker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteMakerViewModel extends AndroidViewModel {
    private NoteMakerRepository mRepository;
    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<Course>> mAllCourses;

    public NoteMakerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteMakerRepository(application);
        mAllCourses = mRepository.getAllCourses();
        mAllNotes = mRepository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return mAllNotes;
    }
    LiveData<List<Course>> getAllCourses(){
        return mAllCourses;
    }
}
