package com.example.social_network_friendy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Adapter_otherprofile extends RecyclerView.Adapter<Adapter_otherprofile.PostViewHolder> {
    Context context;
    ArrayList<Post_otherprofile> postOtherprofiles;

    public Adapter_otherprofile(Context context, ArrayList<Post_otherprofile> postOtherprofiles) {
        this.context = context;
        this.postOtherprofiles = postOtherprofiles;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post_otherprofile postOtherprofile = postOtherprofiles.get(position);
        holder.txtUserName.setText(postOtherprofile.getUserName());
        holder.txtTime.setText(postOtherprofile.getTimePosted());
        holder.txtContent.setText(postOtherprofile.getContent());
        holder.imgProfile.setImageResource(postOtherprofile.getProfileImage());
        holder.imgPost.setImageResource(postOtherprofile.getPostImage());
        holder.txtLikes.setText(postOtherprofile.getLikes() + " likes");
        holder.txtComments.setText(postOtherprofile.getComments() + " comments");
    }

    @Override
    public int getItemCount() {
        return postOtherprofiles.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtTime, txtContent, txtLikes, txtComments;
        ImageView imgProfile,imgPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_username);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtContent = itemView.findViewById(R.id.txt_post_content);
            imgProfile = itemView.findViewById(R.id.img_avatar);
            imgPost=itemView.findViewById(R.id.img_post);
            txtLikes = itemView.findViewById(R.id.txt_heart_count);
            txtComments = itemView.findViewById(R.id.txt_comment_count);
        }
    }
}
