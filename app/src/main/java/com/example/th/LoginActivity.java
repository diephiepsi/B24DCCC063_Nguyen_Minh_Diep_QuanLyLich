package com.example.th;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private TextView tvError;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        tvError = findViewById(R.id.tvError);
        Button btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new DatabaseHelper(this);
        tvError.setVisibility(View.GONE);

        btnLogin.setOnClickListener(v -> checkLogin());
    }

    private void checkLogin() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            tvError.setText("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu");
            tvError.setVisibility(View.VISIBLE);
            return;
        }

        if (dbHelper.checkLogin(username, password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Không cho Back quay lại màn đăng nhập
        } else {
            tvError.setText("Sai tên đăng nhập hoặc mật khẩu");
            tvError.setVisibility(View.VISIBLE);
        }
    }
}
