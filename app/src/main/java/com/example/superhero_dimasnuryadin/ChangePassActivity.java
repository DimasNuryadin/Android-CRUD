package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {
    // Deklarasi Variabel
    private EditText myPassword;
    private Button ChangeButton;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private String getPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        // Inisiasi Widget dan Membuat Objek dari FirebaseUser
        myPassword = findViewById(R.id.changePassword);
        ChangeButton = findViewById(R.id.change);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Menyembunyikan / Hide Password
        myPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getPassword = myPassword.getText().toString().trim();

                // Melakukan Proses Update, Dengan memasukan password baru
                user.updatePassword(getPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Mengecek Status keberhasilan saat proses update password
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "Password Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        } else {
                            Toast.makeText(ChangePassActivity.this, "Terjadi Kesalahan, Silahkan coba lagi", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}