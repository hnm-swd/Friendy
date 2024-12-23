package com.example.social_network_friendy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comment> commentList;

    public CommentsAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.usernameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getCommentText());
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, commentTextView;  // Make sure this is defined

        public CommentViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);  // Correctly initialize
            commentTextView = itemView.findViewById(R.id.commentTextView);  // Correctly initialize

        }
    }
}

