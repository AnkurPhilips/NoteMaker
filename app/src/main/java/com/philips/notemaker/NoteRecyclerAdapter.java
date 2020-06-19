package com.philips.notemaker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder> {

    public final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Note> mNotes;
    private List<Course> mCourses;

    void setNotes(List<Note> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    void setCourses(List<Course> courses){
        mCourses = courses;
    }



    public NoteRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

     class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextCourse;
        public  final TextView mTextTitle;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            mTextCourse = itemView.findViewById(R.id.text_course);
            mTextTitle = itemView.findViewById(R.id.text_title);


        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_note_list,parent,false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Note current;
        if(mNotes!=null){
            current = mNotes.get(position);
            for(Course course: mCourses){
                if(course.courseId.equals(current.getCourseId()))
                    holder.mTextCourse.setText(course.courseTitle);
            }

            holder.mTextTitle.setText(current.getNoteTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,NoteActivity.class);
                    intent.putExtra(NoteActivity.COURSE_ID,current.getCourseId());
                    intent.putExtra(NoteActivity.NOTE_TITLE,current.getNoteTitle());
                    intent.putExtra(NoteActivity.NOTE_TEXT,current.getNoteText());
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mNotes==null?0:mNotes.size();
    }
}
