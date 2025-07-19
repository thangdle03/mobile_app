package com.example.admin_stadium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Áp dụng padding cho hệ thống insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Lấy nút btn_schedule
        Button btnSchedule = findViewById(R.id.btn_schedule);
        Button btnStadium = findViewById(R.id.btn_stadium);
        Button btnTime = findViewById(R.id.btn_time);
        Button btnRevenue = findViewById(R.id.btn_revenue);
        Button btnHome = findViewById(R.id.btn_home);
        Button btnAcc = findViewById(R.id.btn_account);

        // Thêm sự kiện click để chuyển sang ScheduleActivity
        btnSchedule.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });

        // Thêm sự kiện click để chuyển sang StadiumActivity
        btnStadium.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StadiumActivity.class);
            startActivity(intent);
        });

        btnTime.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimeActivity.class);
            startActivity(intent);
        });

        btnRevenue.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RevenueActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAcc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        // Initialize Firebase Database with the correct URL
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("timeframe");

        // Check Firebase connection
        DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d(TAG, "Connected to Firebase");
                } else {
                    Log.d(TAG, "Disconnected from Firebase");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Listener was cancelled", error.toException());
            }
        });

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange called");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String startTime = snapshot.child("startTime").getValue(String.class);
                    String endTime = snapshot.child("endTime").getValue(String.class);
                    Log.d(TAG, "Start Time: " + startTime + ", End Time: " + endTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}