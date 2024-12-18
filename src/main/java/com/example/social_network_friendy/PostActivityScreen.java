package com.example.social_network_friendy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import androidx.cardview.widget.CardView;

public class PostActivityScreen extends Activity {

    private Button btnCancel, btnPost;
    private EditText editTitle, editContent;
    private ImageView imageUpload;
    private TextView textViewUserName;
    private LinearLayout imageContainer;  // For holding multiple uploaded images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpost_activity_layout);

        // Initialize the UI components
        btnCancel = findViewById(R.id.btnCancel);
        btnPost = findViewById(R.id.btnPost);
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editText);
        imageUpload = findViewById(R.id.ImageView);
        textViewUserName = findViewById(R.id.textViewUserName);
        imageContainer = findViewById(R.id.imageContainer);

        // Set the username dynamically (replace with the actual username)
        textViewUserName.setText("app_name");

        // Handle "Cancel" button click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and go back
            }
        });

        // Handle "Post" button click
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postTitle = editTitle.getText().toString();
                String postContent = editContent.getText().toString();

                if (postTitle.isEmpty() || postContent.isEmpty()) {
                    Toast.makeText(PostActivityScreen.this, "Title or content cannot be empty!", Toast.LENGTH_SHORT).show();
                } else {
                    // Here you would handle the actual posting logic (e.g., sending data to a server)
                    Toast.makeText(PostActivityScreen.this, "Post submitted!", Toast.LENGTH_SHORT).show();

                    // Clear the fields after posting
                    editTitle.setText("");
                    editContent.setText("");
                    imageContainer.removeAllViews(); // Clear uploaded images

                    // Optionally, return to the previous screen
                    finish();
                }
            }
        });

        // Handle image upload button click
        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch an intent to choose an image from the gallery
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1); // Request code 1 for image selection
            }
        });
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            // Tạo CardView cho ảnh
            CardView cardView = new CardView(this);
            cardView.setLayoutParams(new LinearLayout.LayoutParams(220, 220));
            cardView.setCardElevation(4);
            cardView.setCardBackgroundColor(Color.WHITE);
            cardView.setRadius(20); // Bo góc cho CardView

            // Thiết lập margin cho CardView
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            params.setMargins(10, 10, 10, 10); // Khoảng cách giữa các CardView
            cardView.setLayoutParams(params);

            // Tạo RelativeLayout để chứa ImageView và ImageButton
            RelativeLayout imageWrapper = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            imageWrapper.setLayoutParams(layoutParams);

            // Tạo ImageView để hiển thị ảnh đã tải lên
            ImageView uploadedImage = new ImageView(this);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            uploadedImage.setLayoutParams(imageParams);
            uploadedImage.setImageURI(selectedImage);
            uploadedImage.setScaleType(ImageView.ScaleType.CENTER_CROP); // Giúp ảnh vừa khít khung

            // Tạo nút xóa (ImageButton) và đặt nó nằm ở góc phải trên của ảnh
            ImageButton deleteButton = new ImageButton(this);
            RelativeLayout.LayoutParams deleteParams = new RelativeLayout.LayoutParams(50, 50);
            deleteParams.addRule(RelativeLayout.ALIGN_PARENT_END); // Canh phải
            deleteParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);   // Canh trên
            deleteButton.setLayoutParams(deleteParams);
            deleteButton.setImageResource(android.R.drawable.ic_delete);
            deleteButton.setBackgroundColor(Color.TRANSPARENT); // Để nền trong suốt

            // Thêm ImageView và ImageButton vào RelativeLayout
            imageWrapper.addView(uploadedImage);
            imageWrapper.addView(deleteButton);

            // Thêm RelativeLayout vào CardView
            cardView.addView(imageWrapper);

            // Thêm CardView vào imageContainer
            imageContainer.addView(cardView);

            // Xử lý sự kiện khi nút xóa được nhấn
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageContainer.removeView(cardView); // Xóa CardView thay vì RelativeLayout
                }
            });
        }
    }
}
