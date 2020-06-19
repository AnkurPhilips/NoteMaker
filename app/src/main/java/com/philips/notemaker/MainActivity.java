package com.philips.notemaker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private RecyclerView mRecyclerNotes;
    private NoteMakerViewModel mViewModel;
    private ItemTouchHelper.SimpleCallback mSimpleCallback;
    private ItemTouchHelper mItemTouchHelper;
    private LiveData<List<Note>> mNotes;
    private LiveData<List<Course>> mCourses;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private NotificationManager mNotifyManager;
    private static final int NOTIFICATION_ID = 0;
    private NotificationCompat.Builder mNotifyBuilder;

    private NotificationCompat.Builder getNotificationBuilder(Note note){
        Intent intent = new Intent(this,NoteActivity.class);
        intent.putExtra(NoteActivity.COURSE_ID,note.getCourseId());
        intent.putExtra(NoteActivity.NOTE_TITLE,note.getNoteTitle());
        intent.putExtra(NoteActivity.NOTE_TEXT,note.getNoteText());

        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                .setContentTitle(note.getNoteTitle())
                .setContentText(note.getNoteText())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true);
        return notifyBuilder;


    }


    public void createNotificationChannel()
    {
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "NoteMaker Notification", NotificationManager.IMPORTANCE_HIGH
                    );
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from NoteMaker");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                startActivity(new Intent(MainActivity.this,NoteActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ViewModelProvider provider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = provider.get(NoteMakerViewModel.class);

        mNotes = mViewModel.getAllNotes();
        mCourses = mViewModel.getAllCourses();

        mCourses.observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                mNoteRecyclerAdapter.setCourses(courses);
            }
        });

        mNotes.observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mNoteRecyclerAdapter.setNotes(notes);
            }
        });

        initializeDisplayContent();

        mSimpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NoteMakerRepository noteMakerRepository = new NoteMakerRepository(getApplication());
                noteMakerRepository.delete(mNotes.getValue().get(position));
                mNoteRecyclerAdapter.notifyItemRemoved(position);
            }
        };
        mItemTouchHelper = new ItemTouchHelper(mSimpleCallback);

        mItemTouchHelper.attachToRecyclerView(mRecyclerNotes);

        createNotificationChannel();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNoteRecyclerAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initializeDisplayContent() {
        mRecyclerNotes = findViewById(R.id.list_items);
        LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);
        mRecyclerNotes.setLayoutManager(notesLayoutManager) ;


        mNoteRecyclerAdapter = new NoteRecyclerAdapter(this);
        mRecyclerNotes.setAdapter(mNoteRecyclerAdapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.action_settings){
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mNotes.getValue()!=null) {
            int index = new Random().nextInt(mNotes.getValue().size());
            mNotifyBuilder = getNotificationBuilder(mNotes.getValue().get(index));
            mNotifyManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
        }
    }
}