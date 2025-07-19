package com.example.admin_stadium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateTimeFrame extends DialogFragment {

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        // Tạo View từ layout tùy chỉnh
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_time_frame, null); // Đảm bảo layout này trỏ đúng tới file XML của bạn

        // Thiết lập TimePicker dạng 24 giờ
        TimePicker timePickerStart = view.findViewById(R.id.timePickerStart);
        timePickerStart.setIs24HourView(true);
        TimePicker timePickerEnd = view.findViewById(R.id.timePickerEnd);
        timePickerEnd.setIs24HourView(true);

        // Các nút hành động trong popup
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            // Xử lý hành động Hủy
            dismiss();
        });

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // Xử lý hành động Lưu
            int startHour = timePickerStart.getHour();
            int startMinute = timePickerStart.getMinute();
            String newStartTime = String.format("%02d:%02d", startHour, startMinute);

            int endHour = timePickerEnd.getHour();
            int endMinute = timePickerEnd.getMinute();
            String newEndTime = String.format("%02d:%02d", endHour, endMinute);

            // Kiểm tra điều kiện giờ kết thúc phải sau giờ bắt đầu
            if (endHour > startHour || (endHour == startHour && endMinute > startMinute)) {
                saveTimeFrame(newStartTime, newEndTime);
            } else {
                Toast.makeText(getActivity(), "Giờ kết thúc phải sau giờ bắt đầu", Toast.LENGTH_SHORT).show();
            }
        });

        // Tạo và trả về Dialog
        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void saveTimeFrame(String startTime, String endTime) {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("timeframe");

        // Create a new timeframe object
        DatabaseReference newTimeFrameRef = myRef.push();
        newTimeFrameRef.child("startTime").setValue(startTime);
        newTimeFrameRef.child("endTime").setValue(endTime);

        Toast.makeText(getActivity(), "Khung giờ mới đã được tạo", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}