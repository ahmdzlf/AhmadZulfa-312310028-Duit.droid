package com.azarcorp.duitdroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Activity extends AppCompatActivity {

    // Deklarasi elemen-elemen UI
    private ImageView backButton, profilePicture, editProfileIcon;
    private TextView nameText, emailPhoneText;
    private CardView editProfileCard;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Pastikan sesuai dengan nama layout XML Anda

        // Inisialisasi elemen-elemen UI
        backButton = findViewById(R.id.back_button);
        profilePicture = findViewById(R.id.profile_picture);
        editProfileIcon = findViewById(R.id.edit_profile_icon);
        nameText = findViewById(R.id.user_name);
        emailPhoneText = findViewById(R.id.email_phone);
        editProfileCard = findViewById(R.id.edit_profile_card);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Dapatkan pengguna yang sedang login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Set nama pengguna (jika tersedia)
            String displayName = currentUser.getDisplayName();
            if (displayName != null && !displayName.isEmpty()) {
                nameText.setText(displayName);
            } else {
                nameText.setText("Nama Pengguna");
            }

            // Set email pengguna
            emailPhoneText.setText(currentUser.getEmail());

            // Set foto profil pengguna
            if (currentUser.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(currentUser.getPhotoUrl()) // URL foto profil
                        .placeholder(R.drawable.ic_profile) // Gambar placeholder
                        .error(R.drawable.ic_profile) // Gambar jika terjadi error
                        .into(profilePicture);
            } else {
                // Jika tidak ada foto profil, gunakan gambar default
                profilePicture.setImageResource(R.drawable.ic_profile);
            }
        } else {
            // Jika pengguna tidak login
            nameText.setText("Nama Tidak Diketahui");
            emailPhoneText.setText("Email Tidak Tersedia");
            profilePicture.setImageResource(R.drawable.ic_profile); // Gambar default
        }

        // Event Listener untuk tombol kembali
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kembali ke layar sebelumnya
                finish();
            }
        });

        // Event Listener untuk FrameLayout "Contact Us"
        findViewById(R.id.contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke Contact Us Activity
                Intent intent = new Intent(Profile_Activity.this, ContactUs_Activity.class);
                startActivity(intent);
            }
        });

        // Event Listener untuk FrameLayout "About Duit.Droid"
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Berpindah ke About Activity
                Intent intent = new Intent(Profile_Activity.this, About_Activity.class);
                startActivity(intent);
            }
        });
    }
}
