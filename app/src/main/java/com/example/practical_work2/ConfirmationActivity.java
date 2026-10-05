package com.example.practical_work2;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EventDbHelper;

public class ConfirmationActivity extends AppCompatActivity {
    private TextView txtEvent, txtName, txtRegNo, txtEmail, txtPhone, txtProgramme;
    private Button btnHome, btnUpdate, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // 1. Hubungkan elemen UI daripada XML
        txtEvent = findViewById(R.id.txtEvent);
        txtName = findViewById(R.id.txtName);
        txtRegNo = findViewById(R.id.txtRegNo);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        txtProgramme = findViewById(R.id.txtProgramme);
        btnHome = findViewById(R.id.btnHome);

        // Hubungkan butang baharu untuk Update dan Delete
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // 2. Dapatkan data Intent (termasuk URI rekod pangkalan data)
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String regNo = intent.getStringExtra("regNo");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");
        String programme = intent.getStringExtra("programme");
        String eventName = intent.getStringExtra("eventName");

        // Dapatkan Uri rekod daripada ContentProvider
        String uriString = intent.getStringExtra("recordUri");
        Uri recordUri = uriString != null ? Uri.parse(uriString) : null;

        // 3. Paparkan ringkasan data pada skrin
        txtEvent.setText(eventName);
        txtName.setText("Name: " + name);
        txtRegNo.setText("Reg No: " + regNo);
        txtEmail.setText("Email: " + email);
        txtPhone.setText("Phone: " + phone);
        txtProgramme.setText("Programme: " + programme);

        // 4. Fungsi Butang Update (Kemaskini Rekod melalui ContentResolver)
        btnUpdate.setOnClickListener(v -> {
            if (recordUri != null) {
                ContentValues values = new ContentValues();
                String updatedName = txtName.getText().toString().replace("Name: ", "") + " (Updated)";
                values.put(EventDbHelper.COLUMN_NAME, updatedName);

                int rowsUpdated = getContentResolver().update(recordUri, values, null, null);
                if (rowsUpdated > 0) {
                    txtName.setText("Name: " + updatedName);
                    Toast.makeText(ConfirmationActivity.this, "Rekod berjaya dikemaskini!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ConfirmationActivity.this, "URI Rekod tidak dijumpai!", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Fungsi Butang Delete (Padam Rekod melalui ContentResolver)
        btnDelete.setOnClickListener(v -> {
            if (recordUri != null) {
                int rowsDeleted = getContentResolver().delete(recordUri, null, null);
                if (rowsDeleted > 0) {
                    Toast.makeText(ConfirmationActivity.this, "Rekod berjaya dipadam!", Toast.LENGTH_SHORT).show();
                    finish(); // Kembali ke skrin sebelumnya selepas memadam
                }
            } else {
                Toast.makeText(ConfirmationActivity.this, "URI Rekod tidak dijumpai!", Toast.LENGTH_SHORT).show();
            }
        });

        // 6. Fungsi Butang Home
        btnHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(ConfirmationActivity.this, MainActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        });
    }
}