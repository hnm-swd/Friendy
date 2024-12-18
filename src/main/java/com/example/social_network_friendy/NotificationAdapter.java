package com.example.social_network_friendy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        // Lấy đối tượng NotificationItem tương ứng với vị trí
        NotificationItem notification = notificationList.get(position);

        // Thiết lập giá trị cho các view trong item
        holder.usernameTextView.setText(notification.getUsername());
        holder.commentTextView.setText(notification.getComment());
        holder.likeCountTextView.setText(String.valueOf(notification.getLikeCount()));
        holder.avatarImageView.setImageResource(notification.getAvatarResId());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    // ViewHolder cho RecyclerView
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView, commentTextView, likeCountTextView;
        public ImageView avatarImageView, likeIconImageView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout
            usernameTextView = itemView.findViewById(R.id.usernameText);
            commentTextView = itemView.findViewById(R.id.commentText);
            likeCountTextView = itemView.findViewById(R.id.likeCount);
            avatarImageView = itemView.findViewById(R.id.userAvatar);
            likeIconImageView = itemView.findViewById(R.id.likeIcon);
        }
    }
}
