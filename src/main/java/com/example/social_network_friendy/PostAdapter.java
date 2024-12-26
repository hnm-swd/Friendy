package com.example.social_network_friendy;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context; // Context để sử dụng trong Intent
    private List<Post> postList; // Danh sách bài viết
    private Handler handler = new Handler();
    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged(); // Làm mới danh sách bài viết
            handler.postDelayed(this, 60 * 1000); // Cập nhật mỗi phút
        }
    };
    // Constructor nhận thêm context
    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList != null ? postList : new ArrayList<>();
    }

    // Tạo ViewHolder và inflate layout
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    // Gắn dữ liệu vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (position < 0 || position >= postList.size()) {
            Log.e("PostAdapter", "Invalid position: " + position);
            return;
        }

        Post post = postList.get(position);

        if (post == null) {
            Log.e("PostAdapter", "Post is null at position: " + position);
            return;
        }
        // Kiểm tra postId và userId không phải là null
        final String postId = post.getPostId();
        if (postId == null) {
            Log.e("PostAdapter", "postId is null at position: " + position);
            return;
        }

        final String userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        if (userId == null) {
            Log.e("PostAdapter", "userId is null");
            return;
        }

        // Set user name
        holder.tvUsername.setText(post.getUsername() != null ? post.getUsername() : "Người dùng");

        // Set time of post
        holder.tvTime.setText(post.getTimeAgo() != null ? post.getTimeAgo() : "Vào lúc nào");

        // Set post content
        holder.tvContent.setText(post.getContent() != null ? post.getContent() : "Chưa có nội dung");

        // Hiển thị hình ảnh bài viết (nếu có)
        if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
            holder.imgPost.setVisibility(View.VISIBLE);
            try {
                Glide.with(holder.itemView.getContext())
                        .load(Base64.decode(post.getImageBase64().get(0), Base64.DEFAULT)) // Chỉ lấy hình ảnh đầu tiên
                        .override(500, 500) // Resize ảnh thành 500x500
                        .into(holder.imgPost);
            } catch (Exception e) {
                Log.e("PostAdapter", "Error loading image: " + e.getMessage());
                holder.imgPost.setVisibility(View.GONE);
            }
        } else {
            holder.imgPost.setVisibility(View.GONE); // Ẩn ảnh nếu không có ảnh
        }

        // Set số lượng likes
        holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));
        // Kiểm tra trạng thái like của người dùng khi đăng nhập
        final DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes");

        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)) {
                    // Nếu người dùng đã like, cập nhật hình ảnh trái tim thành tim đầy
                    holder.imgHeart.setImageResource(R.drawable.ic_heart_filled);
                    post.setHasUserLiked(true);
                } else {
                    // Nếu người dùng chưa like, cập nhật hình ảnh trái tim thành tim rỗng
                    holder.imgHeart.setImageResource(R.drawable.heart);
                    post.setHasUserLiked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostAdapter", "Failed to check like status: " + error.getMessage());
            }
        });

        // Set số lượng bình luận
        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));
        holder.imgComment.setOnClickListener(v -> {
            Log.d("PostAdapter", "Comment clicked for post: " + post.getPostId());

            // Tạo Intent để mở CommentActivity
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId()); // Truyền postId vào CommentActivity
            ((Activity) context).startActivityForResult(intent, 1); // Mở Activity và nhận kết quả từ Activity

        });
        // Xử lý sự kiện click vào nút tim (like)
        holder.imgHeart.setOnClickListener(v -> {
            // Xử lý khi người dùng nhấn like hoặc bỏ like
//            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes");

            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean hasUserLiked = snapshot.hasChild(userId);

                    if (hasUserLiked) {
                        // Nếu người dùng đã thích, xóa like (bỏ tim)
                        postRef.child(userId).removeValue();
                        post.setLikeCount(post.getLikeCount() - 1);
                        post.setHasUserLiked(false);
                        holder.imgHeart.setImageResource(R.drawable.heart); // Tim rỗng
                    } else {
                        // Nếu người dùng chưa thích, thêm like
                        postRef.child(userId).setValue(true);
                        post.setLikeCount(post.getLikeCount() + 1);
                        post.setHasUserLiked(true);
                        holder.imgHeart.setImageResource(R.drawable.ic_heart_filled); // Tim đầy
                    }

                    // Cập nhật số lượng like lên Firebase
                    FirebaseDatabase.getInstance()
                            .getReference("posts")
                            .child(postId)
                            .child("likeCount")
                            .setValue(post.getLikeCount());
//                     Gửi thông báo
                    sendLikeNotification(post);
                    // Cập nhật giao diện
                    holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("PostAdapter", "Failed to update likes: " + error.getMessage());
                }
            });


        });


        // Xử lý sự kiện click vào biểu tượng bình luận
        holder.imgComment.setOnClickListener(v -> {
            Log.d("PostAdapter", "Comment clicked for post: " + post.getPostId());

            // Tạo một Intent để mở CommentActivity
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId()); // Truyền postId sang CommentActivity
            context.startActivity(intent); // Bắt đầu activity

        });

        holder.tvUsername.setOnClickListener(v -> {
            String postUsername = post.getUsername(); // Username của bài viết
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Lấy ID người dùng hiện tại

            // Lấy username của người dùng hiện tại từ Firebase (hoặc SharedPreferences nếu đã lưu)
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("username");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentUsername = dataSnapshot.getValue(String.class); // Username người dùng hiện tại

                    Intent intent;
                    Context context = v.getContext();

                    if (currentUsername != null && currentUsername.equals(postUsername)) {
                        // Nếu bài viết là của người dùng hiện tại, mở MyProfileActivity
                        intent = new Intent(context, MyProfileActivity.class);
                    } else {
                        // Nếu bài viết không phải của người dùng hiện tại, mở OtherProfileActivity
                        intent = new Intent(context, OtherProfileActivity.class);
                        intent.putExtra("username", postUsername); // Truyền username của bài viết sang OtherProfileActivity
                    }
                    context.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("PostAdapter", "Failed to fetch current username: " + databaseError.getMessage());
                }
            });
        });

        // Load avatar
        String username = post.getUsername();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Lấy userId từ dữ liệu người dùng
                    String userId = snapshot.getChildren().iterator().next().getKey(); // Lấy key của người dùng từ Firebase

                    // Truy vấn ảnh avatar từ Firebase
                    DatabaseReference avatarRef = FirebaseDatabase.getInstance().getReference("avatars").child(userId);
                    avatarRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String avatarBase64 = snapshot.child("avatar").getValue(String.class);
                            if (avatarBase64 != null && !avatarBase64.isEmpty()) {
                                // Chuyển base64 thành Bitmap và hiển thị
                                Bitmap avatarBitmap = decodeBase64ToBitmap(avatarBase64);
                                holder.avatarImageView.setImageBitmap(avatarBitmap);
                            } else {
                                // Nếu không có avatar, dùng ảnh mặc định
                                holder.avatarImageView.setImageResource(R.drawable.img_profile_avatar);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("PostAdapter", "Failed to load avatar from Firebase: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostAdapter", "Failed to load userId: " + error.getMessage());
            }
        });


    }
    private void sendLikeNotification(Post post) {
        // Kiểm tra nếu người dùng chưa đăng nhập
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("PostAdapter", "User is not logged in.");
            return; // Dừng lại nếu người dùng chưa đăng nhập
        }

        String userId = currentUser.getUid(); // Lấy userId của người dùng hiện tại
        // Lấy userId của chủ bài viết
        String postOwnerId = post.getUsername();
        if (postOwnerId == null) {
            Log.e("PostAdapter", "Post owner ID is null.");
            return;
        }
