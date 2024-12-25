package com.example.social_network_friendy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;

    public NotificationAdapter(List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        Log.d("NotificationAdapter", "Creating new view holder");
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        Log.d("NotificationAdapter", "Size of notification list: " + notificationList.size());

        holder.tvMessage.setText(notification.getMessage());
        holder.tvTimestamp.setText(notification.getTimestamp().toString());

        // Tùy thuộc vào loại thông báo, bạn có thể thay đổi icon hoặc hành động
        if ("like".equals(notification.getType())) {
            holder.imgIcon.setImageResource(R.drawable.ic_favorite);  // Thay đổi theo loại thông báo
        } else if ("comment".equals(notification.getType())) {
            holder.imgIcon.setImageResource(R.drawable.ic_comment);
        }
//        } else if ("follow".equals(notification.getType())) {
//            holder.imgIcon.setImageResource(R.drawable);
//        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
//        TextView tvNotificationContent, tvNotificationTime;
        TextView tvMessage, tvTimestamp;
        ImageView imgIcon;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            imgIcon = itemView.findViewById(R.id.imgAvatar);
        }
    }
}