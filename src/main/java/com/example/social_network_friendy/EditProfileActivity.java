
package com.example.social_network_friendy;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;



import java.util.Calendar;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

//public class EditProfileActivity extends Activity {
//    private EditText bioEditText;
//    private EditText birthDateEditText;
//    private AutoCompleteTextView locationEditText;
//    private TextView doneTextView;
//    private TextView cancelTextView;  // Thêm TextView cho nút Hủy
//
//    private ArrayList<String> locationSuggestions = new ArrayList<>();
//
//    private FirebaseDatabase firebaseDatabase;
//    private DatabaseReference profileRef;
//    private DatabaseReference locationRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.edit_myprofile);
//
//        bioEditText = findViewById(R.id.bioEditText);
//        birthDateEditText = findViewById(R.id.birthDateEditText);
//        locationEditText = findViewById(R.id.locationEditText);
//        doneTextView = findViewById(R.id.tvDone);
//        cancelTextView = findViewById(R.id.tvCancel);  // Khai báo TextView cho nút Hủy
//
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        profileRef = firebaseDatabase.getReference("user_profiles/current_user");
//        locationRef = firebaseDatabase.getReference("location_suggestions");
//
//        doneTextView.setOnClickListener(v -> saveProfileInfo());
//        cancelTextView.setOnClickListener(v -> cancelEditing());
//
//        // Load location suggestions from Firebase
//        loadLocationSuggestions();
//
//        birthDateEditText.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                showDatePickerDialog();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private void showDatePickerDialog() {
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(
//                this,
//                (view, selectedYear, selectedMonth, selectedDay) -> {
//                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//                    birthDateEditText.setText(selectedDate);
//                },
//                year, month, day
//        );
//        datePickerDialog.show();
//    }
//
//    private void loadLocationSuggestions() {
//        locationRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                locationSuggestions.clear(); // Clear existing list
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String location = snapshot.getValue(String.class);
//                    locationSuggestions.add(location);
//                }
//
//                // Set the adapter for AutoCompleteTextView with the loaded location suggestions
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfileActivity.this, android.R.layout.simple_dropdown_item_1line, locationSuggestions);
//                locationEditText.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(EditProfileActivity.this, "Lỗi khi tải gợi ý địa điểm", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveProfileInfo() {
//        String bio = bioEditText.getText().toString().trim();
//        String birthDate = birthDateEditText.getText().toString().trim();
//        String location = locationEditText.getText().toString().trim();
//
//        // Prepare data for Firebase
//        if (bio.isEmpty() && birthDate.isEmpty() && location.isEmpty()) {
//            Toast.makeText(EditProfileActivity.this, "Hãy điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Store data in Firebase Realtime Database
//        profileRef.child("bio").setValue(bio);
//        profileRef.child("birthDate").setValue(birthDate);
//        profileRef.child("location").setValue(location)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(EditProfileActivity.this, "Thông tin đã lưu", Toast.LENGTH_SHORT).show();
//                        // Trở về MyProfileActivity sau khi lưu
//                        finish();
//                    } else {
//                        Toast.makeText(EditProfileActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    // Phương thức để xử lý khi bấm Hủy
//    private void cancelEditing() {
//        // Không lưu thông tin và trở về MyProfileActivity
//        finish();
//    }
//}
public class EditProfileActivity extends Activity {
    private EditText bioEditText;
    private EditText birthDateEditText;
    private AutoCompleteTextView locationEditText;
    private TextView doneTextView;
    private TextView cancelTextView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference profileRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_myprofile);

        bioEditText = findViewById(R.id.bioEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        locationEditText = findViewById(R.id.locationEditText);
        doneTextView = findViewById(R.id.tvDone);
        cancelTextView = findViewById(R.id.tvCancel);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // Thay đổi đường dẫn từ "user_profiles" sang "users"
        profileRef = firebaseDatabase.getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        // Load existing data into fields
        loadProfileData();

        doneTextView.setOnClickListener(v -> saveProfileInfo());
        cancelTextView.setOnClickListener(v -> cancelEditing());

        birthDateEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                showDatePickerDialog();
                return true;
            }
            return false;
        });
    }

    private void loadProfileData() {
        profileRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                bioEditText.setText(snapshot.child("bio").getValue(String.class));
                birthDateEditText.setText(snapshot.child("birthDate").getValue(String.class));
                locationEditText.setText(snapshot.child("location").getValue(String.class));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi tải thông tin cá nhân", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveProfileInfo() {
        String bio = bioEditText.getText().toString().trim();
        String birthDate = birthDateEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        // Store data in Firebase
        profileRef.child("bio").setValue(bio);
        profileRef.child("birthDate").setValue(birthDate);
        profileRef.child("location").setValue(location).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditProfileActivity.this, "Thông tin đã lưu", Toast.LENGTH_SHORT).show();
                finish(); // Close this activity and return to MyProfileActivity
            } else {
                Toast.makeText(EditProfileActivity.this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelEditing() {
        finish();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    birthDateEditText.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}
