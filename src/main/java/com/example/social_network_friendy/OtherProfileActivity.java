package com.example.social_network_friendy;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OtherProfileActivity extends Activity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private DatabaseReference followersRef;
    private TextView usernameTextView;
    private TextView followersCountTextView;
    private Button followButton;
    private String username;
    private boolean isFollowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherprofile);

        postsRef = FirebaseDatabase.getInstance().getReference("posts");
        followersRef = FirebaseDatabase.getInstance().getReference("followers");

        recyclerView = findViewById(R.id.postRecyclerView);
        usernameTextView = findViewById(R.id.usernameTextView);
        followersCountTextView = findViewById(R.id.followersCount);
        followButton = findViewById(R.id.followButton);

        username = getIntent().getStringExtra("username");
        if (username != null) {
            usernameTextView.setText(username);
        }

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);

        fetchPosts(username);
        loadFollowersCount(username);
        setupFollowButton(username);

        findViewById(R.id.backIcon).setOnClickListener(v -> finish());
        findViewById(R.id.backText).setOnClickListener(v -> finish());
        loadUserProfile();
    }

    private void fetchPosts(String username) {
        postsRef.orderByChild("username").equalTo(username)  // Lọc theo username
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        postList.clear();  // Xóa danh sách cũ để tránh trùng lặp
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (post != null) {
                                postList.add(post);  // Thêm bài viết vào danh sách
                            }
                        }

                        // Sắp xếp lại theo thứ tự thời gian (mới nhất lên đầu)
                        Collections.sort(postList, new Comparator<Post>() {
                            @Override
                            public int compare(Post post1, Post post2) {
                                return Long.compare(post2.getTimestamp(), post1.getTimestamp());
                            }
                        });

                        postAdapter.notifyDataSetChanged();  // Thông báo cho adapter về dữ liệu mới
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Xử lý lỗi nếu việc truy xuất dữ liệu bị hủy
                    }
                });
    }


    private void loadUserProfile() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    TextView bioTextView = findViewById(R.id.bioTextView);
                    TextView birthDateTextView = findViewById(R.id.birthDateTextView);
                    TextView locationTextView = findViewById(R.id.locationTextView);

                    String bio = userSnapshot.child("bio").getValue(String.class);
                    if (bio != null && !bio.trim().isEmpty()) {
                        bioTextView.setText(bio);
                        bioTextView.setVisibility(View.VISIBLE);
                    } else {
                        bioTextView.setVisibility(View.GONE);
                    }

                    String birthDate = userSnapshot.child("birthDate").getValue(String.class);
                    if (birthDate != null && !birthDate.trim().isEmpty()) {
                        birthDateTextView.setText(birthDate);
                        birthDateTextView.setVisibility(View.VISIBLE);
                    } else {
                        birthDateTextView.setVisibility(View.GONE);
                    }

                    String location = userSnapshot.child("location").getValue(String.class);
                    if (location != null && !location.trim().isEmpty()) {
                        locationTextView.setText(location);
                        locationTextView.setVisibility(View.VISIBLE);
                    } else {
                        locationTextView.setVisibility(View.GONE);
                    }

                    String profileImageUrl = userSnapshot.child("profileImageUrl").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(OtherProfileActivity.this)
                                .load(profileImageUrl)
                                .into((ImageView) findViewById(R.id.avatarImageView));
                    }

                    DatabaseReference avatarRef = FirebaseDatabase.getInstance()
                            .getReference("avatars")
                            .child(userSnapshot.getKey());

                    avatarRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String avatarBase64 = snapshot.child("avatar").getValue(String.class);
                            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                                Bitmap avatarBitmap = decodeBase64ToBitmap(avatarBase64);
                                ((ImageView) findViewById(R.id.avatarImageView)).setImageBitmap(avatarBitmap);
                            } else {
                                ((ImageView) findViewById(R.id.avatarImageView)).setImageResource(R.drawable.img_profile_avatar);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(OtherProfileActivity.this, "Failed to load avatar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OtherProfileActivity.this, "Failed to load user profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void loadFollowersCount(String username) {
        followersRef.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                followersCountTextView.setText(count + " Followers");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OtherProfileActivity.this, "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFollowButton(String usernameToFollow) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        String currentUserId = currentUser.getUid();

        followersRef.child(usernameToFollow).child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isFollowing = true;
                    followButton.setText("Đang theo dõi");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    followButton.setTextColor(Color.BLACK);
                } else {
                    isFollowing = false;
                    followButton.setText("Theo dõi");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    followButton.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OtherProfileActivity.this, "Lỗi khi tải dữ liệu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        followButton.setOnClickListener(v -> {
            if (!isFollowing) {
                followersRef.child(usernameToFollow).child(currentUserId).setValue(true).addOnSuccessListener(aVoid -> {
                    isFollowing = true;
                    followButton.setText("Đang theo dõi");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                    followButton.setTextColor(Color.BLACK);
                    updateFollowersCount(usernameToFollow);
                });
            } else {
                followersRef.child(usernameToFollow).child(currentUserId).removeValue().addOnSuccessListener(aVoid -> {
                    isFollowing = false;
                    followButton.setText("Theo dõi");
                    followButton.setBackgroundTintList(ColorStateList.valueOf(Color.BLACK));
                    followButton.setTextColor(Color.WHITE);
                    updateFollowersCount(usernameToFollow);
                });
            }
        });
    }

    private void updateFollowersCount(String usernameToFollow) {
        followersRef.child(usernameToFollow).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long followersCount = snapshot.getChildrenCount();
                followersCountTextView.setText(followersCount + " Followers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OtherProfileActivity.this, "Error updating followers count", Toast.LENGTH_SHORT).show();
            }
        });
    }
}