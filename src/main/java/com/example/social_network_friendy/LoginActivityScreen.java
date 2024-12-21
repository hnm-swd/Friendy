package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        mAuth = FirebaseAuth.getInstance();

        EditText nhapsdtemail = findViewById(R.id.edtphoneemail);
        EditText nhapmatkhau = findViewById(R.id.edtpassword);
        Button nutdangnhap = findViewById(R.id.btnlogin);

        nutdangnhap.setOnClickListener(view -> {
            String email = nhapsdtemail.getText().toString();
            String password = nhapmatkhau.getText().toString();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Intent intentmainscreen = new Intent(LoginActivity.this, NewsFeedActivity.class);
                            intentmainscreen.putExtra("email", email);
                            startActivity(intentmainscreen);
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        TextView chontextviewdangki = findViewById(R.id.textcreateaccount);
        chontextviewdangki.setOnClickListener(view -> {
            Intent intentcreateaccount = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentcreateaccount);
        });
    }
}
