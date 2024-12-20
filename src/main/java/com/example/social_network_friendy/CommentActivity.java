package com.example.social_network_friendy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

    public class CommentActivity extends AppCompatActivity {
        private EditText commentEditText;
        private Button btnSubmitComment;
        private String postId;
        private DatabaseReference commentsRef;
        private DatabaseReference postRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_comment);  // Define your layout here

            commentEditText = findViewById(R.id.commentEditText);
            btnSubmitComment = findViewById(R.id.btnSubmitComment);

            // Get the post ID from the Intent
            postId = getIntent().getStringExtra("postId");
            commentsRef = FirebaseDatabase.getInstance().getReference("comments");
            postRef = FirebaseDatabase.getInstance().getReference("posts").child(postId);
            // Set up the button to submit the comment
            btnSubmitComment.setOnClickListener(v -> submitComment());
        }

//        private void submitComment(String postId, String commentText) {
//            DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference("comments").child(postId);
//
//            String commentId = commentsRef.push().getKey();  // Generate a new ID for the comment
//            if (commentId != null) {                Comment comment = new Comment(commentText, FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), System.currentTimeMillis());
//                commentsRef.child(commentId).setValue(comment)
//                        .addOnSuccessListener(aVoid -> {
//                            Toast.makeText(CommentActivity.this, "Comment posted successfully!", Toast.LENGTH_SHORT).show();
//                            finish(); // Optionally close activity after posting
//                        })
//                        .addOnFailureListener(e -> {
//                            Toast.makeText(CommentActivity.this, "Failed to post comment: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                        });
//            }
//        }
private void submitComment() {
    String commentText =commentEditText.getText().toString().trim();

    if (!commentText.isEmpty()) {
        // Tạo đối tượng bình luận mới
        String commentId = commentsRef.push().getKey(); // Tạo ID ngẫu nhiên cho bình luận
        Comment newComment = new Comment(commentId, commentText, System.currentTimeMillis());

        // Lưu bình luận vào Firebase
        commentsRef.child(postId).child(commentId).setValue(newComment)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sau khi bình luận được lưu thành công, tăng số lượng comment của bài viết
                        postRef.child("commentCount").get().addOnSuccessListener(snapshot -> {
                            int currentCommentCount = snapshot.exists() ? snapshot.getValue(Integer.class) : 0;
                            postRef.child("commentCount").setValue(currentCommentCount + 1); // Tăng số lượt bình luận lên 1
                        });

                        // Trở lại MainHomeScreen sau khi bình luận thành công
                        setResult(RESULT_OK);
                        finish(); // Đóng CommentActivity
                    } else {
                        Toast.makeText(CommentActivity.this, "Không thể gửi bình luận!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
    }

