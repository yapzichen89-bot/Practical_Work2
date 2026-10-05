package com.example.practical_work2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EventContentProvider;
import com.example.EventDbHelper;

public class RegistrationActivity extends AppCompatActivity {
    private EditText edtName, edtRegNo, edtEmail, edtPhone, edtProgramme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // TODO 1: Connect all fields and button
        edtName = findViewById(R.id.edtName);
        edtRegNo = findViewById(R.id.edtRegNo);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtProgramme = findViewById(R.id.edtProgramme);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            // TODO 2: Read input values
            String name = edtName.getText().toString().trim();
            String regNo = edtRegNo.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String programme = edtProgramme.getText().toString().trim();

            // TODO 3: Validate required fields, email and phone
            if (!validateInput(name, regNo, email, phone)) {
                return;
            }
            if (programme.isEmpty()) {
                edtProgramme.setError("Programme is required");
                edtProgramme.requestFocus();
                return;
            }

            // TODO 4: Save Name, Registration No and Event Name locally
            SharedPreferences prefs = getSharedPreferences("EventPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", name);
            editor.putString("regNo", regNo);
            editor.putString("eventName", "TECH INNOVATION DAY 2026");
            editor.apply();

            // Simpan data melalui ContentResolver
            ContentValues values = new ContentValues();
            values.put(EventDbHelper.COLUMN_NAME, name);
            values.put(EventDbHelper.COLUMN_REG_NO, regNo);
            values.put(EventDbHelper.COLUMN_EMAIL, email);
            values.put(EventDbHelper.COLUMN_PHONE, phone);
            values.put(EventDbHelper.COLUMN_PROGRAMME, programme);
            values.put(EventDbHelper.COLUMN_EVENT_NAME, "TECH INNOVATION DAY 2026");

            Uri newUri = getContentResolver().insert(EventContentProvider.CONTENT_URI, values);

            // TODO 5: Pass registration data to ConfirmationActivity
            Intent intent = new Intent(RegistrationActivity.this, ConfirmationActivity.class);
            if (newUri != null) {
                intent.putExtra("recordUri", newUri.toString());
            }
            intent.putExtra("name", name);
            intent.putExtra("regNo", regNo);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            intent.putExtra("programme", programme);
            intent.putExtra("eventName", "TECH INNOVATION DAY 2026");
            startActivity(intent);
        });
    }

    private boolean validateInput(String name, String regNo,
                                  String email, String phone) {
        // TODO: Complete validation
        if (name.isEmpty()) {
            edtName.setError("Full Name is required");
            edtName.requestFocus();
            return false;
        }
        if (regNo.isEmpty()) {
            edtRegNo.setError("Registration Number is required");
            edtRegNo.requestFocus();
            return false;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Invalid email format");
            edtEmail.requestFocus();
            return false;
        }
        if (phone.isEmpty()) {
            edtPhone.setError("Phone Number is required");
            edtPhone.requestFocus();
            return false;
        }
        if (!phone.matches("^0\\d{9,10}$")) {
            edtPhone.setError("Invalid phone number");
            edtPhone.requestFocus();
            return false;
        }
        return true;
    }
}
