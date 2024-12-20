package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.social_network_friendy.databinding.ActivityNewsFeedBinding;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//
//public class NewsFeedActivity extends Activity {
//    private RecyclerView recyclerView;
//    private PostAdapter postAdapter;
//    private List<Post> postList;
//    private DatabaseReference postsRef;
//    private ActivityNewsFeedBinding binding; // Khai báo biến binding
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater()); // Khởi tạo binding
//        setContentView(binding.getRoot());  // Đặt root view cho activity
//
//        // Gán RecyclerView từ binding
//        recyclerView = binding.postRecyclerView; // Thay đổi theo ID trong XML
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        postList = new ArrayList<>();
//        postAdapter = new PostAdapter(postList);
//        recyclerView.setAdapter(postAdapter);
//
//        // Khởi tạo DatabaseReference
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//
//        // Lắng nghe thay đổi dữ liệu trong Firebase
//        postsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postList.clear();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Post post = snapshot.getValue(Post.class);
//                    if (post != null) {
//                        postList.add(post);
//                    }
//                }
//                postAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Xử lý lỗi nếu có
//            }
//        });
//
//        // Chuyển sang màn hình tạo bài viết khi click vào nút "Go Create Post"
//        binding.goCreatePost.setOnClickListener(view -> {
//            startActivity(new Intent(NewsFeedActivity.this, PostActivity.class));
//        });
//    }
//}

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.social_network_friendy.databinding.ActivityNewsFeedBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFeedActivity extends Activity {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private ActivityNewsFeedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        recyclerView = binding.postRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this,postList); // Pass 'this' as the context
        recyclerView.setAdapter(postAdapter);

        // Initialize Firebase database reference
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Query to get only the last 10 posts ordered by timestamp (ascending)
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
}
