package com.example.our_stories.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.our_stories.R;
import com.example.our_stories.model.Chapter;
import com.example.our_stories.model.Comment;

import java.util.List;

public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerAdapter.ViewHolder>{
    private List<Chapter> chapters;
    private LayoutInflater mInflater;
    private chapterClickListener mClickListener;

    // data is passed into the constructor
    ChapterRecyclerAdapter(Context context, List<Chapter> data) {
        this.mInflater = LayoutInflater.from(context);
        this.chapters = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chapter_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return chapters.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Title;
        TextView chapterNum;
        TextView date;

        ViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.chapter_row_title);
            chapterNum = itemView.findViewById(R.id.chapter_row_number);
            date = itemView.findViewById(R.id.chapter_row_date);
        }

    }

    // convenience method for getting data at click position
    Chapter getItem(int id) {
        return chapters.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(chapterClickListener listener) {
        this.mClickListener = listener;
    }

    // parent activity will implement this method to respond to click events
    public interface chapterClickListener {
        void onItemClick(View view, int position);
    }
}
