package com.example.social_network_friendy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class USER_FINDING_ADAPTER extends RecyclerView.Adapter<USER_FINDING_ADAPTER.UserViewHolder> {

    private final ArrayList<String> userList;
    private final OnItemClickListener listener;

    public USER_FINDING_ADAPTER(ArrayList<String> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_userfinding, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        String username = userList.get(position);
        holder.usernameTextView.setText(username);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(username)); // Sự kiện click
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String username);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvmeofinding);
        }
    }
}
