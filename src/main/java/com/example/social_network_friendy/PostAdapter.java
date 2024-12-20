package com.example.social_network_friendy;//package com.example.social_network_friendy;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
//
//    private List<PostModel> postList;
//
//    public PostAdapter(List<PostModel> postList) {
//        this.postList = postList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new ViewHolder(view);
//    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        PostModel post = postList.get(position);
//
//        holder.usernameTextView.setText(post.getUsername());
//        holder.timeTextView.setText(post.getTime());
//        holder.contentTextView.setText(post.getContent());
//
//        // Load image using Glide
//        Glide.with(holder.itemView.getContext())
//                .load(post.getImageUrl())
//                .placeholder(R.drawable.profileimage) // Placeholder image
//                .into(holder.postImageView);
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView usernameTextView, timeTextView, contentTextView;
//        ImageView postImageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            usernameTextView = itemView.findViewById(R.id.tvUsername); // Match with ID in XML
//            timeTextView = itemView.findViewById(R.id.tvTime); // Match with ID in XML
//            contentTextView = itemView.findViewById(R.id.tvContent); // Match with ID in XML
//            postImageView = itemView.findViewById(R.id.imgPost); // Match with ID in XML
//        }
//    }
//}
//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//    private List<PostModel> postList;
//
//    public PostAdapter(List<PostModel> postList) {
//        this.postList = postList;
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.post_item_layout, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        PostModel post = postList.get(position);
//        holder.usernameTextView.setText(post.getUsername());
//        holder.contentTextView.setText(post.getContent());
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView usernameTextView, contentTextView;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            usernameTextView = itemView.findViewById(R.id.textViewUsername);
//            contentTextView = itemView.findViewById(R.id.textViewContent);
//        }
//    }
//}

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.social_network_friendy.Post;

import java.util.List;

//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//
//    private List<Post> posts;
//
//    public PostAdapter(List<Post> posts) {
//        this.posts = posts;
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        Post post = posts.get(position);
//        holder.titleTextView.setText(post.getContent());
//
//        String imageUriString = String.valueOf(post.getImageUri());
//        if (imageUriString != null && !imageUriString.isEmpty()) {
//            holder.postImageView.setVisibility(View.VISIBLE);
//            Glide.with(holder.postImageView.getContext())
//                    .load(imageUriString)
//                    .placeholder(R.drawable.ic_launcher_background) // Ảnh hiển thị khi đang tải
////                    .error(R.drawable.x) // Ảnh lỗi
//                    .into(holder.postImageView);
//        } else {
//            holder.postImageView.setVisibility(View.GONE);
//        }
//    }
//
//
//
//
//
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        ImageView postImageView;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.tvContent); // ID của title trong post_item.xml
//            postImageView = itemView.findViewById(R.id.imgPost); // ID của ảnh trong post_item.xml
//        }
//    }
//    // PostAdapter.java
//    public void updatePosts(List<Post> newPosts) {
//        this.posts.clear(); // Xóa danh sách cũ
//        this.posts.addAll(newPosts); // Thêm bài viết mới
//        notifyDataSetChanged(); // Thông báo adapter để cập nhật giao diện
//    }
//
//}
//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//
//    private List<Post> posts;
//
//    public PostAdapter(List<Post> posts) {
//        this.posts = posts;
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        Post post = posts.get(position);
//
//        // Hiển thị nội dung bài viết
//        holder.titleTextView.setText(post.getContent());
//
//        // Hiển thị hình ảnh (nếu có)
//        if (post.getImageUri() != null && !post.getImageUri().isEmpty()) {
//            holder.postImageView.setVisibility(View.VISIBLE);
//            Glide.with(holder.postImageView.getContext())
//                    .load(post.getImageUri().get(0)) // Lấy ảnh đầu tiên từ danh sách ảnh
//                    .placeholder(R.drawable.ic_launcher_background)
//                    .into(holder.postImageView);
//        } else {
//            holder.postImageView.setVisibility(View.GONE);
//        }
//
//        // Sự kiện click vào bài đăng
//        holder.itemView.setOnClickListener(v -> {
//            // Xử lý sự kiện click vào bài viết, sử dụng postId để làm điều gì đó
//            String postId = post.getPostId();
//            Toast.makeText(holder.itemView.getContext(), "Post ID: " + postId, Toast.LENGTH_SHORT).show();
//
//            // Ví dụ: Chuyển đến màn hình chi tiết bài viết
//            // Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
//            // intent.putExtra("POST_ID", postId);
//            // holder.itemView.getContext().startActivity(intent);
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return posts.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        ImageView postImageView;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.tvContent); // ID của title trong post_item.xml
//            postImageView = itemView.findViewById(R.id.imgPost); // ID của ảnh trong post_item.xml
//        }
//    }
//
//    // Cập nhật danh sách bài viết
//    public void updatePosts(List<Post> newPosts) {
//        this.posts.clear();
//        this.posts.addAll(newPosts);
//        notifyDataSetChanged();
//    }
//}


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import java.util.ArrayList;
//
//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//
//    private ArrayList<Post> postsList;
//
//    // Constructor
//    public PostAdapter(ArrayList<Post> postsList) {
//        this.postsList = postsList;
//    }
//
//    @Override
//    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Post post = postsList.get(position);
//
//        // Hiển thị nội dung bài viết
//        holder.contentTextView.setText(post.getContent());
//
//        // Hiển thị ảnh nếu có
//        if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
//            // Lặp qua tất cả ảnh trong danh sách Base64
//            for (String base64Image : post.getImageBase64()) {
//                Bitmap bitmap = decodeBase64ToBitmap(base64Image);
//                if (bitmap != null) {
//                    holder.imageView.setImageBitmap(bitmap);
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return postsList.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        public TextView contentTextView;
//        public ImageView imageView;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            contentTextView = itemView.findViewById(R.id.tvContent);
//            imageView = itemView.findViewById(R.id.imgPost);
//        }
//    }
//
//    // Chuyển đổi Base64 thành Bitmap
//    private Bitmap decodeBase64ToBitmap(String base64Str) {
//        try {
//            byte[] decodedString = Base64.decode(base64Str, Base64.DEFAULT);
//            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
//    private List<Post> postList;
//
//    public PostAdapter(List<Post> postList) {
//        this.postList = postList;
//    }
//
//    @Override
//    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//        holder.contentTextView.setText(post.getContent());
//        // Hiển thị ảnh (nếu có)
//        if (post.getImageBase64()    != null && !post.getImageBase64().isEmpty()) {
//            Glide.with(holder.itemView.getContext())
//                    .load(post.getImageBase64().get(0))  // Chỉ lấy ảnh đầu tiên
//                    .into(holder.postImageView);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView contentTextView;
//        ImageView postImageView;
//
//        public PostViewHolder(View itemView) {
//            super(itemView);
//            contentTextView = itemView.findViewById(R.id.tvContent);
//            postImageView = itemView.findViewById(R.id.imgPost);
//        }
//    }
//}
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Set avatar, username, time, and content
        holder.tvUsername.setText(post.getUsername());
        holder.tvTime.setText(post.getTimeAgo());
        holder.tvContent.setText(post.getContent());

        // Set image if available
        if (post.getImageBase64() != null && !post.getImageBase64().isEmpty()) {
            holder.imgPost.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext()).load(post.getImageBase64()).into(holder.imgPost);
        } else {
            holder.imgPost.setVisibility(View.GONE);
        }

        // Set like count
        holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));

        // Set comment count
        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername, tvTime, tvContent, tvLikeCount, tvCommentCount;
        ImageView imgPost, imgHeart, imgComment;

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
        }
    }
}

