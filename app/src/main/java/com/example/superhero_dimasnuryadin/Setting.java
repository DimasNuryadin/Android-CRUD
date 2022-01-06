package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    // Deklarasi Variabel
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Button ChangePass, ChangeEmail, DeleteAccount, ResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ChangePass = findViewById(R.id.changePass);
        ChangePass.setOnClickListener(this);
        ChangeEmail = findViewById(R.id.changeEmail);
        ChangeEmail.setOnClickListener(this);
        DeleteAccount = findViewById(R.id.deleteAccount);
        DeleteAccount.setOnClickListener(this);
        ResetPassword = findViewById(R.id.resetPassword);
        ResetPassword.setOnClickListener(this);

        // Instance Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Menambahkan Listener untuk mengecek apakah user telah logout / keluar
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                // Jika iya atau null maka akan pindah ke halaman login
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(Setting.this, "Logout Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Setting.this, MainActivity.class));
                    finish();
                }
            }
        };
    }

    // Menerapkan Listener
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    // Melepaskan Listener
    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changePass :
                startActivity(new Intent(Setting.this, ChangePassActivity.class));
                break;

            case R.id.changeEmail :
                startActivity(new Intent(Setting.this, ChangeEmailActivity.class));
                break;

            case  R.id.deleteAccount :
                FirebaseUser user = auth.getCurrentUser();
                // ProgressBar.setVisibility(View.VISIBLE);
                if (user != null) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Setting.this, "Akun Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Setting.this, "Terjadi Kesalahan, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                break;

            case R.id.resetPassword :
                startActivity(new Intent(Setting.this, ResetPasswordActivity.class));
                break;

        }
    }
}