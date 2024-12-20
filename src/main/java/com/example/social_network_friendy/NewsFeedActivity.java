//package com.example.social_network_friendy;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.example.social_network_friendy.databinding.ActivityNewsFeedBinding;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.DataSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class NewsFeedActivity extends Activity {
//    ActivityNewsFeedBinding binding;
//    private RecyclerView recyclerView;
//    private PostAdapter postAdapter;
//    private List<Post> postList;
//    private DatabaseReference postsRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        recyclerView = findViewById(R.id.recyclerView);  // assuming you have a RecyclerView with this ID
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        postList = new ArrayList<>();
//        postAdapter = new PostAdapter(postList);
//        recyclerView.setAdapter(postAdapter);
//
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//
//        // Lắng nghe thay đổi dữ liệu trong Firebase
//        Query postsQuery = postsRef.orderByChild("timestamp").limitToLast(10);
//        postsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                postList.clear();  // Xóa dữ liệu cũ trước khi cập nhật
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Post post = snapshot.getValue(Post.class);
//                    if (post != null) {
//                        postList.add(post); // Thêm bài viết vào danh sách
//                    }
//                }
//                postAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle possible errors
//            }
//        });
//
//        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(NewsFeedActivity.this, PostActivity.class));
//            }
//        });
//    }
//}

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

public class NewsFeedActivity extends Activity {
    ActivityNewsFeedBinding binding;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;

    // Biến để điều chỉnh số lượng bài viết hiển thị
    private int postLimit = 10;  // Mặc định là 10 bài viết gần đây nhất

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_news_feed);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recyclerView);  // assuming you have a RecyclerView with this ID
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);

        // Khởi tạo DatabaseReference
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Lắng nghe thay đổi dữ liệu trong Firebase
        Query postsQuery = postsRef.orderByChild("timestamp").limitToLast(postLimit);  // Sử dụng postLimit ở đây
        postsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();  // Xóa dữ liệu cũ trước khi cập nhật
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post); // Thêm bài viết vào danh sách
                    }
                }

                // Đảo ngược danh sách để hiển thị bài viết mới nhất lên đầu
                Collections.reverse(postList);

                // Cập nhật RecyclerView
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });

        // Chuyển sang màn hình tạo bài viết khi click vào nút "Go Create Post"
        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsFeedActivity.this, PostActivity.class));
            }
        });
    }
}
