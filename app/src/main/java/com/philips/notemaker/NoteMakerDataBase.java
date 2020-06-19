package com.philips.notemaker;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {Note.class,Course.class},version = 1,exportSchema = false)
public abstract class NoteMakerDataBase extends RoomDatabase {

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final CourseDao mDao;
        List<Course> mCourses = new ArrayList<>();




        PopulateDbAsync(NoteMakerDataBase db){
            mDao = db.courseDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            mDao.deleteAll();
            mCourses.add(new Course("java_lang", "Java Fundamentals: The Java Language"));
            mCourses.add(new Course("android_intents", "Android Programming with Intents"));
            mCourses.add(new Course("java_core", "Java Fundamentals: The Core Platform"));
            mCourses.add(new Course("android_async", "Android Async Programming and Services"));

            for(Course course: mCourses)
                mDao.insert(course);

            return null;
        }
    }


    public static NoteMakerDataBase INSTANCE;

    public abstract NoteDao noteDao();
    public abstract CourseDao courseDao();

    public static NoteMakerDataBase getDataBase(final Context context){
        if(INSTANCE==null)
        {
            synchronized (NoteMakerDataBase.class){
                if(INSTANCE==null)
                {
                    INSTANCE = Room.databaseBuilder(context,NoteMakerDataBase.class,"note_maker_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(sRoomDatabaseCallback)
                    .build();
                }
            }
        }
        return INSTANCE;
    }
}
