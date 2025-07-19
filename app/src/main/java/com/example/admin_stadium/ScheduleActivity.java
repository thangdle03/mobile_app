package com.example.admin_stadium;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule); // Đảm bảo file activity_schedule.xml tồn tại

        // Lấy CardView từ layout
        CardView cardButton = findViewById(R.id.card_campnou);

        // Thiết lập sự kiện click cho CardView
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị DialogFragment
                showPopup();
            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void showPopup() {
        // Hiển thị DialogFragment
       ScheduleDetailActivity dialogFragment = new ScheduleDetailActivity();
        dialogFragment.show(getSupportFragmentManager(), "scheduleDetail");
    }

}
