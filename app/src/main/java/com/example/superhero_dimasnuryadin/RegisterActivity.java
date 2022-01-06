package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText myEmail, myPassword;
    private Button regButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String getEmail, getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisiasi Widget dan Membuat Objek dari Firebase Authentication
        myEmail = findViewById(R.id.regEmail);
        myPassword = findViewById(R.id.regPassword);
        regButton = findViewById(R.id.regUser);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();

        // Menyembunyikan / Hide Password
        myPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekDataUser();
            }
        });
    }

    // Method ini digunakan untuk mengecek dan mendapatkan yang dimasukan user
    private void cekDataUser() {
        // Mendapatkan data yang diinputkan user
        getEmail = myEmail.getText().toString();
        getPassword = myPassword.getText().toString();

        // Mengecek apakah email dan sandi kosong atau tidak
        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
            Toast.makeText(this, "Email atau sandi Tidak boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            // Mengecek panjang karakter password baru yang akan didaftarkan
            if (getPassword.length() < 6) {
                Toast.makeText(this, "Sandi Terlalu pendek, Minimal 6 Karakter", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                createUserAccount();
            }
        }
    }

    // Method ini digunakan untuk membuat akun baru user
    private void createUserAccount() {
        auth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Mengecek status keberhasilan saat mendaftarkan email dan sandi baru
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}