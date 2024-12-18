package com.example.social_network_friendy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivityScreen extends Activity {
    private static final String SHARED_PREF_NAME_USER = "myprefuser";
    private static final String KEY_NAME_USER = "nameuser";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONFIRMPASSWORD = "confirmpassword";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME_USER, MODE_PRIVATE);

        // Kết nối phần tử trong layout login
        EditText nhapsdtemail = findViewById(R.id.edtphoneemail);
        EditText nhapmatkhau = findViewById(R.id.edtpassword);
        Button nutdangnhap = findViewById(R.id.btnlogin);

        // Đặt sự kiện cho nút btnlogin
        nutdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailnhaptrangdangnhap = nhapsdtemail.getText().toString();
                String passwordnhaptrangdangnhap = nhapmatkhau.getText().toString();

                // Lấy thông tin đã đăng ký từ SharedPreferences
                String nhapemaildangki = sharedPreferences.getString(KEY_EMAIL, null);
                String nhapmatkhaudangki = sharedPreferences.getString(KEY_PASSWORD, null);

                // So sánh
                if (emailnhaptrangdangnhap.equals(nhapemaildangki) && passwordnhaptrangdangnhap.equals(nhapmatkhaudangki)) {
                    Toast.makeText(LoginActivityScreen.this, "Xin chào!", Toast.LENGTH_SHORT).show();
                    Intent intentmainscreen = new Intent(LoginActivityScreen.this, MainActivityScreen.class);
                    intentmainscreen.putExtra("nhaptennguoidungdangki", nhapemaildangki);
                    startActivity(intentmainscreen);
                } else {
                    Toast.makeText(LoginActivityScreen.this, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Kết nối với text Đăng ký
        TextView chontextviewdangki = findViewById(R.id.textcreateaccount);
        chontextviewdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcreateaccount = new Intent(LoginActivityScreen.this, RegisterActivityScreen.class);
                startActivity(intentcreateaccount);
            }
        });

        // Đăng nhập qua các logo mạng xã hội
        ImageView facebook = findViewById(R.id.logofb);
        ImageView insta = findViewById(R.id.logoinsta);
        ImageView gmail = findViewById(R.id.logogmail);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://vi-vn.facebook.com/");
            }
        });
        insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://www.instagram.com/");
            }
        });
        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://workspace.google.com/intl/vi/gmail/");
            }
        });
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}