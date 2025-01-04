package com.azarcorp.duitdroid;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class About_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity); // Pastikan nama file XML-nya sesuai

        // Setup Toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Menambahkan tombol back
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(""); // Kosongkan agar teks di TextView tampil di tengah
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle klik tombol back
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Kembali ke fragment sebelumnya
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

