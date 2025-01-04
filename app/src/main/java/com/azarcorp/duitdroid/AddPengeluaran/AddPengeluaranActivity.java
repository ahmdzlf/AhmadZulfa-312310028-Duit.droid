package com.azarcorp.duitdroid.AddPengeluaran;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.azarcorp.duitdroid.R;
import com.azarcorp.duitdroid.model.ModelDatabase;
import com.azarcorp.duitdroid.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPengeluaranActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GALLERY = 1;
    private static final int REQUEST_CODE_CAMERA = 2;

    private static final String KEY_IS_EDIT = "key_is_edit";
    private static final String KEY_DATA = "key_data";

    private Toolbar toolbar;
    private TextInputEditText etKeterangan, etTanggal, etJmlUang;
    private Button btnSimpan, buttonTambahFoto;
    private ImageView imageViewFoto;

    private AddPengeluaranViewModel addPengeluaranViewModel;
    private boolean mIsEdit = false;
    private int strUid = 0;
    private Uri selectedImageUri;

    public static void startActivity(Context context, boolean isEdit, ModelDatabase pengeluaran) {
        Intent intent = new Intent(context, AddPengeluaranActivity.class);
        intent.putExtra(KEY_IS_EDIT, isEdit);
        intent.putExtra(KEY_DATA, pengeluaran);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        // Inisialisasi Views

        etKeterangan = findViewById(R.id.etKeterangan);
        etTanggal = findViewById(R.id.etTanggal);
        etJmlUang = findViewById(R.id.etJmlUang);
        btnSimpan = findViewById(R.id.btnSimpan);
        buttonTambahFoto = findViewById(R.id.buttonTambahFoto);
        imageViewFoto = findViewById(R.id.imageViewFoto);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        addPengeluaranViewModel = new ViewModelProvider(this).get(AddPengeluaranViewModel.class);

        loadData();
        initActions();
    }

    private void initActions() {
        etTanggal.setOnClickListener(view -> showDatePicker());

        btnSimpan.setOnClickListener(view -> saveData());

        buttonTambahFoto.setOnClickListener(view -> showImagePickerDialog());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            String dateFormat = "d MMMM yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
            etTanggal.setText(sdf.format(calendar.getTime()));
        };
        new DatePickerDialog(this, date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Sumber Foto");
        builder.setItems(new CharSequence[]{"Galeri", "Kamera"}, (dialog, which) -> {
            if (which == 0) {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_CODE_GALLERY, this::openGallery);
            } else {
                requestPermission(Manifest.permission.CAMERA, REQUEST_CODE_CAMERA, this::openCamera);
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    imageViewFoto.setImageURI(selectedImageUri);
                } else {
                    Toast.makeText(this, "Gagal memuat gambar dari galeri", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == REQUEST_CODE_CAMERA) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                if (photo != null) {
                    selectedImageUri = saveImageToInternalStorage(photo);
                    imageViewFoto.setImageURI(selectedImageUri);
                } else {
                    Toast.makeText(this, "Gagal mengambil foto", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Uri saveImageToInternalStorage(Bitmap bitmap) {
        try {
            String filename = "IMG_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getFilesDir(), filename);
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void requestPermission(String permission, int requestCode, Runnable onGranted) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            onGranted.run();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == REQUEST_CODE_GALLERY) openGallery();
            else if (requestCode == REQUEST_CODE_CAMERA) openCamera();
        } else {
            Toast.makeText(this, "Izin diperlukan untuk melanjutkan", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        mIsEdit = getIntent().getBooleanExtra(KEY_IS_EDIT, false);
        if (mIsEdit) {
            ModelDatabase pengeluaran = getIntent().getParcelableExtra(KEY_DATA);
            if (pengeluaran != null) {
                strUid = pengeluaran.uid;
                etKeterangan.setText(pengeluaran.keterangan);
                etTanggal.setText(pengeluaran.tanggal);
                etJmlUang.setText(String.valueOf(pengeluaran.jmlUang));
                if (pengeluaran.fotoUri != null) {
                    selectedImageUri = Uri.parse(pengeluaran.fotoUri);
                    imageViewFoto.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private void saveData() {
        String keterangan = etKeterangan.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String jmlUang = etJmlUang.getText().toString().trim();

        if (keterangan.isEmpty() || tanggal.isEmpty() || jmlUang.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
        } else {
            if (mIsEdit) {
                addPengeluaranViewModel.updatePengeluaran(strUid, keterangan, tanggal, Integer.parseInt(jmlUang),
                        selectedImageUri != null ? selectedImageUri.toString() : null);
            } else {
                addPengeluaranViewModel.addPengeluaran("pengeluaran", keterangan, tanggal, Integer.parseInt(jmlUang),
                        selectedImageUri != null ? selectedImageUri.toString() : null);
            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