//
//        // Kiểm tra nếu post.getUserId() là null
//        if (post.getUsername() == null) {
//            Log.e("PostAdapter", "Post userId is null, can't send like notification.");
//            return; // Dừng lại nếu không có userId trong bài viết
//        }
        // Lấy username của người dùng hiện tại
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("username");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentUsername = dataSnapshot.getValue(String.class);
                if (currentUsername == null) {
                    Log.e("PostAdapter", "Current username is null");
                    return;
                }
//                // Gửi thông báo tới chủ bài viết
//                DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
//                        .child(post.getUsername());  // Gửi thông báo cho chủ bài viết
                // Tạo thông báo
                DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications").child(postOwnerId);
                String notificationId = notificationsRef.push().getKey();
                if (notificationId == null) {
                    Log.e("PostAdapter", "Notification ID is null.");
                    return; // Nếu notificationId là null, không thể tạo thông báo
                }

                Notification notification = new Notification(
                        "like",  // Loại thông báo (like)
                        currentUsername + " đã thích bài viết của bạn.", // Nội dung thông báo
                        post.getPostId(),
                        userId,
                        ServerValue.TIMESTAMP  // Thời gian tạo thông báo
                );

                notificationsRef.child(notificationId).setValue(notification)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d("PostAdapter", "Like notification sent successfully.");
                            } else {
                                Log.e("PostAdapter", "Failed to send like notification: " + task.getException());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("PostAdapter", "Failed to fetch current username: " + databaseError.getMessage());
            }
        });

//        // Gửi thông báo tới chủ bài viết
//        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
//                .child(post.getUsername());  // Gửi thông báo cho chủ bài viết
//
//        String notificationId = notificationsRef.push().getKey();
//        if (notificationId == null) {
//            Log.e("PostAdapter", "Notification ID is null.");
//            return; // Nếu notificationId là null, không thể tạo thông báo
//        }
//
//        Notification notification = new Notification(
//                "like",  // Loại thông báo (like)
//                userId + " liked your post", // Nội dung thông báo
//                post.getPostId(),
//                userId,
//                ServerValue.TIMESTAMP  // Thời gian tạo thông báo
//        );
//
//        notificationsRef.child(notificationId).setValue(notification)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("PostAdapter", "Like notification sent successfully.");
//                    } else {
//                        Log.e("PostAdapter", "Failed to send like notification: " + task.getException());
//                    }
//                });

    }






    // Trả về số lượng bài viết
    @Override
    public int getItemCount() {
        return postList != null ? postList.size() : 0;
    }

    // Cập nhật danh sách bài viết (dùng để làm mới danh sách)
    public void updatePosts(List<Post> newPosts) {
        if (newPosts != null) {
            if (this.postList == null) {
                this.postList = new ArrayList<>();
            }
            this.postList.clear();
            this.postList.addAll(newPosts);
            notifyDataSetChanged();
        }
    }

    // ViewHolder để đại diện cho mỗi bài viết
    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvTime, tvContent, tvLikeCount, tvCommentCount, commentsTextView;
        ImageView imgPost, imgHeart, imgComment;
        CircleImageView avatarImageView;
        public PostViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            imgPost = itemView.findViewById(R.id.imgPost);
            imgHeart = itemView.findViewById(R.id.imgHeart);
            imgComment = itemView.findViewById(R.id.imgComment);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);

            if (tvUsername == null || tvContent == null || imgPost == null) {
                Log.e("PostViewHolder", "Error inflating View, check post_item.xml");
            }
        }
    }
    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}