////package com.example.social_network_friendy;
////
////import android.app.Activity;
////import android.content.Intent;
////import android.os.Bundle;
////import android.view.View;
////import androidx.recyclerview.widget.LinearLayoutManager;
////import androidx.recyclerview.widget.RecyclerView;
////import com.example.social_network_friendy.databinding.ActivityNewsFeedBinding;
////import com.google.firebase.database.DatabaseError;
////import com.google.firebase.database.DatabaseReference;
////import com.google.firebase.database.FirebaseDatabase;
////import com.google.firebase.database.Query;
////import com.google.firebase.database.ValueEventListener;
////import com.google.firebase.database.DataSnapshot;
////
////import java.util.ArrayList;
////import java.util.List;
////
////public class NewsFeedActivity extends Activity {
////    ActivityNewsFeedBinding binding;
////    private RecyclerView recyclerView;
////    private PostAdapter postAdapter;
////    private List<Post> postList;
////    private DatabaseReference postsRef;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        binding = ActivityNewsFeedBinding.inflate(getLayoutInflater());
////        setContentView(binding.getRoot());
////
////        recyclerView = findViewById(R.id.recyclerView);  // assuming you have a RecyclerView with this ID
////        recyclerView.setLayoutManager(new LinearLayoutManager(this));
////        postList = new ArrayList<>();
////        postAdapter = new PostAdapter(postList);
////        recyclerView.setAdapter(postAdapter);
////
////        postsRef = FirebaseDatabase.getInstance().getReference("posts");
////
////        // Lắng nghe thay đổi dữ liệu trong Firebase
////        Query postsQuery = postsRef.orderByChild("timestamp").limitToLast(10);
////        postsQuery.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                postList.clear();  // Xóa dữ liệu cũ trước khi cập nhật
////                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                    Post post = snapshot.getValue(Post.class);
////                    if (post != null) {
////                        postList.add(post); // Thêm bài viết vào danh sách
////                    }
////                }
////                postAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                // Handle possible errors
////            }
////        });
////
////        binding.goCreatePost.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                startActivity(new Intent(NewsFeedActivity.this, PostActivity.class));
////            }
////        });
////    }
////}
//
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
//import java.util.Collections;
//import java.util.List;
//
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
//

//package com.example.social_network_friendy;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Base64;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class NewsFeedActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private DatabaseReference postsRef;
//    private FirebaseRecyclerAdapter<Post, PostViewHolder> adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news_feed);
//
//        recyclerView = findViewById(R.id.postRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//
//        // Tạo FirebaseRecyclerOptions cho adapter
//        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
//                .setQuery(postsRef, Post.class)
//                .build();
//
//        // Khởi tạo adapter
//        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
//                holder.usernameTextView.setText(model.getUsername());
//                holder.contentTextView.setText(model.getContent());
//                holder.timeTextView.setText(model.getTimeAgo());
//
//                // Hiển thị ảnh từ Base64
//                if (model.getImageBase64() != null && !model.getImageBase64().isEmpty()) {
//                    for (String base64Image : model.getImageBase64()) {
//                        Bitmap bitmap = decodeBase64ToBitmap(base64Image);
//                        ImageView imageView = new ImageView(NewsFeedActivity.this);
//                        imageView.setImageBitmap(bitmap);
//                        holder.imageContainer.addView(imageView);
//                    }
//                }
//            }
//
//            @NonNull
//            @Override
//            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return new PostViewHolder(getLayoutInflater().inflate(R.layout.item_post, parent, false));
//            }
//        };
//
//        // Gán adapter cho RecyclerView
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//    }
//
//    // Phương thức giải mã Base64 thành Bitmap
//    private Bitmap decodeBase64ToBitmap(String base64String) {
//        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }
//
//    // ViewHolder cho bài viết
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView usernameTextView, contentTextView, timeTextView;
//        LinearLayout imageContainer;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            usernameTextView = itemView.findViewById(R.id.tvUsername);
//            contentTextView = itemView.findViewById(R.id.tvContent);
//            timeTextView = itemView.findViewById(R.id.tvTime);
//            imageContainer = itemView.findViewById(R.id.imageContainer);
//        }
//    }
//}

//package com.example.social_network_friendy;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class NewsFeedActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private DatabaseReference postsRef;
//    private FirebaseRecyclerAdapter<Post, PostViewHolder> adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_news_feed);
//
//        recyclerView = findViewById(R.id.postRecyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//
//        // Tạo FirebaseRecyclerOptions cho adapter
//        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>()
//                .setQuery(postsRef, Post.class)
//                .build();
//
//        // Khởi tạo adapter
//        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
//                holder.usernameTextView.setText(model.getUsername());
//                holder.contentTextView.setText(model.getContent());
//                holder.timeTextView.setText(model.getTimeAgo());
//
//                // Xóa tất cả hình ảnh trước khi thêm để tránh lỗi lắp hình ảnh
//                holder.imageContainer.removeAllViews();
//
//                // Hiển thị ảnh từ Base64
//                if (model.getImageBase64() != null && !model.getImageBase64().isEmpty()) {
//                    for (String base64Image : model.getImageBase64()) {
//                        try {
//                            Bitmap bitmap = decodeBase64ToBitmap(base64Image);
//                            if (bitmap != null) {
//                                ImageView imageView = new ImageView(NewsFeedActivity.this);
//                                imageView.setImageBitmap(bitmap);
//                                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                                holder.imageContainer.addView(imageView);
//                            }
//                        } catch (IllegalArgumentException e) {
//                            e.printStackTrace(); // Log lỗi và bỏ qua hình ảnh sai
//                        }
//                    }
//                }
//            }
//
//            @NonNull
//            @Override
//            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
//                return new PostViewHolder(view);
//            }
//        };
//
//        // Gán adapter cho RecyclerView
//        recyclerView.setAdapter(adapter);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (adapter != null) {
//            adapter.startListening();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (adapter != null) {
//            adapter.stopListening();
//        }
//    }
//
//    // Phương thức giải mã Base64 thành Bitmap
//    private Bitmap decodeBase64ToBitmap(String base64String) {
//        try {
//            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace(); // Log lỗi và bỏ qua ảnh sai
//            return null;
//        }
//    }
//
//    // ViewHolder cho bài viết
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView usernameTextView, contentTextView, timeTextView;
//        LinearLayout imageContainer;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            usernameTextView = itemView.findViewById(R.id.tvUsername);
//            contentTextView = itemView.findViewById(R.id.tvContent);
//            timeTextView = itemView.findViewById(R.id.tvTime);
//            imageContainer = itemView.findViewById(R.id.imageContainer);
//        }
//    }
//}

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
