package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//
//public class PostAdapter_MyProfile extends RecyclerView.Adapter<PostAdapter_MyProfile.PostViewHolder> {
//
//    private List<Post_MyProfile> postList;
//    private Context context;
//    private static final int COMMENT_REQUEST_CODE = 100;
//    private OnPostClickListener onPostClickListener;
//    // Interface cho callback
//    public interface OnPostClickListener {
//        void onPostClick(Post_MyProfile post, int position);
//    }
//
//    public PostAdapter_MyProfile(List<Post_MyProfile> postList, Context context, OnPostClickListener listener) {
//        this.postList = postList;
//        this.context = context;
//        this.onPostClickListener = listener;
//    }
//
//    // Cập nhật bài đăng khi có bình luận mới
//    public void updatePost(Post_MyProfile updatedPost, int position) {
//        postList.set(position, updatedPost);
//        notifyItemChanged(position); // Cập nhật bài đăng tại vị trí
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        Post_MyProfile post = postList.get(position);
//
//        holder.tvContent.setText(post.getContent());
//        holder.imgPost.setImageResource(post.getImageResId());
//        holder.tvUsername.setText(post.getUsername());
//        holder.tvTime.setText(post.getTime());
//        holder.tvCommentCount.setText(String.valueOf(post.getCommentCount()));
////        // Hiển thị số lượng bình luận
////        holder.commentCountTextView.setText(post.getComments().size() + " Bình luận");
//        // Hiển thị các bình luận dưới bài viết
//        StringBuilder comments = new StringBuilder();
//        for (String comment : post.getComments()) {
//            comments.append(comment).append("\n");
//        }
//        holder.commentsTextView.setText(comments.toString());
//
//        updateLikeStatus(holder, post);
//
//        holder.imgHeart.setOnClickListener(v -> {
//            if (post.isLiked()) {
//                post.setLiked(false);
//                post.setLikeCount(post.getLikeCount() - 1);
//            } else {
//                post.setLiked(true);
//                post.setLikeCount(post.getLikeCount() + 1);
//            }
//
//            updateLikeStatus(holder, post);
//        });
//        // Thêm OnClickListener cho icon comment
//        holder.imgComment.setOnClickListener(v -> openPostComment(post));
//
////        holder.itemView.setOnClickListener(v -> {
////            if (onPostClickListener != null) {
////                onPostClickListener.onPostClick(post, position); // Thêm position
////            }
////        });
//        holder.itemView.setOnClickListener(v -> openPostComment(post));
//
//    }
//
//    private void updateLikeStatus(PostViewHolder holder, Post_MyProfile post) {
//        if (post.isLiked()) {
//            holder.imgHeart.setImageResource(R.drawable.ic_heart_filled);
//        } else {
//            holder.imgHeart.setImageResource(R.drawable.ic_heart);
//        }
//        holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));
//
//    }
//
//
//    private void openPostComment(Post_MyProfile post) {
//        Intent intent = new Intent(context, PostDetailActivity_MyProfile.class);
//        intent.putExtra("postId", post.getId());
//        intent.putExtra("imageResId", post.getImageResId());
//        context.startActivity(intent);
////        ((Activity) context).startActivityForResult(intent, COMMENT_REQUEST_CODE);
//    }
//
//    public void updatePostList(List<Post_MyProfile> newPostList) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return postList.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newPostList.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                return postList.get(oldItemPosition).getId() == newPostList.get(newItemPosition).getId();
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                Post_MyProfile oldPost = postList.get(oldItemPosition);
//                Post_MyProfile newPost = newPostList.get(newItemPosition);
//                return oldPost.equals(newPost);
//            }
//        });
//
//        postList.clear();
//        postList.addAll(newPostList);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView tvContent, tvUsername, tvTime, tvLikeCount, tvCommentCount, commentsTextView;
//        ImageView imgPost, imgHeart,imgComment;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvContent = itemView.findViewById(R.id.tvContent);
//            tvUsername = itemView.findViewById(R.id.tvUsername);
//            tvTime = itemView.findViewById(R.id.tvTime);
//            imgPost = itemView.findViewById(R.id.imgPost);
//            imgHeart = itemView.findViewById(R.id.imgHeart);
//            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
//            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
//            imgComment = itemView.findViewById(R.id.imgComment);
//            commentsTextView = itemView.findViewById(R.id.commentsTextView);
//
//        }
//    }
//}
//
//
//package com.example.social_network_friendy;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.DiffUtil;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class PostAdapter_MyProfile extends RecyclerView.Adapter<PostAdapter_MyProfile.PostViewHolder> {
//
//    private List<Post_MyProfile> postList;
//    private Context context;
//    private OnPostClickListener onPostClickListener;
//
//    // Interface callback
//    public interface OnPostClickListener {
//        void onCommentClick(Post_MyProfile post);
//    }
//
//    public PostAdapter_MyProfile(List<Post_MyProfile> postList, Context context, OnPostClickListener listener) {
//        this.postList = postList;
//        this.context = context;
//        this.onPostClickListener = listener;
//    }
//
//    // DiffUtil để so sánh danh sách cũ và mới
//    public void updatePostList(List<Post_MyProfile> newPostList) {
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
//            @Override
//            public int getOldListSize() {
//                return postList.size();
//            }
//
//            @Override
//            public int getNewListSize() {
//                return newPostList.size();
//            }
//
//            @Override
//            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//                return postList.get(oldItemPosition).getId() == newPostList.get(newItemPosition).getId();
//            }
//
//            @Override
//            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//                return postList.get(oldItemPosition).equals(newPostList.get(newItemPosition));
//            }
//        });
//
//        postList.clear();
//        postList.addAll(newPostList);
//        diffResult.dispatchUpdatesTo(this);
//    }
//
//    @NonNull
//    @Override
//    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.post_item, parent, false);
//        return new PostViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        Post_MyProfile post = postList.get(position);
//
//        // Bind dữ liệu
//        holder.tvContent.setText(post.getContent());
//        holder.tvUsername.setText(post.getUsername());
//        holder.tvTime.setText(post.getTime());
//        holder.tvLikeCount.setText(String.valueOf(post.getLikeCount()));
//
//        holder.imgPost.setImageResource(post.getImageResId());
//        updateLikeIcon(holder, post.isLiked());
//
//
//
//    }
//
//
//    private void updateLikeIcon(PostViewHolder holder, boolean isLiked) {
//        if (isLiked) {
//            holder.imgHeart.setImageResource(R.drawable.ic_heart_filled);
//        } else {
//            holder.imgHeart.setImageResource(R.drawable.ic_heart);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return postList.size();
//    }
//
//    public static class PostViewHolder extends RecyclerView.ViewHolder {
//        TextView tvContent, tvUsername, tvTime, tvLikeCount, tvCommentCount, commentsTextView;
//        ImageView imgPost, imgHeart, imgComment;
//
//        public PostViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvContent = itemView.findViewById(R.id.tvContent);
//            tvUsername = itemView.findViewById(R.id.tvUsername);
//            tvTime = itemView.findViewById(R.id.tvTime);
//            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
//            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
//            imgPost = itemView.findViewById(R.id.imgPost);
//            imgHeart = itemView.findViewById(R.id.imgHeart);
//            imgComment = itemView.findViewById(R.id.imgComment);
//            commentsTextView = itemView.findViewById(R.id.commentsTextView);
//        }
//    }
//}
public class PostAdapter_MyProfile extends RecyclerView.Adapter<PostAdapter_MyProfile.ViewHolder> {

