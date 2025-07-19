package com.example.admin_stadium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class StadiumDetailActivity extends DialogFragment {

    private static final String ARG_ID = "id";
    private static final String ARG_NAME = "name";
    private static final String ARG_TYPE = "type";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_PRICE = "price";

    private DatabaseReference stadiumRef;

    public static StadiumDetailActivity newInstance(String stadiumId, String name, String type, String location, Double price) {
        StadiumDetailActivity fragment = new StadiumDetailActivity();
        Bundle args = new Bundle();
        args.putString(ARG_ID, stadiumId);
        args.putString(ARG_NAME, name);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_LOCATION, location);
        args.putDouble(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stadium_detail, container, false);

        TextView tvTitle = view.findViewById(R.id.title_stadium_name);
        EditText etName = view.findViewById(R.id.stadium_name);
        EditText etType = view.findViewById(R.id.stadium_type);
        EditText etLocation = view.findViewById(R.id.stadium_location);
        EditText etPrice = view.findViewById(R.id.stadium_price);

        // Lấy dữ liệu từ arguments
        if (getArguments() != null) {
            tvTitle.setText(getArguments().getString(ARG_NAME, "Tên sân"));
            etName.setText(getArguments().getString(ARG_NAME));
            etType.setText(getArguments().getString(ARG_TYPE));
            etLocation.setText(getArguments().getString(ARG_LOCATION));
            etPrice.setText(String.valueOf(getArguments().getDouble(ARG_PRICE, 0.0)));
        }

        // Kết nối Firebase
        stadiumRef = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("stadiums");

        // Các nút
        Button btnDelete = view.findViewById(R.id.btn_delete);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);

        // Xóa sân bóng
        btnDelete.setOnClickListener(v -> {
            String stadiumId = getArguments().getString(ARG_ID);
            if (stadiumId != null) {
                stadiumRef.child(stadiumId).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Sân đã được xóa thành công", Toast.LENGTH_SHORT).show();
                            dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Xóa thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getActivity(), "Không tìm thấy ID sân để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        // Hủy và đóng dialog
        btnCancel.setOnClickListener(v -> dismiss());

        // Lưu thông tin chỉnh sửa
        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String type = etType.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            Double price;

            // Kiểm tra giá trị nhập vào
            try {
                price = Double.valueOf(etPrice.getText().toString().trim().replace(",", ""));
            } catch (NumberFormatException e) {
                Toast.makeText(getActivity(), "Giá không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            String stadiumId = getArguments().getString(ARG_ID);
            if (stadiumId == null || stadiumId.isEmpty()) {
                Toast.makeText(getActivity(), "Không tìm thấy ID sân để cập nhật!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo map cập nhật
            Map<String, Object> updates = new HashMap<>();
            updates.put("name", name);
            updates.put("type", type);
            updates.put("location", location);
            updates.put("price", price);

            stadiumRef.child(stadiumId).updateChildren(updates)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getActivity(), "Thông tin sân đã được cập nhật", Toast.LENGTH_SHORT).show();
                        dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        return view;
    }

    // Class đại diện cho sân bóng
    public static class Stadium {
        public String name;
        public String type;
        public String location;
        public Double price;

        public Stadium() {
        }

        public Stadium(String name, String type, String location, Double price) {
            this.name = name;
            this.type = type;
            this.location = location;
            this.price = price;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("name", name);
            result.put("type", type);
            result.put("location", location);
            result.put("price", price);
            return result;
        }
    }
}
