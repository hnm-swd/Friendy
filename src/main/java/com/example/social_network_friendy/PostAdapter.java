//package com.example.social_network_friendy;
//
//import android.content.Context;
//import android.content.Intent;
//import android.health.connect.datatypes.OvulationTestRecord;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//import com.bumptech.glide.Glide;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.List;
//
//
////public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
////
////    private List<Post> posts;
////
////    public PostAdapter(List<Post> posts) {
////        this.posts = posts;
////    }
////
////    @NonNull
////    @Override
////    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
////        return new PostViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
////        Post post = posts.get(position);
////
////        // Gán nội dung bài viết vào TextView
////        holder.titleTextView.setText(post.getContent());
////
////        // Gán tên người dùng vào TextView
////        holder.usernameTextView.setText(post.getUsername());
////
////        // Xử lý hình ảnh nếu có
////        String imageUriString = post.getImageUri();
////        if (imageUriString != null && !imageUriString.isEmpty()) {
////            holder.postImageView.setVisibility(View.VISIBLE);
////            Glide.with(holder.postImageView.getContext())
////                    .load(imageUriString)
////                    .placeholder(R.drawable.ic_launcher_background) // Ảnh hiển thị khi đang tải
////                    .into(holder.postImageView);
////        } else {
////            holder.postImageView.setVisibility(View.GONE);
////        }
////
////        // Handle like button
////        if (post.isLiked()) {
////            holder.heartImageView.setImageResource(R.drawable.ic_heart_filled); // Tim đỏ
////        } else {
////            holder.heartImageView.setImageResource(R.drawable.ic_heart); // Tim trắng
////        }
////        holder.heartImageView.setOnClickListener(v -> {
////            post.setLiked(!post.isLiked()); // Toggle trạng thái liked
////            int likeCount = post.getLikeCount();
////            post.setLikeCount(post.isLiked() ? likeCount + 1 : likeCount - 1); // Tăng hoặc giảm số lượt thích
////            updateLikeInDatabase(post); // Cập nhật vào Firebase
////            notifyItemChanged(position); // Cập nhật UI cho bài viết hiện tại
////        });
////        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
////
////    }
////
////    @Override
////    public int getItemCount() {
////        return posts.size();
////    }
////    private void updateLikeInDatabase(Post post) {
////        // Cập nhật trạng thái và số lượt like của bài viết lên Firebase
////        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId());
////        postRef.child("liked").setValue(post.isLiked());
////        postRef.child("likeCount").setValue(post.getLikeCount());
////    }
////
////    public static class PostViewHolder extends RecyclerView.ViewHolder {
////        TextView titleTextView, usernameTextView, likeCountTextView; // Thêm TextView cho username
////        ImageView postImageView, heartImageView;
////
////        public PostViewHolder(@NonNull View itemView) {
////            super(itemView);
////            titleTextView = itemView.findViewById(R.id.tvContent); // ID của title trong post_item.xml
////            usernameTextView = itemView.findViewById(R.id.tvUsername); // ID của username trong post_item.xml
////            postImageView = itemView.findViewById(R.id.imgPost); // ID của ảnh trong post_item.xml
////            heartImageView = itemView.findViewById(R.id.imgHeart);
////            likeCountTextView = itemView.findViewById(R.id.tvLikeCount);
////        }
////    }
////
////    // Cập nhật danh sách bài viết
////    public void updatePosts(List<Post> newPosts) {
////        this.posts.clear(); // Xóa danh sách cũ
////        this.posts.addAll(newPosts); // Thêm bài viết mới
////        notifyDataSetChanged(); // Thông báo adapter để cập nhật giao diện
////    }
////}
////
////public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
////
////    private List<Post> posts;
////
////    public PostAdapter(List<Post> posts) {
////        this.posts = posts;
////    }
////
////    @NonNull
////    @Override
////    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
////        return new PostViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
////        Post post = posts.get(position);
////
////        holder.titleTextView.setText(post.getContent());
////        holder.usernameTextView.setText(post.getUsername());
////
////        String imageUriString = post.getImageUri();
////        if (imageUriString != null && !imageUriString.isEmpty()) {
////            holder.postImageView.setVisibility(View.VISIBLE);
////            Glide.with(holder.postImageView.getContext())
////                    .load(imageUriString)
////                    .placeholder(R.drawable.ic_launcher_background)
////                    .into(holder.postImageView);
////        } else {
////            holder.postImageView.setVisibility(View.GONE);
////        }
////
////        // Set the like icon based on whether the post is liked
////        holder.heartImageView.setImageResource(post.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
////
////        holder.heartImageView.setOnClickListener(v -> {
////            // Toggle like state
////            post.setLiked(!post.isLiked());
////
////            // Update like count
////            int likeCount = post.getLikeCount();
////            post.setLikeCount(post.isLiked() ? likeCount + 1 : likeCount - 1);
////
////            // Update the database with the new like state and like count
////            updateLikeInDatabase(post);
////
////            // Notify RecyclerView to update the UI
////            notifyItemChanged(position);
////        });
////
////        // Set the like count text
////        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
////    }
////
////    @Override
////    public int getItemCount() {
////        return posts.size();
////    }
////
////    private void updateLikeInDatabase(Post post) {
////        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId());
////        postRef.child("liked").setValue(post.isLiked());
////        postRef.child("likeCount").setValue(post.getLikeCount());
////    }
////
////    public static class PostViewHolder extends RecyclerView.ViewHolder {
////        TextView titleTextView, usernameTextView, likeCountTextView;
////        ImageView postImageView, heartImageView;
////
////        public PostViewHolder(@NonNull View itemView) {
////            super(itemView);
////            titleTextView = itemView.findViewById(R.id.tvContent);
////            usernameTextView = itemView.findViewById(R.id.tvUsername);
////            postImageView = itemView.findViewById(R.id.imgPost);
////            heartImageView = itemView.findViewById(R.id.imgHeart);
////            likeCountTextView = itemView.findViewById(R.id.tvLikeCount);
////        }
////    }
////
////    public void updatePosts(List<Post> newPosts) {
////        this.posts.clear();
////        this.posts.addAll(newPosts);
////        notifyDataSetChanged();
////    }
////}
////public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
////
////    private List<Post> posts;
////
////    public PostAdapter(List<Post> posts) {
////        this.posts = posts;
////    }
////
////    @NonNull
////    @Override
////    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
////        return new PostViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
////        Post post = posts.get(position);
////
////        // Set the post content and username
////        holder.titleTextView.setText(post.getContent());
////        holder.usernameTextView.setText(post.getUsername());
////        // Set listener for username click
////        holder.usernameTextView.setOnClickListener(v -> {
////            // Navigate to OtherProfileActivity when username is clicked
////            Context context = v.getContext();
////            Intent intent = new Intent(context, OtherProfileActivity.class);
////            intent.putExtra("userId", post.getUserId()); // Pass userId to the other profile activity
////            context.startActivity(intent);
////        });
////        // Set the image if available
////        String imageUriString = post.getImageUri();
////        if (imageUriString != null && !imageUriString.isEmpty()) {
////            holder.postImageView.setVisibility(View.VISIBLE);
////            Glide.with(holder.postImageView.getContext())
////                    .load(imageUriString)
////                    .placeholder(R.drawable.ic_launcher_background)
////                    .into(holder.postImageView);
////        } else {
////            holder.postImageView.setVisibility(View.GONE);
////        }
////
////        // Set the like icon based on whether the post is liked
////        holder.heartImageView.setImageResource(post.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
////
////        // Handle the like button click
////        holder.heartImageView.setOnClickListener(v -> {
////            // Toggle like state
////            post.setLiked(!post.isLiked());
////
////            // Update like count
////            int likeCount = post.getLikeCount();
////            post.setLikeCount(post.isLiked() ? likeCount + 1 : likeCount - 1);
////
////            // Update the database with the new like state and like count
////            updateLikeInDatabase(post);
////
////            // Notify RecyclerView to update the UI
////            notifyItemChanged(position);
////        });
////
////        // Set the like count text
////        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
////    }
////
////    @Override
////    public int getItemCount() {
////        return posts.size();
////    }
////
////    private void updateLikeInDatabase(Post post) {
////        // Ensure postId is available and update the corresponding post in Firebase
////        if (post.getPostId() != null) {
////            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId());
////            postRef.child("liked").setValue(post.isLiked());
////            postRef.child("likeCount").setValue(post.getLikeCount());
////        }
////    }
////
////    public static class PostViewHolder extends RecyclerView.ViewHolder {
////        TextView titleTextView, usernameTextView, likeCountTextView;
////        ImageView postImageView, heartImageView;
////
////        public PostViewHolder(@NonNull View itemView) {
////            super(itemView);
////            titleTextView = itemView.findViewById(R.id.tvContent);
////            usernameTextView = itemView.findViewById(R.id.tvUsername);
////            postImageView = itemView.findViewById(R.id.imgPost);
////            heartImageView = itemView.findViewById(R.id.imgHeart);
////            likeCountTextView = itemView.findViewById(R.id.tvLikeCount);
////        }
////    }
////
////    public void updatePosts(List<Post> newPosts) {
////        this.posts.clear();
////        this.posts.addAll(newPosts);
////        notifyDataSetChanged();
////    }
////}
//
////public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
////
////    private List<Post> posts;
////
////    public PostAdapter(List<Post> posts) {
////        this.posts = posts;
////    }
////
////    @NonNull
////    @Override
////    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
////        return new PostViewHolder(view);
////    }
////
////    @Override
////    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
////        Post post = posts.get(position);
////
////        // Set the post content and username
////        holder.titleTextView.setText(post.getContent());
////        holder.usernameTextView.setText(post.getUsername());
////
////        // Set listener for username click
////        holder.usernameTextView.setOnClickListener(v -> {
////            // Navigate to OtherProfileActivity when username is clicked
////            Context context = v.getContext();
////            Intent intent = new Intent(context, OtherProfileActivity.class);
////            intent.putExtra("userId", post.getUserId()); // Pass userId to the other profile activity
////            context.startActivity(intent);
////        });
////
////        // Set the image if available
////        String imageUriString = post.getImageUri();
////        if (imageUriString != null && !imageUriString.isEmpty()) {
////            holder.postImageView.setVisibility(View.VISIBLE);
////            Glide.with(holder.postImageView.getContext())
////                    .load(imageUriString)
////                    .placeholder(R.drawable.ic_launcher_background)
////                    .into(holder.postImageView);
////        } else {
////            holder.postImageView.setVisibility(View.GONE);
////        }
////
////        // Set the like icon based on whether the post is liked
////        holder.heartImageView.setImageResource(post.isLiked() ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
////
////        // Handle the like button click
////        holder.heartImageView.setOnClickListener(v -> {
////            // Toggle like state
////            post.setLiked(!post.isLiked());
////
////            // Update like count
////            int likeCount = post.getLikeCount();
////            post.setLikeCount(post.isLiked() ? likeCount + 1 : likeCount - 1);
////
////            // Update the database with the new like state and like count
////            updateLikeInDatabase(post);
////
////            // Notify RecyclerView to update the UI
////            notifyItemChanged(position);
////        });
////
////        // Set the like count text
////        holder.likeCountTextView.setText(String.valueOf(post.getLikeCount()));
////    }
////
////    @Override
////    public int getItemCount() {
////        return posts.size();
////    }
////
////    private void updateLikeInDatabase(Post post) {
////        // Ensure postId is available and update the corresponding post in Firebase
////        if (post.getPostId() != null) {
////            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(post.getPostId());
////            postRef.child("liked").setValue(post.isLiked());
////            postRef.child("likeCount").setValue(post.getLikeCount());
////        }
////    }
////
////    public static class PostViewHolder extends RecyclerView.ViewHolder {
////        TextView titleTextView, usernameTextView, likeCountTextView;
////        ImageView postImageView, heartImageView;
////
////        public PostViewHolder(@NonNull View itemView) {
////            super(itemView);
////            titleTextView = itemView.findViewById(R.id.tvContent);
////            usernameTextView = itemView.findViewById(R.id.tvUsername);
////            postImageView = itemView.findViewById(R.id.imgPost);
////            heartImageView = itemView.findViewById(R.id.imgHeart);
////            likeCountTextView = itemView.findViewById(R.id.tvLikeCount);
////        }
////    }
////
////    public void updatePosts(List<Post> newPosts) {
////        this.posts.clear();
////        this.posts.addAll(newPosts);
////        notifyDataSetChanged();
////    }
////}
//
//
package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context; // Context để sử dụng trong Intent
    private List<Post> postList; // Danh sách bài viết

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

        // Set số lượng bình luận
        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));

