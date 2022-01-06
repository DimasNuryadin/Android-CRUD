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
import com.google.firebase.auth.FirebaseUser;

public class ChangeEmailActivity extends AppCompatActivity {
    // Deklarasi Variabel
    private EditText myEmail;
    private Button ChangeButton;
    private ProgressBar progressBar;
    private FirebaseUser user;
    private String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        // Inisiasi Widget dan Membuat Objek dari FirebaseUser
        myEmail = findViewById(R.id.changeEmail);
        ChangeButton = findViewById(R.id.change);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        user = FirebaseAuth.getInstance().getCurrentUser();

        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getEmail = myEmail.getText().toString().trim();

                // Melakukan Proses Update, dengan memasukan email email baru
                user.updateEmail(getEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        // Mengecek status keberhasilan saat proses update Password
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangeEmailActivity.this, "Email Berhasil Diubah", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            finish();
                        } else {
                            Toast.makeText(ChangeEmailActivity.this, "Terjadi Kesalahan, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}