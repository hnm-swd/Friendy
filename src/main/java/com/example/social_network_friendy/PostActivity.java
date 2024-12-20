//package com.example.social_network_friendy;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//public class PostActivity extends Activity {
//
//    private EditText editTextPostContent;
//    private Button btnPost;
//    private ImageView btnSelectImage;
//    private LinearLayout imageContainer;
//
//    private ArrayList<Uri> imageUris = new ArrayList<>(); // Lưu trữ danh sách ảnh
//    private DatabaseReference postsRef;
//    private StorageReference storageRef;
//
//    private static final int STORAGE_PERMISSION_REQUEST = 1;
//    private static final int IMAGE_PICK_REQUEST = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addpost_activity_layout);
//
//        // Firebase
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//        storageRef = FirebaseStorage.getInstance().getReference("post_images");
//        // Lấy file image từ bộ nhớ của thiết bị
//        Uri file = Uri.fromFile(new File("/path/to/my_image.jpg"));
//
//        // Thêm tên file vào tham chiếu, ví dụ "my_image.jpg"
//        StorageReference imageRef = storageRef.child("my_image.jpg");
//
//        // Tải lên file
//        imageRef.putFile(file)
//                .addOnSuccessListener(taskSnapshot -> {
//                    // Xử lý thành công
//                    Log.d("FirebaseStorage", "File uploaded successfully.");
//                })
//                .addOnFailureListener(e -> {
//                    // Xử lý lỗi
//                    Log.e("FirebaseStorage", "Error uploading file", e);
//                    e.printStackTrace();
//                });
//        // UI components
//        editTextPostContent = findViewById(R.id.tvtContent);
//        btnPost = findViewById(R.id.btnPost);
//        btnSelectImage = findViewById(R.id.imgUpload);
//        imageContainer = findViewById(R.id.imageContainer);
//
//        // Chọn ảnh
//        btnSelectImage.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
//            } else {
//                openImagePicker();
//            }
//        });
//
//        // Đăng bài viết
//        btnPost.setOnClickListener(v -> {
//            String content = editTextPostContent.getText().toString().trim();
//            if (!imageUris.isEmpty()) {
//                uploadImagesAndPost(content);
//            } else {
//                postNewContent(content, null);
//            }
//        });
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Nếu quyền được cấp, mở trình chọn ảnh
//                openImagePicker();
//            } else {
//                // Nếu quyền bị từ chối, hiển thị thông báo
//                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    // Mở trình chọn ảnh
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(intent, IMAGE_PICK_REQUEST);
//    }
//
//    // Xử lý kết quả chọn ảnh
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
//            if (data.getClipData() != null) {
//                // Chọn nhiều ảnh
//                int count = data.getClipData().getItemCount();
//                for (int i = 0; i < count; i++) {
//                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    imageUris.add(imageUri);
//                    addImageToContainer(imageUri);
//                }
//            } else if (data.getData() != null) {
//                // Chọn một ảnh
//                Uri imageUri = data.getData();
//                imageUris.add(imageUri);
//                addImageToContainer(imageUri);
//            }
//        }
//    }
//
//    // Thêm ảnh vào thanh ngang
//    private void addImageToContainer(Uri imageUri) {
//        ImageView imageView = new ImageView(this);
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
//        imageView.setPadding(8, 8, 8, 8);
//        imageView.setImageURI(imageUri);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        imageContainer.addView(imageView);
//    }
//
//    // Tải ảnh lên Firebase và tạo bài viết
//    private void addImagesToPost(ArrayList<String> imageUrls) {
//        LinearLayout imageContainer = findViewById(R.id.imageContainer);
//
//        // Xóa các ảnh cũ (nếu có)
//        imageContainer.removeAllViews();
//
//        // Lặp qua tất cả các URL ảnh và thêm chúng vào layout
//        for (String imageUrl : imageUrls) {
//            ImageView imageView = new ImageView(this);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(8, 8, 8, 8);
//            imageView.setLayoutParams(params);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//            // Sử dụng thư viện Glide hoặc Picasso để tải ảnh
//            Glide.with(this)
//                    .load(imageUrl)
//                    .into(imageView);
//
//            // Thêm ImageView vào container
//            imageContainer.addView(imageView);
//        }
//    }
//
//    private void uploadImagesAndPost(String content) {
//        Toast.makeText(this, "Uploading images...", Toast.LENGTH_SHORT).show();
//
//        // Nếu không có ảnh thì chỉ đăng bài mà không cần tải ảnh lên
//        if (imageUris.isEmpty()) {
//            postNewContent(content, null);
//            return;
//        }
//    // Nếu có ảnh, thì bắt đầu tải ảnh lên Firebase
//        ArrayList<String> imageUrls = new ArrayList<>();  // Lưu trữ các URL ảnh
//        int[] remainingImages = {imageUris.size()};  // Biến đếm số ảnh còn lại để kiểm tra khi tải lên hoàn tất
//
//        for (Uri imageUri : imageUris) {
////            String fileName = "images/" + System.currentTimeMillis() + ".jpg";
//            String fileName = "images/" + UUID.randomUUID().toString() + ".jpg";
//            StorageReference fileRef = storageRef.child(fileName);
//            Log.d("DEBUG", "File path: " + fileRef.getPath());
//
//            // Tải ảnh lên Firebase Storage
//            fileRef.putFile(imageUri)
//                    .addOnSuccessListener(taskSnapshot -> {
//                        Log.d("FirebaseStorage","Upload success: " + fileRef.getPath());
//                        // Lấy URL tải xuống sau khi tải lên thành công
//                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            Log.d("FirebaseStorage", "Download URL: " + uri.toString());
//                            imageUrls.add(uri.toString()); // Thêm URL ảnh vào danh sách
//
//                            // Kiểm tra xem tất cả ảnh đã tải lên chưa
//                            remainingImages[0]--;
//                            if (remainingImages[0] == 0 && imageUrls.size() == imageUris.size()) {
//                                postNewContent(content, imageUrls);
//                            }
//
//                        }).addOnFailureListener(e -> {
//                            // Xử lý lỗi khi không thể lấy URL tải xuống
//                            Log.e("FirebaseStorage", "Failed to get download URL", e);
//                        });
//                    })
//                    .addOnFailureListener(e -> {
//                        // Xử lý lỗi khi tải ảnh lên thất bại
//                        Log.e("FirebaseStorage", "Failed to upload file", e);
//                    });
//        }
//    }
//
//
//    // Đăng bài viết
////    private void postNewContent(String content, String imageUri) {
////        // Tạo ID cho bài viết
////        String postId = postsRef.push().getKey();
////        Post newPost = new Post(content, imageUri);
////
////        if (postId != null) {
////            postsRef.child(postId).setValue(newPost).addOnCompleteListener(task -> {
////                if (task.isSuccessful()) {
////                    // Đăng bài thành công
//////                    Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
////                    // Chuyển về màn hình chính sau khi đăng bài thành công
////                    Intent intent = new Intent(PostActivity.this, MainHomeScreen.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                    startActivity(intent);
////                    finish();
////                } else {
////                    // Lỗi khi đăng bài
////                    Toast.makeText(this, "Failed to upload post!", Toast.LENGTH_SHORT).show();
////                }
////            });
////        }
////    }
////    private void postNewContent(String content, ArrayList<String> imageUrls) {
////        String postId = postsRef.push().getKey();
////        if (postId != null) {
////            Map<String, Object> postMap = new HashMap<>();
////            postMap.put("content", content);
////            postMap.put("imageUrls", imageUrls);
////            postMap.put("timestamp", System.currentTimeMillis());
////
////            postsRef.child(postId).setValue(postMap).addOnCompleteListener(task -> {
////                if (task.isSuccessful()) {
////                    Intent intent = new Intent(PostActivity.this, MainHomeScreen.class);
////                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                    startActivity(intent);
////                    finish();
////                } else {
////                    Toast.makeText(this, "Failed to upload post!", Toast.LENGTH_SHORT).show();
////                }
////            });
////        }
////    }
//    private void postNewContent(String content, ArrayList<String> imageUrls) {
//        String postId = postsRef.push().getKey();
//        if (postId == null) {
//            Log.e("FirebaseError", "Không thể tạo postId");
//            Toast.makeText(this, "Không thể tạo postId", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Log.d("DEBUG", "Nội dung bài viết: " + content);
//        Log.d("DEBUG", "URL Ảnh: " + imageUrls.toString());
//        Log.d("DEBUG", "Post ID: " + postId);
//
//        Map<String, Object> postMap = new HashMap<>();
//        postMap.put("content", content);
//        postMap.put("imageUrls", imageUrls);
//        postMap.put("timestamp", System.currentTimeMillis());
//
//        postsRef.child(postId).setValue(postMap)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Bài viết đã được đăng!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(PostActivity.this, MainHomeScreen.class);
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
//
//
//
//}


