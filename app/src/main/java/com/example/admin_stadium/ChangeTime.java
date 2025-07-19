package com.example.admin_stadium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeTime extends DialogFragment {

    private static final String ARG_START_TIME = "start_time";
    private static final String ARG_END_TIME = "end_time";

    public static ChangeTime newInstance(String startTime, String endTime) {
        ChangeTime fragment = new ChangeTime();
        Bundle args = new Bundle();
        args.putString(ARG_START_TIME, startTime);
        args.putString(ARG_END_TIME, endTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        // Tạo View từ layout tùy chỉnh
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_change_time, null); // Đảm bảo layout này trỏ đúng tới file XML của bạn

        // Thiết lập TimePicker dạng 24 giờ
        TimePicker timePickerStart = view.findViewById(R.id.timePickerStart);
        timePickerStart.setIs24HourView(true);
        TimePicker timePickerEnd = view.findViewById(R.id.timePickerEnd);
        timePickerEnd.setIs24HourView(true);

        // Lấy giờ bắt đầu và giờ kết thúc từ arguments và thiết lập cho TimePicker
        if (getArguments() != null) {
            String startTime = getArguments().getString(ARG_START_TIME);
            String[] startTimeParts = startTime.split(":");
            int startHour = Integer.parseInt(startTimeParts[0]);
            int startMinute = Integer.parseInt(startTimeParts[1]);
            timePickerStart.setHour(startHour);
            timePickerStart.setMinute(startMinute);

            String endTime = getArguments().getString(ARG_END_TIME);
            String[] endTimeParts = endTime.split(":");
            int endHour = Integer.parseInt(endTimeParts[0]);
            int endMinute = Integer.parseInt(endTimeParts[1]);
            timePickerEnd.setHour(endHour);
            timePickerEnd.setMinute(endMinute);
        }

        // Các nút hành động trong popup
        Button btnDelete = view.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(v -> {
            // Xử lý hành động Xóa
            if (getArguments() != null) {
                String startTime = getArguments().getString(ARG_START_TIME);
                String endTime = getArguments().getString(ARG_END_TIME);
                deleteTimeFrame(startTime, endTime);
            }
            dismiss();
        });

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            // Xử lý hành động Hủy
            dismiss();
        });

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> {
            // Xử lý hành động Lưu
            if (getArguments() != null) {
                int startHour = timePickerStart.getHour();
                int startMinute = timePickerStart.getMinute();
                String newStartTime = String.format("%02d:%02d", startHour, startMinute);

                int endHour = timePickerEnd.getHour();
                int endMinute = timePickerEnd.getMinute();
                String newEndTime = String.format("%02d:%02d", endHour, endMinute);

                // Kiểm tra điều kiện giờ kết thúc phải sau giờ bắt đầu
                if (endHour > startHour || (endHour == startHour && endMinute > startMinute)) {
                    updateTimeFrame(newStartTime, newEndTime);
                } else {
                    Toast.makeText(getActivity(), "Giờ kết thúc phải sau giờ bắt đầu", Toast.LENGTH_SHORT).show();
                }
            }
            dismiss();
        });

        // Tạo và trả về Dialog
        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void updateTimeFrame(String newStartTime, String newEndTime) {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("timeframe");

        // Query to find the specific timeframe to update
        myRef.orderByChild("startTime").equalTo(getArguments().getString(ARG_START_TIME)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("endTime").getValue(String.class).equals(getArguments().getString(ARG_END_TIME))) {
                        snapshot.getRef().child("startTime").setValue(newStartTime);
                        snapshot.getRef().child("endTime").setValue(newEndTime);
                        Toast.makeText(getActivity(), "Khung giờ đã được cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void deleteTimeFrame(String startTime, String endTime) {
        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("timeframe");

        // Query to find the specific timeframe to delete
        myRef.orderByChild("startTime").equalTo(startTime).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("endTime").getValue(String.class).equals(endTime)) {
                        snapshot.getRef().removeValue();
                        Toast.makeText(getActivity(), "Khung giờ đã được xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}