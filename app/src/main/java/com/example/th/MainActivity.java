package com.example.th;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalCount;
    private TextView tvReminderCount;
    private ListView listView;
    private DatabaseHelper dbHelper;
    private AppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvReminderCount = findViewById(R.id.tvReminderCount);
        listView = findViewById(R.id.listAppointments);
        Button btnAdd = findViewById(R.id.btnAdd);

        dbHelper = new DatabaseHelper(this);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddAppointmentActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Appointment selected = adapter.getItem(position);
            if (selected != null) {
                Intent intent = new Intent(MainActivity.this, AppointmentDetailActivity.class);
                intent.putExtra("id", selected.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        List<Appointment> appointments = dbHelper.getAllAppointments();
        adapter = new AppointmentAdapter(this, appointments);
        listView.setAdapter(adapter);

        int total = dbHelper.countAll();
        int reminders = dbHelper.countReminders();
        tvTotalCount.setText(String.valueOf(total));
        tvReminderCount.setText(String.valueOf(reminders));
    }
}
