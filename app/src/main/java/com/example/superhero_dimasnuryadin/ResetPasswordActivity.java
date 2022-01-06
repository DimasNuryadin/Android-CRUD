package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    // Deklarasi Variabel
    private EditText myEmail;
    private Button ResetButton;
    private ProgressBar progressBar;
    private FirebaseAuth user;
    private String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Inisiasi Widget dan Membuat Objek dari FirebaseUser
        myEmail = findViewById(R.id.resetPassword);
        ResetButton = findViewById(R.id.reset);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        user = FirebaseAuth.getInstance();

        ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getEmail = myEmail.getText().toString().trim();

                // Melakukan Proses Reset Password, dengan Memasukan alamat email pengguna
                user.sendPasswordResetEmail(getEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Mengecek status keberhasilan saat proses reset password
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "Silahkan Cek Inbox pada Email Anda", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Terjadi Kesalahan, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}