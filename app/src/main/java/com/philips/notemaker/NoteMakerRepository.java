package com.philips.notemaker;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteMakerRepository {
    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao mAsyncNoteDao;

        InsertNoteAsyncTask(NoteDao noteDao){
            mAsyncNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncNoteDao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao mAsyncNoteDao;

        DeleteNoteAsyncTask(NoteDao noteDao){
            mAsyncNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncNoteDao.delete(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void>{
        private NoteDao mAsyncNoteDao;

        UpdateNoteAsyncTask(NoteDao noteDao){
            mAsyncNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncNoteDao.update(notes[0]);
            return null;
        }
    }

    private NoteDao mNoteDao;
    private CourseDao mCourseDao;

    LiveData<List<Note>> mNotes;
    LiveData<List<Course>> mCourses;

    NoteMakerRepository(Application application){
        NoteMakerDataBase db = NoteMakerDataBase.getDataBase(application);
        mCourseDao = db.courseDao();
        mCourses = mCourseDao.getAllCourses();
        mNoteDao = db.noteDao();
        mNotes = mNoteDao.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes(){
        return mNotes;
    }

    LiveData<List<Course>> getAllCourses(){
        return mCourses;
    }

    public void insert(Course course){
        mCourseDao.insert(course);
    }

    public void insert(Note note){
        new InsertNoteAsyncTask(mNoteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }


}
