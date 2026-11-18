package com.example.th;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appointments.db";
    private static final int DATABASE_VERSION = 1;

    // Bảng users
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Bảng appointments
    public static final String TABLE_APPOINTMENTS = "appointments";
    public static final String COL_APP_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_LOCATION = "location";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";
    public static final String COL_REMINDER = "reminder";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE " + TABLE_USERS + " ("
                + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_USERNAME + " TEXT NOT NULL UNIQUE, "
                + COL_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsers);

        String createAppointments = "CREATE TABLE " + TABLE_APPOINTMENTS + " ("
                + COL_APP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT NOT NULL, "
                + COL_LOCATION + " TEXT NOT NULL, "
                + COL_DATE + " TEXT NOT NULL, "
                + COL_TIME + " TEXT NOT NULL, "
                + COL_REMINDER + " INTEGER NOT NULL)";
        db.execSQL(createAppointments);

        // Tài khoản mẫu
        insertUser(db, "admin", "123456");

        // 3 lịch hẹn mẫu
        insertAppointment(db, new Appointment("Họp nhóm đồ án", "Phòng B203", "2026-10-25", "09:00", true));
        insertAppointment(db, new Appointment("Khám răng", "Nha khoa Minh Tâm", "2026-10-26", "14:30", true));
        insertAppointment(db, new Appointment("Đi cà phê với bạn", "Highland Coffee", "2026-10-27", "19:00", false));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // --- Users ---

    public void insertUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        insertUser(db, username, password);
        db.close();
    }

    private void insertUser(SQLiteDatabase db, String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{COL_USER_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null
        );
        boolean found = cursor != null && cursor.moveToFirst();
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return found;
    }

    // --- Appointments ---

    public long insertAppointment(Appointment a) {
        SQLiteDatabase db = getWritableDatabase();
        long id = insertAppointment(db, a);
        db.close();
        return id;
    }

    private long insertAppointment(SQLiteDatabase db, Appointment a) {
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, a.getTitle());
        values.put(COL_LOCATION, a.getLocation());
        values.put(COL_DATE, a.getDate());
        values.put(COL_TIME, a.getTime());
        values.put(COL_REMINDER, a.isReminder() ? 1 : 0);
        return db.insert(TABLE_APPOINTMENTS, null, values);
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_APPOINTMENTS,
                null,
                null, null, null, null,
                COL_DATE + " ASC, " + COL_TIME + " ASC"
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToAppointment(cursor));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public Appointment getAppointmentById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_APPOINTMENTS,
                null,
                COL_APP_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null
        );
        Appointment appointment = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                appointment = cursorToAppointment(cursor);
            }
            cursor.close();
        }
        db.close();
        return appointment;
    }

    public void updateReminder(int id, boolean on) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_REMINDER, on ? 1 : 0);
        db.update(TABLE_APPOINTMENTS, values, COL_APP_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAppointment(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_APPOINTMENTS, COL_APP_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int countAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_APPOINTMENTS, null);
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    public int countReminders() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM " + TABLE_APPOINTMENTS + " WHERE " + COL_REMINDER + "=1",
                null
        );
        int count = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        db.close();
        return count;
    }

    private Appointment cursorToAppointment(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_APP_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
        String location = cursor.getString(cursor.getColumnIndexOrThrow(COL_LOCATION));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));
        String time = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIME));
        boolean reminder = cursor.getInt(cursor.getColumnIndexOrThrow(COL_REMINDER)) == 1;
        return new Appointment(id, title, location, date, time, reminder);
    }
}
