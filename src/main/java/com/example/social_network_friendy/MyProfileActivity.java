package com.example.social_network_friendy;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends Activity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;
    private Bitmap avatarBitmap;
    private TextView usernameTextView;
    private TextView followersCountTextView;
    private CircleImageView avatarImageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        // Firebase references
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        recyclerView = findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usernameTextView = findViewById(R.id.usernameTextView);
        avatarImageView = findViewById(R.id.avatarImageView);
        followersCountTextView = findViewById(R.id.followersCount);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String username = task.getResult().child("username").getValue(String.class);
                    String avatarBase64 = task.getResult().child("avatar").getValue(String.class);

                    if (username != null && !username.isEmpty()) {
                        usernameTextView.setText(username);
                        fetchPosts(username);
                        loadFollowersCount(username);
                    } else {
                        usernameTextView.setText("Unknown User");
                    }

                    if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                        Bitmap avatarBitmap = decodeBase64ToBitmap(avatarBase64);
                        avatarImageView.setImageBitmap(avatarBitmap);
                    } else {
                        avatarImageView.setImageResource(R.drawable.img_profile_avatar);
                    }
                } else {
                    usernameTextView.setText("Failed to fetch username");
                }
            }).addOnFailureListener(e -> usernameTextView.setText("Error fetching username"));
        } else {
            usernameTextView.setText("Not logged in");
        }

        findViewById(R.id.editProfileButton).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.imgHome).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, NewsFeedActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.exit).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.imgAdd).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, PostActivity.class);
            startActivity(intent);
        });

        avatarImageView.setOnClickListener(v -> chooseImage());

        loadUserProfile();
        avatarImageView.setOnClickListener(v -> {
            // Tạo một hộp thoại với hai tùy chọn
            AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
            builder.setTitle("Choose an option")
                    .setItems(new CharSequence[]{"Choose new avatar", "Remove avatar"}, (dialog, which) -> {
                        switch (which) {
                            case 0: // Chọn ảnh mới
                                chooseImage();
                                break;
                            case 1: // Gỡ ảnh avatar
                                removeAvatar();
                                break;
                        }
                    })
                    .show();
        });
    }

    private void loadFollowersCount(String username) {
        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference("followers").child(username);

        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                followersCountTextView.setText(count + " Followers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
                followersCountTextView.setText("0 Followers");
                Toast.makeText(MyProfileActivity.this, "Failed to load followers count", Toast.LENGTH_SHORT).show();
            }
        });
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(user.getUid());

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        TextView bioTextView = findViewById(R.id.bioTextView);
                        TextView birthDateTextView = findViewById(R.id.birthDateTextView);
                        TextView locationTextView = findViewById(R.id.locationTextView);

                        // Bio
                        String bio = snapshot.child("bio").getValue(String.class);
                        if (bio != null && !bio.trim().isEmpty()) {
                            bioTextView.setText(bio);
                            bioTextView.setVisibility(View.VISIBLE);
                        } else {
                            bioTextView.setVisibility(View.GONE);
                        }

                        // Birth Date
                        String birthDate = snapshot.child("birthDate").getValue(String.class);
                        if (birthDate != null && !birthDate.trim().isEmpty()) {
                            birthDateTextView.setText(birthDate);
                            birthDateTextView.setVisibility(View.VISIBLE);
                        } else {
                            birthDateTextView.setVisibility(View.GONE);
                        }

                        // Location
                        String location = snapshot.child("location").getValue(String.class);
                        if (location != null && !location.trim().isEmpty()) {
                            locationTextView.setText(location);
                            locationTextView.setVisibility(View.VISIBLE);
                        } else {
                            locationTextView.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            });

            DatabaseReference avatarRef = FirebaseDatabase.getInstance()
                    .getReference("avatars")
                    .child(user.getUid());

            avatarRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String avatarBase64 = snapshot.child("avatar").getValue(String.class);
                    if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                        Bitmap avatarBitmap = decodeBase64ToBitmap(avatarBase64);
                        avatarImageView.setImageBitmap(avatarBitmap);
                    } else {
                        avatarImageView.setImageResource(R.drawable.img_profile_avatar);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MyProfileActivity.this, "Failed to load avatar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void updatePostsAvatar(String avatarBase64) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");

            // Update all posts for the current user
            postsRef.orderByChild("username").equalTo(user.getDisplayName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // Get the post ID and update the avatar
                        String postId = postSnapshot.getKey();
                        postsRef.child(postId).child("avatar").setValue(avatarBase64);
                    }

                    // Sau khi cập nhật avatar cho tất cả các bài đăng, làm mới lại RecyclerView
                    postAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MyProfileActivity.this, "Failed to update posts' avatar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void removeAvatar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Xóa avatar trong Firebase
            DatabaseReference avatarRef = FirebaseDatabase.getInstance()
                    .getReference("avatars")
                    .child(user.getUid());

            avatarRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Nếu xóa thành công, cập nhật avatar về ảnh mặc định
                    avatarImageView.setImageResource(R.drawable.img_profile_avatar);
                    // Cập nhật avatar trong tất cả các bài đăng của người dùng
                    updatePostsAvatar(null);
                    Toast.makeText(MyProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyProfileActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatarImageView.setImageBitmap(bitmap);

                String avatarBase64 = encodeBitmapToBase64(bitmap);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("avatars").child(user.getUid());
                    userRef.child("avatar").setValue(avatarBase64).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Update the avatar in all posts
                            updatePostsAvatar(avatarBase64);
                            Toast.makeText(MyProfileActivity.this, "Avatar updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyProfileActivity.this, "Failed to update avatar", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToBitmap(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


}

