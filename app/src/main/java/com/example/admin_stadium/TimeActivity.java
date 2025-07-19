package com.example.admin_stadium;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimeActivity extends AppCompatActivity {

    private LinearLayout listSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_time);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listSchedule = findViewById(R.id.list_schedule);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ImageButton btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(v -> showPopupStart());

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("timeframe");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear existing views
                listSchedule.removeAllViews();

                // Iterate through all children
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String startTime = snapshot.child("startTime").getValue(String.class);
                    String endTime = snapshot.child("endTime").getValue(String.class);
                    addTimeCard(startTime, endTime);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void showPopupStart() {
        // Hiển thị DialogFragment
        CreateTimeFrame dialogFragment = new CreateTimeFrame();
        dialogFragment.show(getSupportFragmentManager(), "createTimeFrame");
    }

    private void addTimeCard(String startTime, String endTime) {
        CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.time_card, listSchedule, false);
        TextView textView = cardView.findViewById(R.id.time_text);
        textView.setText(startTime + " - " + endTime);

        cardView.setOnClickListener(v -> {
            ChangeTime changeTime = ChangeTime.newInstance(startTime, endTime);
            changeTime.show(getSupportFragmentManager(), "changeTime");
        });

        listSchedule.addView(cardView);
    }
}