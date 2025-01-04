package com.azarcorp.duitdroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUs_Activity extends AppCompatActivity {

    private EditText nameInput, emailInput, phoneInput;
    private Spinner dropdown;
    private Button submitButton;
    private LinearLayout contactDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus_activity);

        // Inisialisasi elemen UI
        ImageView backButton = findViewById(R.id.backButton);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        dropdown = findViewById(R.id.dropdown);
        submitButton = findViewById(R.id.submitButton);
        contactDetails = findViewById(R.id.contactDetails);

        // Aksi kembali ke fragment sebelumnya
        backButton.setOnClickListener(view -> onBackPressed());

        // Aksi untuk tombol submit
        submitButton.setOnClickListener(view -> sendEmail());

        // Aksi untuk nomor telepon (menghubungi WhatsApp)
        TextView phoneTextView = contactDetails.findViewById(R.id.contactDetails).findViewById(R.id.phone_text);
        phoneTextView.setOnClickListener(view -> openWhatsApp(phoneTextView.getText().toString()));

        // Aksi untuk email
        TextView emailTextView = contactDetails.findViewById(R.id.contactDetails).findViewById(R.id.email_text);
        emailTextView.setOnClickListener(view -> sendEmailDirectly(emailTextView.getText().toString()));

        // Aksi untuk GitHub
        TextView githubTextView = contactDetails.findViewById(R.id.contactDetails).findViewById(R.id.github_text);
        githubTextView.setOnClickListener(view -> openGitHub(githubTextView.getText().toString()));
    }

    private void sendEmail() {
        String name = nameInput.getText().toString();
        String email = emailInput.getText().toString();
        String phone = phoneInput.getText().toString();
        String subject = "Formulir Contact Us";
        String message = "Nama: " + name + "\nEmail: " + email + "\nNomor Telepon: " + phone;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"julpaahmad7@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(intent, "Pilih aplikasi email"));
        } catch (android.content.ActivityNotFoundException e) {
            Toast.makeText(this, "Tidak ada aplikasi email yang terpasang.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsApp(String phoneNumber) {
        Uri uri = Uri.parse("https://wa.me/" + phoneNumber.replaceAll("[^0-9]", ""));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "WhatsApp tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmailDirectly(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Tidak dapat membuka aplikasi email.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGitHub(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Tidak dapat membuka URL.", Toast.LENGTH_SHORT).show();
        }
    }
}

