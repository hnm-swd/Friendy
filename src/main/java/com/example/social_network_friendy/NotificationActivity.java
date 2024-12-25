package com.example.social_network_friendy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    private DatabaseReference notificationsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications); // Gán RecyclerView từ layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Gán LayoutManager

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);
        loadNotifications();

        // Lấy user ID của người dùng đã đăng nhập
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);



        // Lắng nghe thay đổi trong notifications của người dùng
//        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()); // Nhận thông báo cho người dùng hiện tại
//        notificationsRef.addChildEventListener(new ChildEventListener() {
        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear(); // Xóa danh sách cũ
                for (DataSnapshot data : snapshot.getChildren()) {
                    Notification notification = data.getValue(Notification.class);
                    notificationList.add(notification); // Thêm thông báo vào danh sách
                }
                adapter.notifyDataSetChanged(); // Làm mới giao diện
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotificationActivity", "Failed to load notifications: " + error.getMessage());
            }
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
//                Notification notification = dataSnapshot.getValue(Notification.class);
//                if (notification != null) {
//                    notificationList.add(notification); // Thêm thông báo vào danh sách
//
//                    adapter.notifyItemInserted(notificationList.size() - 1); // Cập nhật RecyclerView
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {}

//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//            }

//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        // click để vào trang MyProfile
        ImageView myProfile = findViewById(R.id.myprofile);
        findViewById(R.id.myprofile).setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, MyProfileActivity.class);
            startActivity(intent);
        });

        //Sự kiện nhấn nút Home
        findViewById(R.id.imgHome).setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, NewsFeedActivity.class);
            startActivity(intent);
        });
        //Sự kiện nhấn nút thêm bài viết
        ImageView imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, PostActivity.class);
            startActivity(intent);
        });
        //Sự kiện nhấn nút tìm kiếm
        ImageView ImgSearch = findViewById(R.id.ImgSearch);
        ImgSearch.setOnClickListener(v -> {
            Intent intent = new Intent(NotificationActivity.this, Search.class);
            startActivity(intent);
        });

    }

    private void loadNotifications() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(currentUserId);

        notificationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    notificationList.add(notification);
                    Log.d("NotificationActivity", "Added notification: " + notification.getMessage());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("NotificationActivity", "Failed to load notifications: " + error.getMessage());
            }
        });
    }
}