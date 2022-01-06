package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable Login Email dan Password
    private Button Register, Login;
    private EditText myEmail, myPassword;
    private TextView ResetPW;

    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private String getEmail, getPassword;

    // Google
    private ImageButton LoginG;
    // Membuat Kode Permintaan
    private int RC_SIGN_IN = 1;

    // Facebook
    private FirebaseAuth firebaseAuth; //untuk authentikasi
    private FirebaseAuth.AuthStateListener authStateListener;
    private LoginButton loginFB;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Inisiasi Widget
        myEmail = findViewById(R.id.getEmail);
        myPassword = findViewById(R.id.getPassword);
        Register = findViewById(R.id.btnRegister);
        Register.setOnClickListener(this);
        Login = findViewById(R.id.btnLogin);
        Login.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        ResetPW = findViewById(R.id.resetPW);
        ResetPW.setOnClickListener(this);

        // Login google
        LoginG = findViewById(R.id.loginG);
        LoginG.setOnClickListener(this);

        // Facebook
        loginFB = findViewById(R.id.login_fb); // Inisisasi facebook login button
        callbackManager = CallbackManager.Factory.create();
        // Mengizinkan button untuk membaca email dan profil
        loginFB.setReadPermissions("email", "public_profile");
        loginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //menghandle saat login kita sukses
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                //handle saat tidak jadi login
            }
            @Override
            public void onError(FacebookException error) {
                //handle saat terjadi error
                Toast.makeText(getApplicationContext(), "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
        //proses mengkoneksikan android dengan firebase
        FirebaseConnect();


        // Menyembunyikan / Hide Password
        myPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // Instance / Membuat Objek Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Mengecek Keberadaan User
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Mengecek apakah ada user yang sudah login / belum logout
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Jika ada maka langsung pindah ke halama main activity
                    startActivity(new Intent(MainActivity.this, UtamaActivity.class));
                    finish();
                }
            }
        };
    }

    // Menerapkan listener
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(listener);

        // Facebook
        // jika user masuk/login, maka akan menangani kejadian apakah user telah masuk atau tidak
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    // Melepaskan listener
    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) {
            auth.removeAuthStateListener(listener);
        }
        if (authStateListener !=null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    // Merhod Ini digunakan untuk proses autentikasi user menggunakan email dan kata sandi
    private void loginUserAccount() {
        auth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // Mengecek Status keberhasilan saat login
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Succes", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Tidak dapat masuk, silahkan coba lagi", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister :
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                break;
            case R.id.btnLogin :
                // Mendapatkan data yang diinput user
                getEmail = myEmail.getText().toString();
                getPassword = myPassword.getText().toString();

                // Mengecek apakah email dan sandi kosong atau tidak
                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)) {
                    Toast.makeText(this, "Email dan Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                } else {
                    loginUserAccount();
                    progressBar.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.resetPW :
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.loginG :
                // Statement program login/masuk
                startActivityForResult(AuthUI.getInstance().getInstance()
                        .createSignInIntentBuilder()

                        // Memilih provide atau method masuk yang kita gunakan
                        .setAvailableProviders(Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false)
                        .build(), RC_SIGN_IN);
                progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    // Login Google

    // Mengecek apakah data yang kosong, akan digunakan lllllllll
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        // RC_SIGN_IN adalah kode permintaan yang anda berikan ke start ActivityForResul, saat memluai masuknya arus.
        if (requestCode == RC_SIGN_IN) {
            // Berhasil masuk
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Login Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        }

        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Facebook
    private void FirebaseConnect() {
        // ini method yang berisi kumpulan konfigurasi untuk menghubungkan dengan firebase
        firebaseAuth = FirebaseAuth.getInstance();
        // autentikasi listener
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //jika user sebelumnya telah masuk.login dan belum logout
                // saat aplikasi dibuka kembali, aktivity ini akan langsung kita alihkan ke activity mainmenu
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(MainActivity.this, UtamaActivity.class));
                    finish();
                }
            }
        };
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        //handle kejadian saat user berhasil login
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Autentikasi Berhasil", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Autentikasi Gagal", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}