package com.example.social_network_friendy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//public class PostActivity extends Activity {
//
//    private EditText editTextPostContent;
//    private Button btnPost;
//    private ImageView btnSelectImage;
//    private LinearLayout imageContainer;
//
//    private ArrayList<Uri> imageUris = new ArrayList<>(); // Lưu trữ danh sách ảnh
//    private DatabaseReference postsRef;
//
//    private static final int STORAGE_PERMISSION_REQUEST = 1;
//    private static final int IMAGE_PICK_REQUEST = 2;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.addpost_activity_layout);
//
//        // Firebase
//        postsRef = FirebaseDatabase.getInstance().getReference("posts");
//
//        // UI components
//        editTextPostContent = findViewById(R.id.tvtContent);
//        btnPost = findViewById(R.id.btnPost);
//        btnSelectImage = findViewById(R.id.imgUpload);
//        imageContainer = findViewById(R.id.imageContainer);
//
//        // Chọn ảnh
//        btnSelectImage.setOnClickListener(v -> {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST);
//            } else {
//                openImagePicker();
//            }
//        });
//
//        // Đăng bài viết
//        btnPost.setOnClickListener(v -> {
//            String content = editTextPostContent.getText().toString().trim();
//            if (!imageUris.isEmpty()) {
//                uploadImagesAndPost(content);
//            } else {
//                postNewContent(content, null);
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == STORAGE_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Nếu quyền được cấp, mở trình chọn ảnh
//                openImagePicker();
//            } else {
//                // Nếu quyền bị từ chối, hiển thị thông báo
//                Toast.makeText(this, "Bạn cần cấp quyền để chọn ảnh.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Mở trình chọn ảnh
//    private void openImagePicker() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//        startActivityForResult(intent, IMAGE_PICK_REQUEST);
//    }
//
//    // Xử lý kết quả chọn ảnh
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK) {
//            if (data.getClipData() != null) {
//                // Chọn nhiều ảnh
//                int count = data.getClipData().getItemCount();
//                for (int i = 0; i < count; i++) {
//                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    imageUris.add(imageUri);
//                    addImageToContainer(imageUri);
//                }
//            } else if (data.getData() != null) {
//                // Chọn một ảnh
//                Uri imageUri = data.getData();
//                imageUris.add(imageUri);
//                addImageToContainer(imageUri);
//            }
//        }
//    }
//
//    // Thêm ảnh vào thanh ngang
//    private void addImageToContainer(Uri imageUri) {
//        ImageView imageView = new ImageView(this);
//        imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
//        imageView.setPadding(8, 8, 8, 8);
//        imageView.setImageURI(imageUri);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//
//        imageContainer.addView(imageView);
//    }
//
//    // Chuyển đổi Bitmap thành chuỗi Base64
//    private String encodeBitmapToBase64(Bitmap bitmap) {
//        if (bitmap == null) {
//            return null;
//        }
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(byteArray, Base64.DEFAULT);
//    }
//
//    // Chuyển đổi chuỗi Base64 thành Bitmap
//    private Bitmap decodeBase64ToBitmap(String base64String) {
//        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
//    }
//
//    // Tải ảnh lên Realtime Database và đăng bài
//    private void uploadImagesAndPost(String content) {
//        Toast.makeText(this, "Uploading images...", Toast.LENGTH_SHORT).show();
//
//        // Nếu không có ảnh thì chỉ đăng bài mà không cần tải ảnh lên
//        if (imageUris.isEmpty()) {
//            postNewContent(content, null);
//            return;
//        }
//
//        // Nếu có ảnh, thì bắt đầu tải ảnh lên Firebase
//        ArrayList<String> base64Images = new ArrayList<>();  // Lưu trữ các chuỗi Base64 của ảnh
//        int[] remainingImages = {imageUris.size()};  // Biến đếm số ảnh còn lại để kiểm tra khi tải lên hoàn tất
//
//        for (Uri imageUri : imageUris) {
//            try {
//                // Chuyển đổi Uri thành Bitmap
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                // Chuyển đổi Bitmap thành chuỗi Base64
//                String base64Image = encodeBitmapToBase64(bitmap);
//                base64Images.add(base64Image);  // Thêm Base64 vào danh sách
//
//                // Kiểm tra xem tất cả ảnh đã tải lên chưa
//                remainingImages[0]--;
//                if (remainingImages[0] == 0 && base64Images.size() == imageUris.size()) {
//                    postNewContent(content, base64Images);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Failed to convert image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    // Đăng bài viết vào Realtime Database
//    private void postNewContent(String content, ArrayList<String> base64Images) {
//        String postId = postsRef.push().getKey();
//        if (postId == null) {
//            Log.e("FirebaseError", "Không thể tạo postId");
//            Toast.makeText(this, "Không thể tạo postId", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Log.d("DEBUG", "Nội dung bài viết: " + content);
//        Log.d("DEBUG", "Ảnh Base64: " + base64Images.toString());
//        Log.d("DEBUG", "Post ID: " + postId);
//
//        // Tạo đối tượng Post và lưu vào Firebase
//        Post newPost = new Post(content, base64Images, System.currentTimeMillis());
//
//        // Lưu bài viết vào Firebase Realtime Database
//        postsRef.child(postId).setValue(newPost)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Bài viết đã được đăng!", Toast.LENGTH_SHORT).show();
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
//
//
//    // Hiển thị ảnh từ chuỗi Base64
//    private void displayImagesFromBase64(ArrayList<String> base64Images) {
//        for (String base64Image : base64Images) {
//            Bitmap bitmap = decodeBase64ToBitmap(base64Image);
//            ImageView imageView = new ImageView(this);
//            imageView.setImageBitmap(bitmap);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
//            imageContainer.addView(imageView);
//        }
//    }
//}
public class PostActivity extends Activity {

    private EditText editTextPostContent;
    private Button btnPost;
    private ImageView btnSelectImage;
    private LinearLayout imageContainer;

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

    private void uploadImagesAndPost(String content) {
        Toast.makeText(this, "Uploading images...", Toast.LENGTH_SHORT).show();

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

//    private void postNewContent(String content, ArrayList<String> base64Images) {
//        String postId = postsRef.push().getKey();
//        if (postId == null) {
//            Log.e("FirebaseError", "Không thể tạo postId");
//            Toast.makeText(this, "Không thể tạo postId", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Log.d("DEBUG", "Nội dung bài viết: " + content);
//        Log.d("DEBUG", "Ảnh Base64: " + base64Images.toString());
//        Log.d("DEBUG", "Post ID: " + postId);
//
//        Post newPost = new Post(content, base64Images, System.currentTimeMillis());
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

        String username = "current_user"; // Replace with the actual logged-in username, e.g., FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String timeAgo = "Just now"; // Replace with actual time logic (you can use `DateUtils.getRelativeTimeSpanString()` for example).
        int likeCount = 0;
        int commentCount = 0;

        Log.d("DEBUG", "Nội dung bài viết: " + content);
        Log.d("DEBUG", "Ảnh Base64: " + base64Images.toString());
        Log.d("DEBUG", "Post ID: " + postId);

        Post newPost = new Post(username, content, timeAgo, base64Images, likeCount, commentCount);

        postsRef.child(postId).setValue(newPost)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Bài viết đã được đăng!", Toast.LENGTH_SHORT).show();
                        // Redirect to NewsFeedActivity after successful post
                        Intent intent = new Intent(PostActivity.this, NewsFeedActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("FirebaseError", "Không thể đăng bài", task.getException());
                        Toast.makeText(this, "Không thể đăng bài!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Lỗi khi đăng bài", e);
                    Toast.makeText(this, "Lỗi khi đăng bài: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void displayImagesFromBase64(ArrayList<String> base64Images) {
        for (String base64Image : base64Images) {
            Bitmap bitmap = decodeBase64ToBitmap(base64Image);
            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(bitmap);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            imageContainer.addView(imageView);
        }
    }
}
