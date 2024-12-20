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

        // Xử lý sự kiện click vào nút tim (like)
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
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            imgPost = itemView.findViewById(R.id.imgPost);
            imgHeart = itemView.findViewById(R.id.imgHeart);
            imgComment = itemView.findViewById(R.id.imgComment);

            if (tvUsername == null || tvContent == null || imgPost == null) {
                Log.e("PostViewHolder", "Error inflating View, check post_item.xml");
            }
        }
    }
}