    private List<Post_MyProfile> postList;
    private Context context;
    private OnPostClickListener listener;

    public interface OnPostClickListener {
        void onPostClick(Post_MyProfile post, int position);
    }

    public PostAdapter_MyProfile(List<Post_MyProfile> postList, Context context, OnPostClickListener listener) {
        this.postList = postList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post_MyProfile post = postList.get(position);
        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Mapping with XML IDs
        TextView txtUsername, txtTime, txtPostContent, txtHeartCount, txtCommentCount;
        ImageView imgPost, imgHeart, imgComment;

        public ViewHolder(View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txt_username);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtPostContent = itemView.findViewById(R.id.txt_post_content);
            txtHeartCount = itemView.findViewById(R.id.txt_heart_count);
            txtCommentCount = itemView.findViewById(R.id.txt_comment_count);
            imgPost = itemView.findViewById(R.id.img_post);
            imgHeart = itemView.findViewById(R.id.img_heart);
            imgComment = itemView.findViewById(R.id.img_comment);
        }

        public void bind(Post_MyProfile post, int position) {
            // Set text data
            txtUsername.setText(post.getUsername());
            txtTime.setText(post.getTime());
            txtPostContent.setText(post.getContent());
//            txtHeartCount.setText(String.valueOf(post.getHeartCount()));
//            txtCommentCount.setText(String.valueOf(post.getCommentCount()));

            // Load images dynamically if needed
            imgPost.setImageResource(post.getImageResId());
            imgHeart.setImageResource(R.drawable.heart); // Load your heart drawable
            imgComment.setImageResource(R.drawable.comment); // Load your comment drawable

            // Set click listeners
            itemView.setOnClickListener(v -> listener.onPostClick(post, position));
            imgHeart.setOnClickListener(v -> {
                // Handle heart click
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show();
            });

        }
    }
}
