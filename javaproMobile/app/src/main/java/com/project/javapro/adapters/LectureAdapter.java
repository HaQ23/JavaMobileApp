// LectureAdapter.java
package com.project.javapro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.javapro.R;

import java.util.List;

public class LectureAdapter extends RecyclerView.Adapter<LectureAdapter.LectureViewHolder> {

    private List<String> lectures;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String lecture);
    }

    public LectureAdapter(List<String> lectures, OnItemClickListener listener) {
        this.lectures = lectures;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LectureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lecture, parent, false);
        return new LectureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LectureViewHolder holder, int position) {
        String lecture = lectures.get(position);
        holder.lectureTitleTextView.setText(lecture);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(lecture));
    }

    @Override
    public int getItemCount() {
        return lectures.size();
    }

    public static class LectureViewHolder extends RecyclerView.ViewHolder {
        TextView lectureTitleTextView;

        public LectureViewHolder(View itemView) {
            super(itemView);
            lectureTitleTextView = itemView.findViewById(R.id.lectureTitleTextView);
        }
    }
}
