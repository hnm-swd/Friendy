
package com.example.social_network_friendy;

import android.app.Activity;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileActivity extends Activity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private DatabaseReference postsRef;

    private TextView usernameTextView;
    private TextView followersCountTextView; // TextView mới để hiển thị số lượng người theo dõi
    private CircleImageView avatarImageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myprofile);

        // Firebase references
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // Initialize UI elements
        recyclerView = findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usernameTextView = findViewById(R.id.usernameTextView);
        followersCountTextView = findViewById(R.id.followersCount);
        avatarImageView = findViewById(R.id.avatarImageView);

        // Initialize post list and adapter
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            // Fetch user information
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String username = task.getResult().child("username").getValue(String.class);
                    String avatarBase64 = task.getResult().child("avatar").getValue(String.class);

                    if (username != null && !username.isEmpty()) {
                        usernameTextView.setText(username);
                        fetchPostsForUser(username);
                        loadFollowersCount(username); // Gọi hàm load số lượng followers
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

        // Edit profile button
        findViewById(R.id.editProfileButton).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });
        // NewsFeed screen
        findViewById(R.id.icHome).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, NewsFeedActivity.class);
            startActivity(intent);
        });
        // Exit button
        findViewById(R.id.exit).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        // Post screen
        findViewById(R.id.post).setOnClickListener(v -> {
            Intent intent = new Intent(MyProfileActivity.this, PostActivity.class);
            startActivity(intent);
        });

        // Set avatar click listener
        avatarImageView.setOnClickListener(v -> chooseImage());

        // Load user profile details
        loadUserProfile();
    }

    private void fetchPostsForUser(String username) {
        postsRef.orderByChild("username").equalTo(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyProfileActivity.this, "Error loading posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFollowersCount(String username) {
        DatabaseReference followersRef = FirebaseDatabase.getInstance().getReference("followers").child(username);

        // Đếm số lượng followers
        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                followersCountTextView.setText(count + " Followers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                followersCountTextView.setText("0 Followers");
                Toast.makeText(MyProfileActivity.this, "Failed to load followers count", Toast.LENGTH_SHORT).show();
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

            // Load avatar from "avatars" node
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




    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                avatarImageView.setImageBitmap(bitmap);

                // Encode and upload to Firebase
                String avatarBase64 = encodeBitmapToBase64(bitmap);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("avatars").child(user.getUid());
                    userRef.child("avatar").setValue(avatarBase64).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
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


