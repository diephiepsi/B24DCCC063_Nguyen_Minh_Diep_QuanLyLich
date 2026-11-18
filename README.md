# Ứng dụng Quản lý lịch hẹn (Android)

Project Java + XML + SQLite theo đề bài thực hành.

## Cài đặt trên Android Studio

1. Mở Android Studio → **File → Open** → chọn thư mục `AppointmentManager`
2. Đợi Gradle sync xong
3. Chạy trên emulator hoặc máy thật (Run ▶)

> Nếu thiếu icon PNG ở `mipmap-hdpi`, Android Studio có thể cảnh báo nhẹ trên API thấp; API 26+ dùng adaptive icon sẵn có.

## Tài khoản đăng nhập

| Tên đăng nhập | Mật khẩu |
|---------------|----------|
| admin         | 123456   |

## Cấu trúc chính

```
app/src/main/java/com/example/th/
  Appointment.java              // Model
  DatabaseHelper.java           // SQLiteOpenHelper
  LoginActivity.java            // Màn 1: Đăng nhập
  MainActivity.java             // Màn 2: Danh sách
  AddAppointmentActivity.java   // Màn 3: Thêm
  AppointmentDetailActivity.java// Màn 4: Chi tiết
  AppointmentAdapter.java       // Adapter ListView
```

## Cơ sở dữ liệu

- File: `appointments.db` (version 1)
- Bảng `users`: id, username, password
- Bảng `appointments`: id, title, location, date (yyyy-MM-dd), time (HH:mm), reminder (0/1)

## Luồng màn hình

LoginActivity → MainActivity → AddAppointmentActivity  
MainActivity → AppointmentDetailActivity (truyền `id` qua Intent)
