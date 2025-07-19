package com.example.admin_stadium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CreateStadium extends DialogFragment {

    private DatabaseReference stadiumRef;

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        // Tạo View từ layout tùy chỉnh
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_stadium, null); // Đảm bảo layout này trỏ đúng tới file XML của bạn

        EditText etName = view.findViewById(R.id.et_stadium_name);
        EditText etType = view.findViewById(R.id.et_stadium_type);
        EditText etLocation = view.findViewById(R.id.et_stadium_location);
        EditText etPrice = view.findViewById(R.id.et_stadium_price);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.btn_save);

        stadiumRef = FirebaseDatabase.getInstance("https://user-stadium-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("stadiums");

        btnCancel.setOnClickListener(v -> {
            // Xử lý hành động Hủy
            dismiss();
        });

        btnSave.setOnClickListener(v -> {
            // Xử lý hành động Lưu
            String name = etName.getText().toString();
            String type = etType.getText().toString();
            String location = etLocation.getText().toString();
            Double price = Double.valueOf(etPrice.getText().toString().replace(",", ""));

            String stadiumId = stadiumRef.push().getKey();
            Stadium newStadium = new Stadium(name, type, location, price);

            if (stadiumId != null) {
                stadiumRef.child(stadiumId).setValue(newStadium)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getActivity(), "Sân mới đã được tạo", Toast.LENGTH_SHORT).show();
                            dismiss();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Tạo sân thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Tạo và trả về Dialog
        return new android.app.AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

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