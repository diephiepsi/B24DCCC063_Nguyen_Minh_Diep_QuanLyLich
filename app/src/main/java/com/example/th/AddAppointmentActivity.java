package com.example.th;

import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddAppointmentActivity extends AppCompatActivity {

    private EditText edtTitle;
    private EditText edtLocation;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Switch switchReminder;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        edtTitle = findViewById(R.id.edtTitle);
        edtLocation = findViewById(R.id.edtLocation);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        switchReminder = findViewById(R.id.switchReminder);
        Button btnSave = findViewById(R.id.btnSave);
        ImageButton btnBackHeader = findViewById(R.id.btnBackHeader);

        timePicker.setIs24HourView(true);
        dbHelper = new DatabaseHelper(this);

        btnBackHeader.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveAppointment());
    }

    private void saveAppointment() {
        String title = edtTitle.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError("Không được để trống");
            return;
        }
        if (location.isEmpty()) {
            edtLocation.setError("Không được để trống");
            return;
        }

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar selected = Calendar.getInstance();
        selected.set(year, month, day, hour, minute, 0);
        selected.set(Calendar.MILLISECOND, 0);

        Calendar now = Calendar.getInstance();
        if (!selected.after(now)) {
            Toast.makeText(this, "Thời điểm hẹn phải sau thời điểm hiện tại", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
        String time = String.format(Locale.US, "%02d:%02d", hour, minute);
        boolean reminder = switchReminder.isChecked();

        Appointment appointment = new Appointment(title, location, date, time, reminder);
        long id = dbHelper.insertAppointment(appointment);

        if (id > 0) {
            Toast.makeText(this, "Đã lưu lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Lưu thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
