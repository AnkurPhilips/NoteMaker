package com.philips.notemaker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class NoteActivity extends AppCompatActivity {
    public static final String COURSE_ID = "com.philips.notemaker.COURSE_ID";
    public static final String NOTE_TITLE = "com.philips.notemaker.NOTE_TITLE";
    public static final String NOTE_TEXT = "com.philips.notemaker.NOTE_TEXT";
    public static final int ID_NOT_SET = -1;
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private String mCourseId;
    private String mNoteTitle;
    private String mNoteText;
    private boolean mIsCancelling;
    private ArrayAdapter mAdapterCourses;
    private NoteMakerViewModel mViewModel;
    private LiveData<List<Course>> mCourses;
    private NoteMakerRepository mRepository = new NoteMakerRepository(getApplication());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider provider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = provider.get(NoteMakerViewModel.class);

        mCourses = mViewModel.getAllCourses();
        mSpinnerCourses = findViewById(R.id.spinner_courses);


        mViewModel.getAllCourses().observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courseList) {
                mAdapterCourses = new ArrayAdapter(NoteActivity.this,android.R.layout.simple_spinner_item,courseList);
                mAdapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                mSpinnerCourses.setAdapter(mAdapterCourses);
            }
        });


        mTextNoteTitle = findViewById(R.id.text_note_title);
        mTextNoteText = findViewById(R.id.text_note_text);

        readDisplayStateValues();
//        saveOriginalStateValues();
    }

    private void saveOriginalStateValues() {
        if (mIsNewNote ) {
            return;
        }


    }

    private void displayNote() {


//        mTextNoteTitle.setText(noteTitle);
//        mTextNoteText.setText(noteText);

    }


    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mCourseId = intent.getStringExtra(COURSE_ID);
        mNoteTitle = intent.getStringExtra(NOTE_TITLE);
        mNoteText = intent.getStringExtra(NOTE_TEXT);

        mIsNewNote = mCourseId==null;

        mTextNoteTitle.setText(mNoteTitle);
        mTextNoteText.setText(mNoteText);


    }

    private void createNote() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!mIsCancelling)
            saveNote();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        if(outState!=null){
//            mNoteActivityViewModel.saveState(outState);
//        }
    }

    private void storePreviousNoteValues() {
//        String courseId = mNoteActivityViewModel.mOriginalNoteCourseId;
//        mSpinnerCourses.setSelection(getIndexOfCourseID(courseId));
//        mTextNoteTitle.setText(mNoteActivityViewModel.mOriginalNoteCourseTitle);
//        mTextNoteText.setText(mNoteActivityViewModel.mOriginalNoteCourseText);
    }

    private void saveNote() {
        String courseId = ((Course)mSpinnerCourses.getSelectedItem()).courseId;
        String title = mTextNoteTitle.getText().toString();
        String text = mTextNoteText.getText().toString();
        if(mIsNewNote)
            mRepository.insert(new Note(title,text,courseId));
        else
            mRepository.update(new Note(title,text,courseId));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
//        item.setVisible(mNoteId <lastIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        } else if (id == R.id.action_next) {
            moveNext();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }

    private void moveNext() {
        saveNote();
//        mNote = DataManager.getInstance().getNotes().get(mNoteId);
        saveOriginalStateValues();
        displayNote();


    }

    private void sendEmail() {
//        CourseInfo courseInfo = (CourseInfo) mSpinnerCourses.getSelectedItem();
//        String subject = mTextNoteTitle.getText().toString();
//        String text = "Checkout what I've learnt in the Pluralsight course \"" + courseInfo.getTitle() +
//                "\"\n" + mTextNoteText.getText().toString();
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("message/rfc2822");
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
//        intent.putExtra(Intent.EXTRA_TEXT, text);
//
//        startActivity(intent);


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}