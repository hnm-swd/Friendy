package com.example.social_network_friendy;

import android.os.Bundle;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import com.google.firebase.database.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class NotificationActivity extends Activity {

    private DatabaseReference notificationsRef;
    private LinearLayout notificationContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationContainer = findViewById(R.id.notificationContainer);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(userId);

            // Lắng nghe sự thay đổi trong dữ liệu thông báo
            notificationsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    notificationContainer.removeAllViews();
                    for (DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                        String username = notificationSnapshot.child("username").getValue(String.class);
                        String message = notificationSnapshot.child("message").getValue(String.class);

                        addNotificationToView(username, message);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Xử lý lỗi tại đây
                }
            });
        }
    }

    private void addNotificationToView(String username, String message) {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout notificationView = (LinearLayout) inflater.inflate(R.layout.item_notification, notificationContainer, false);

        TextView usernameTextView = notificationView.findViewById(R.id.usernameText);
        TextView messageTextView = notificationView.findViewById(R.id.commentText);

        usernameTextView.setText(username);
        messageTextView.setText(message);

        notificationContainer.addView(notificationView);
    }
}
