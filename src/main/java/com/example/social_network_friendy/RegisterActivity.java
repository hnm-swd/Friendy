//package com.example.social_network_friendy;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class RegisterActivity extends Activity {
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.register_activity_layout);
//
//        // Khởi tạo Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//
//        EditText emaildangki = findViewById(R.id.edtemail);
//        EditText matkhau = findViewById(R.id.edtpassword);
//        EditText xacnhanmatkhau = findViewById(R.id.edtconfirmpassword);
//        Button chonnutdangki = findViewById(R.id.btncreateaccount);
//
//        chonnutdangki.setOnClickListener(view -> {
//            String email = emaildangki.getText().toString();
//            String password = matkhau.getText().toString();
//            String confirmPassword = xacnhanmatkhau.getText().toString();
//
//            if (password.length() >= 8 && password.length() <= 20) {
//                if (password.equals(confirmPassword)) {
//                    mAuth.createUserWithEmailAndPassword(email, password)
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                    startActivity(intent);
//                                } else {
//                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(RegisterActivity.this, "Mật khẩu phải từ 8-20 kí tự", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
//package com.example.social_network_friendy;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//public class RegisterActivity extends Activity {
//
//    private static final String SHARED_PREF_NAME_USER = "myprefuser";
//    private static final String KEY_NAME_USER = "nameuser";
//    private static final String KEY_EMAIL = "email";
//    private static final String KEY_PASSWORD = "password";
//    private static final String KEY_CONFIRMPASSWORD = "confirmpassword";
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.register_activity_layout);
//
//        // cài đặt nút khi nhấn vào textviewdangnhap thì chuyển sang màn hình đăng nhập
//        TextView chontextviewdangnhap = findViewById(R.id.textlogin);
//        chontextviewdangnhap.setOnClickListener(view -> {
//            Intent intentlogin = new Intent(RegisterActivity.this, LoginActivity.class);
//            startActivity(intentlogin);
//        });
//
//        // kết nối tới layout
//        EditText tennguoidung = findViewById(R.id.edtuser);
//        EditText emaildangki = findViewById(R.id.edtemail);
//        EditText matkhau = findViewById(R.id.edtpassword);
//        EditText xacnhanmatkhau = findViewById(R.id.edtconfirmpassword);
//        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME_USER, MODE_PRIVATE);
//        Button chonnutdangki = findViewById(R.id.btncreateaccount);
//
//        // tạo các tên người đăng nhập, email, password với confirmpassword khi nhấn vào thì nhập để lấy so sánh
//        chonnutdangki.setOnClickListener(view -> {
//            String username = tennguoidung.getText().toString();
//            String email = emaildangki.getText().toString();
//            String password = matkhau.getText().toString();
//            String confirmPassword = xacnhanmatkhau.getText().toString();
//            if(confirmPassword.length()<=20&&confirmPassword.length()>=8){
//                // kiểm tra xem password và confirmpassword có trùng với nhau không
//                if (password.equals(confirmPassword)) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString(KEY_NAME_USER, username);
//                    editor.putString(KEY_EMAIL, email);
//                    editor.putString(KEY_PASSWORD, password);
//                    editor.putString(KEY_CONFIRMPASSWORD, confirmPassword);
//                    editor.apply();
//
//                    Toast.makeText(RegisterActivity.this, "Đã tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
//                }
//            }
//            else {
//                Toast.makeText(RegisterActivity.this, "Mật khẩu phải từ 8-20 kí tự", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//    }
//}
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference; // Thay thế Firestore bằng Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_layout);

        // Khởi tạo Firebase Auth và Database
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users"); // Tạo tham chiếu đến nút "users"

        TextView textviewdangnhap = findViewById(R.id.textlogin);
        textviewdangnhap.setOnClickListener(view -> {
            // Chuyển sang màn hình đăng nhập
            Intent intentlogin = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intentlogin);
        });

        EditText emaildangki = findViewById(R.id.edtemail);
        EditText matkhau = findViewById(R.id.edtpassword);
        EditText xacnhanmatkhau = findViewById(R.id.edtconfirmpassword);
        EditText tennguoidung = findViewById(R.id.edtuser); // Thêm phần lấy tên người dùng
        Button chonnutdangki = findViewById(R.id.btncreateaccount);

        chonnutdangki.setOnClickListener(view -> {
            String email = emaildangki.getText().toString();
            String password = matkhau.getText().toString();
            String confirmPassword = xacnhanmatkhau.getText().toString();
            String username = tennguoidung.getText().toString(); // Lấy tên người dùng

            if (password.length() >= 8 && password.length() <= 20) {
                if (password.equals(confirmPassword)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Đăng ký thành công
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                                    // Lưu thông tin người dùng vào Realtime Database
                                    if (user != null) {
                                        String userId = user.getUid();
                                        User newUser = new User(userId, username, email); // Tạo đối tượng User với userId

                                        databaseReference.child(userId).setValue(newUser)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Chuyển sang màn hình đăng nhập
                                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(RegisterActivity.this, "Lưu thông tin người dùng thất bại", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Mật khẩu phải từ 8-20 kí tự", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tạo một lớp User để lưu thông tin người dùng vào Realtime Database
    public static class User {
        private String userId;
        private String username;
        private String email;

        public User() {
            // Constructor mặc định cho Realtime Database
        }

        public User(String userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
