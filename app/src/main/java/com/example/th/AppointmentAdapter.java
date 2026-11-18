package com.example.th;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_appointment, parent, false);
        }

        Appointment item = getItem(position);
        if (item != null) {
            TextView tvTitle = convertView.findViewById(R.id.tvItemTitle);
            TextView tvLocation = convertView.findViewById(R.id.tvItemLocation);
            TextView tvDateTime = convertView.findViewById(R.id.tvItemDateTime);
            TextView tvReminder = convertView.findViewById(R.id.tvItemReminder);

            tvTitle.setText(item.getTitle());
            tvLocation.setText("Địa điểm: " + item.getLocation());
            tvDateTime.setText(item.getDisplayDateTime());

            if (item.isReminder()) {
                tvReminder.setText("Có nhắc");
                tvReminder.setTextColor(ContextCompat.getColor(getContext(), R.color.reminder_on));
                tvReminder.setBackgroundResource(R.drawable.bg_badge_on);
            } else {
                tvReminder.setText("Không nhắc");
                tvReminder.setTextColor(ContextCompat.getColor(getContext(), R.color.reminder_off));
                tvReminder.setBackgroundResource(R.drawable.bg_badge_off);
            }
        }
        return convertView;
    }
}
