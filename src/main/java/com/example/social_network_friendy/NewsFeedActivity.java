package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.social_network_friendy.databinding.ActivityNewsFeedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NewsFeedActivity extends Activity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private ActivityNewsFeedBinding binding;
    private DatabaseReference usersRef; // Tham chiếu đến nút "users" trong Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth và Database Reference
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Lấy người dùng hiện tại
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Lấy tên từ Firebase Realtime Database
            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Lấy đối tượng User từ Firebase
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        // Gán tên vào TextView có ID "appTitle"
                        binding.edtuser.setText(user.getUsername());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu không thể lấy dữ liệu
                }
            });
        }

        // Initialize RecyclerView
        recyclerView = binding.postRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this,postList); // Pass 'this' as the context
        recyclerView.setAdapter(postAdapter);

        // Initialize Firebase database reference
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        Query query = postsRef.orderByChild("timestamp").limitToLast(10);

        // Add a ValueEventListener to get data from Firebase
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();  // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }

                // Since the posts are ordered by ascending timestamp, the oldest posts are at the end,
                // so we need to reverse the list to show the newest post at the top
                Collections.reverse(postList);

                postAdapter.notifyDataSetChanged();  // Notify adapter of data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if the data retrieval is cancelled
            }
        });

        // Navigate to the "Create Post" activity when the button is clicked
        binding.goCreatePost.setOnClickListener(view -> {
            startActivity(new Intent(NewsFeedActivity.this, PostActivity.class));
        });

        // click để vào trang MyProfile
        ImageView myProfile = findViewById(R.id.myprofile);
        findViewById(R.id.myprofile).setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, MyProfileActivity.class);
            startActivity(intent);
        });

        //Sự kiện nhấn nút Home
        findViewById(R.id.imgHome).setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, NewsFeedActivity.class);
            startActivity(intent);
        });
        //Sự kiện nhấn nút thêm bài viết
        ImageView imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, PostActivity.class);
            startActivity(intent);
        });
        //Sự kiện nhấn nút tìm kiếm
        ImageView ImgSearch = findViewById(R.id.ImgSearch);
        ImgSearch.setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, Search.class);
            startActivity(intent);
        });
        //Sự kiện nhấn nút hình trái tym
        ImageView icFavorite = findViewById(R.id.icFavorite);
        icFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(NewsFeedActivity.this, NotificationActivity.class);
            startActivity(intent);
        });

    }

    // Method to handle the addition of a new post to the list and update the feed
    public void addNewPost(Post post) {
        // Add the new post at the beginning of the list (top of the feed)
        postList.add(0, post);
        postAdapter.notifyItemInserted(0);  // Notify the adapter that a new item was inserted at position 0
        recyclerView.scrollToPosition(0);  // Scroll the RecyclerView to the top
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Cập nhật lại dữ liệu bài viết từ Firebase (lấy lại số lượt bình luận mới nhất)
            fetchPosts();
        }
    }


    // Method to fetch posts from Firebase
    private void fetchPosts() {
        postsRef.orderByChild("timestamp").limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();  // Clear the list to avoid duplication
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }

                // Reverse the list to show newest posts at the top
                Collections.reverse(postList);
                postAdapter.notifyDataSetChanged();  // Notify the adapter of data changes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if the data retrieval is cancelled
            }
        });
    }
    // Hàm xử lý khi người dùng nhấn thích
    private void likePost(String postId, String postOwnerId) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("likes").child(postId);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            likesRef.child(userId).setValue(true).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendNotification(postId, postOwnerId, currentUser.getDisplayName());
                }
            });
        }
    }

    // Hàm gửi thông báo
    private void sendNotification(String postId, String postOwnerId, String username) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(postOwnerId);

        HashMap<String, Object> notificationData = new HashMap<>();
        notificationData.put("postId", postId);
        notificationData.put("username", username);
        notificationData.put("message", "đã thích bài viết của bạn");
        notificationData.put("timestamp", System.currentTimeMillis());

        notificationsRef.push().setValue(notificationData);
    }


}