package com.example.th;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AppointmentDetailActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvLocation;
    private TextView tvDateTime;
    private TextView tvReminderStatus;
    private Switch switchReminder;
    private DatabaseHelper dbHelper;
    private int appointmentId;
    private Appointment appointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvDateTime = findViewById(R.id.tvDetailDateTime);
        tvReminderStatus = findViewById(R.id.tvReminderStatus);
        switchReminder = findViewById(R.id.switchDetailReminder);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);
        ImageButton btnBackHeader = findViewById(R.id.btnBackHeader);

        dbHelper = new DatabaseHelper(this);
        appointmentId = getIntent().getIntExtra("id", -1);

        if (appointmentId < 0) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadAppointment();

        btnBackHeader.setOnClickListener(v -> finish());
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadAppointment() {
        appointment = dbHelper.getAppointmentById(appointmentId);
        if (appointment == null) {
            Toast.makeText(this, "Không tìm thấy lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvTitle.setText(appointment.getTitle());
        tvLocation.setText(appointment.getLocation());
        tvDateTime.setText(appointment.getDisplayDateTime());

        switchReminder.setOnCheckedChangeListener(null);
        switchReminder.setChecked(appointment.isReminder());
        updateReminderStatus(appointment.isReminder());
        switchReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dbHelper.updateReminder(appointmentId, isChecked);
            appointment.setReminder(isChecked);
            updateReminderStatus(isChecked);
        });
    }

    private void updateReminderStatus(boolean on) {
        tvReminderStatus.setText(on ? "Đang bật" : "Đang tắt");
        tvReminderStatus.setTextColor(ContextCompat.getColor(this,
                on ? R.color.reminder_on : R.color.reminder_off));
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa lịch hẹn")
                .setMessage("Bạn có chắc muốn xóa lịch hẹn này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dbHelper.deleteAppointment(appointmentId);
                    Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
