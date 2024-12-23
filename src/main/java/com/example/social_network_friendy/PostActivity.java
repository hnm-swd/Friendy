package com.example.social_network_friendy;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

public class PostActivity extends Activity {

    private EditText editTextPostContent;
    private Button btnPost;
    private ImageView btnSelectImage;
    private LinearLayout imageContainer;
    private TextView usernameTextView; // TextView to display the username

    private ArrayList<Uri> imageUris = new ArrayList<>(); // Lưu trữ danh sách ảnh
    private DatabaseReference postsRef;

    private static final int STORAGE_PERMISSION_REQUEST = 1;
    private static final int IMAGE_PICK_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_activity_layout);

        // Firebase
        postsRef = FirebaseDatabase.getInstance().getReference("posts");

        // UI components
        editTextPostContent = findViewById(R.id.tvtContent);
        btnPost = findViewById(R.id.btnPost);
        btnSelectImage = findViewById(R.id.imgUpload);
        imageContainer = findViewById(R.id.imageContainer);
        usernameTextView = findViewById(R.id.tvtUsername);
        // Initialize the TextView

        // Initial state
        updatePostButtonState();

        // Lắng nghe thay đổi nội dung trong EditText
        editTextPostContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updatePostButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Fetch and display the username of the logged-in user
// Fetch and display the username from Firebase Realtime Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    String username = task.getResult().child("username").getValue(String.class);
                    if (username != null && !username.isEmpty()) {
                        usernameTextView.setText(username);
                    } else {
                        usernameTextView.setText("Unknown User");
                    }
                } else {
                    usernameTextView.setText("Failed to fetch username");
                }
            }).addOnFailureListener(e -> {
                usernameTextView.setText("Error fetching username");
            });
        } else {
            usernameTextView.setText("Not logged in");
        }
        //Sự kiện nhấn nút quay trở về
        ImageView btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
                    Intent intent = new Intent(PostActivity.this, NewsFeedActivity.class);
                    startActivity(intent);
        });



        // Chọn ảnh
        btnSelectImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
            } else {
                openImagePicker();
            }
        });

        // Đăng bài viết
        btnPost.setOnClickListener(v -> {
            String content = editTextPostContent.getText().toString().trim();
            if (!imageUris.isEmpty()) {
                uploadImagesAndPost(content);
            } else {
                postNewContent(content, null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void updatePostButtonState() {
        String content = editTextPostContent.getText().toString().trim();
        boolean isContentNotEmpty = !content.isEmpty();
        boolean hasImages = !imageUris.isEmpty();

        // Kiểm tra và bật/tắt nút btnPost
        btnPost.setEnabled(isContentNotEmpty || hasImages);
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                    addImageToContainer(imageUri);
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                addImageToContainer(imageUri);
            }
            updatePostButtonState(); // Cập nhật trạng thái nút
        }
    }

    private void addImageToContainer(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
        imageView.setPadding(8, 8, 8, 8);
        imageView.setImageURI(imageUri);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageContainer.addView(imageView);
    }

    private String encodeBitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    //    private void postNewContent(String content, ArrayList<String> base64Images) {
//        String postId = postsRef.push().getKey();
//        if (postId == null) {
//            Log.e("FirebaseError", "Không thể tạo postId");
//            Toast.makeText(this, "Không thể tạo postId", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String username = (user != null) ? user.getDisplayName() : "Unknown User";
//        String timeAgo = DateUtils.getRelativeTimeSpanString(System.currentTimeMillis()).toString();
//        int likeCount = 0;
//        int commentCount = 0;
//
//        Post newPost = new Post(postId, username, content, timeAgo, base64Images, likeCount, commentCount);
//
//        postsRef.child(postId).setValue(newPost)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Bài viết đã được đăng!", Toast.LENGTH_SHORT).show();
//                        // Redirect to NewsFeedActivity after successful post
//                        Intent intent = new Intent(PostActivity.this, NewsFeedActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Log.e("FirebaseError", "Không thể đăng bài", task.getException());
//                        Toast.makeText(this, "Không thể đăng bài!", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("FirebaseError", "Lỗi khi đăng bài", e);
//                    Toast.makeText(this, "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                });
//    }
    private void postNewContent(String content, ArrayList<String> base64Images) {
        String postId = postsRef.push().getKey();
        if (postId == null) {
            Log.e("FirebaseError", "Không thể tạo postId");
            Toast.makeText(this, "Không thể tạo postId", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        // Lấy username từ Realtime Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String username = "Unknown User"; // Giá trị mặc định
                if (snapshot.exists() && snapshot.child("username").getValue() != null) {
                    username = snapshot.child("username").getValue(String.class);
                }

                // Tạo đối tượng bài viết
                long timestamp = System.currentTimeMillis();
                int likeCount = 0;
                int commentCount = 0;

                Post newPost = new Post(postId, username, content, timestamp, base64Images, likeCount, commentCount);

                // Lưu bài viết lên Firebase
                postsRef.child(postId).setValue(newPost)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(PostActivity.this, "Bài viết đã được đăng!", Toast.LENGTH_SHORT).show();
                                // Chuyển hướng về NewsFeedActivity
                                startActivity(new Intent(PostActivity.this, NewsFeedActivity.class));
                                finish();
                            } else {
                                Log.e("FirebaseError", "Không thể đăng bài", task.getException());
                                Toast.makeText(PostActivity.this, "Không thể đăng bài!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(PostActivity.this, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImagesAndPost(String content) {
        Toast.makeText(this, "Uploading images...", Toast.LENGTH_SHORT).show();

        // Kiểm tra nếu không có ảnh nào được chọn
        if (imageUris.isEmpty()) {
            postNewContent(content, null);
            return;
        }

        ArrayList<String> base64Images = new ArrayList<>();
        int[] remainingImages = {imageUris.size()};

        for (Uri imageUri : imageUris) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                String base64Image = encodeBitmapToBase64(bitmap);
                base64Images.add(base64Image);

                remainingImages[0]--;
                if (remainingImages[0] == 0 && base64Images.size() == imageUris.size()) {
                    postNewContent(content, base64Images);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to convert image", Toast.LENGTH_SHORT).show();
            }
        }
    }


}