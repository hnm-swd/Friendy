package com.example.social_network_friendy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class USER_FINDING_ADAPTER extends RecyclerView.Adapter<USER_FINDING_ADAPTER.UserViewHolder> {

    private final ArrayList<String> userList;
    private final OnItemClickListener listener;

    public USER_FINDING_ADAPTER(ArrayList<String> userList, OnItemClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_userfinding, parent, false);
        return new UserViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        String username = userList.get(position);
        holder.usernameTextView.setText(username);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(username));// Sự kiện click vào item

        // Load avatar
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

    private Bitmap decodeBase64ToBitmap(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public int getItemCount() {
        return userList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String username); // Gọi khi người dùng nhấn vào username
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTextView;
        CircleImageView avatarImageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvmeofinding); // Gắn view TextView
            avatarImageView = itemView.findViewById(R.id.avatarImageView); // Initialize ImageView
        }
    }
}