//         Xử lý sự kiện click vào nút tim (like)
        holder.imgHeart.setOnClickListener(v -> {
            boolean hasUserLiked = post.getHasUserLiked(); // Kiểm tra xem người dùng đã thích bài viết chưa

            Log.d("PostAdapter", "Heart clicked for post: " + post.getPostId());

            if (hasUserLiked) {
                // Nếu người dùng đã thích, hủy like (giảm số lượt like)
                post.setLikeCount(post.getLikeCount() - 1);
                post.setHasUserLiked(false); // Đánh dấu là chưa thích
                holder.imgHeart.setImageResource(R.drawable.heart); // Đổi icon thành "tim rỗng"
            } else {
                // Nếu người dùng chưa thích, like bài viết (tăng số lượt like)
                post.setLikeCount(post.getLikeCount() + 1);
                post.setHasUserLiked(true); // Đánh dấu là đã thích
                holder.imgHeart.setImageResource(R.drawable.ic_heart_filled); // Đổi icon thành "tim đầy"
            }

            // Cập nhật lại số lượt like trên UI
            holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));
        });
        // Set listener for username click (Thêm đoạn ni)
        holder.tvUsername.setOnClickListener(v -> {
            String postUsername = post.getUsername();
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId).child("username");
            usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentUsername = dataSnapshot.getValue(String.class);
                    Intent intent;
                    Context context = v.getContext();

                    if (currentUsername != null && currentUsername.equals(postUsername)) {
                        intent = new Intent(context, MyProfileActivity.class);
                    } else {
                        intent = new Intent(context, OtherProfileActivity.class);
                        intent.putExtra("username", postUsername);
                    }
                    context.startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("PostAdapter", "Failed to fetch current username: " + databaseError.getMessage());
                }
            });
        });
        holder.imgHeart.setOnClickListener(v -> {
            String postId = post.getPostId();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId).child("likes");

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
// Handle comment icon click event
        holder.imgComment.setOnClickListener(v -> {
            Log.d("PostAdapter", "Comment clicked for post: " + post.getPostId());

            // Tạo Intent để mở CommentActivity
            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra("postId", post.getPostId()); // Truyền postId vào CommentActivity
            ((Activity) context).startActivityForResult(intent, 1); // Mở Activity và nhận kết quả từ Activity

        });


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

        TextView tvUsername, tvTime, tvContent, tvLikeCount, tvCommentCount;
        ImageView imgPost, imgHeart, imgComment;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
//            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            imgPost = itemView.findViewById(R.id.imgPost);
            imgHeart = itemView.findViewById(R.id.imgHeart);
//            imgComment = itemView.findViewById(R.id.imgComment);

            if (tvUsername == null || tvContent == null || imgPost == null) {
                Log.e("PostViewHolder", "Error inflating View, check post_item.xml");
            }
        }
    }
}

