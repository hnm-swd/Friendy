
package com.example.social_network_friendy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {
    private EditText commentEditText;
    private ImageView btnSubmitComment;
    private RecyclerView commentsRecyclerView;
    private CommentAdapter commentsAdapter;
    private List<Comment> commentList;
    private String postId;
    private DatabaseReference commentsRef;
    private DatabaseReference postRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);

        // Initialize views
        commentList = new ArrayList<>();
        commentsRef = FirebaseDatabase.getInstance().getReference("comments");

        // Setup RecyclerView for displaying comments
        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentsAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setAdapter(commentsAdapter);

        commentEditText = findViewById(R.id.commentEditText);
        btnSubmitComment = findViewById(R.id.sendButton);

        // Setup ImageView for going back to NewsFeedActivity
        ImageView imgBack = findViewById(R.id.goBack);
        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(CommentActivity.this, NewsFeedActivity.class);
            startActivity(intent);
        });

        // Get post ID and username from Intent
        postId = getIntent().getStringExtra("postId");

        // Load comments for the current post
        loadComments(postId);

        // Setup button to submit a comment
        btnSubmitComment.setOnClickListener(v -> submitComment());
    }

    // Method to load comments from Firebase
    private void loadComments(String postId) {
        commentsRef.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();  // Clear previous comments
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    Comment comment = commentSnapshot.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentsAdapter.notifyDataSetChanged();  // Update RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentActivity.this, "Failed to load comments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to submit a new comment to Firebase
    private void submitComment() {
        String commentText = commentEditText.getText().toString().trim();

        // Check if comment text is empty
        if (commentText.isEmpty()) {
            Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the current user details (username and avatar)
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String avatarUrl = dataSnapshot.child("avatarUrl").getValue(String.class);

                if (username == null) {
                    username = "Unknown User"; // Default to "Unknown User" if no username is found
                }

                // Generate a unique ID for the new comment
                String commentId = commentsRef.push().getKey();
                long timestamp = System.currentTimeMillis();

                // Create a new Comment object
                Comment newComment = new Comment(username, commentText, timestamp, avatarUrl);

                // Save the comment to Firebase
                commentsRef.child(postId).child(commentId).setValue(newComment)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // After successfully submitting the comment, update the comment count on the post
                                postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);
                                postRef.child("commentCount").get().addOnSuccessListener(snapshot -> {
                                    int currentCommentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                                    postRef.child("commentCount").setValue(currentCommentCount + 1);  // Increment comment count
                                });

                                // Clear the comment input field
                                commentEditText.setText("");
                                Toast.makeText(CommentActivity.this, "Comment posted!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CommentActivity.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CommentActivity.this, "Failed to load user details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
