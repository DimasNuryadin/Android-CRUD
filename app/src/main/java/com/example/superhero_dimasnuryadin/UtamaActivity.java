package com.example.superhero_dimasnuryadin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UtamaActivity extends AppCompatActivity implements View.OnClickListener {
    // Deklarasi Variable
    private FirebaseAuth.AuthStateListener authListener, authStateListener;
    private FirebaseAuth auth;


    // CRUD
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static UtamaActivity ma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);
        Button Logout = findViewById(R.id.logout);
        Button Pengaturan = findViewById(R.id.pengaturan);
        // Instance firebase Auth
        auth = FirebaseAuth.getInstance();


        // CRUD
        Button btn = (Button) findViewById(R.id.tambah);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent inte = new Intent(UtamaActivity.this, BuatBiodata.class);
                startActivity(inte);
            }
        });

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList();


        // Firebase
        // Menambahkan Listener Untuk mengecek apakah userteleh logout / Keluar
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // Jika iya atau null maka akan pindah pada halaman login
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(UtamaActivity.this, "Logout Succes", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UtamaActivity.this, MainActivity.class));
                    finish();
                }
            }
        };


        // Facebook
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // jika user sebelumnya telah logout
                // saat aplikasi di buka kembalu, activitynya akan ke MainActivity
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(UtamaActivity.this, MainActivity.class));
                    finish();
                }
            }
        };


        Logout.setOnClickListener(this);

        Pengaturan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UtamaActivity.this, Setting.class);
                startActivity(i);
            }
        });
    }

    // Menerapkan Listener
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        auth.addAuthStateListener(authStateListener);
    }

    // Melepaskan listener
    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }


    // Logout Onclick
    @Override
    public void onClick(View v) {
        auth.signOut();
        switch (v.getId()){
            case R.id.logout:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(UtamaActivity.this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
        }
    }
    // CRUD
    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodataHero", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc< cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        ListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat Biodata", "Ubah Biodata", "Hapus Biodata"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UtamaActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item){
                            case 0 :
                                Intent i = new Intent(getApplicationContext(), LihatBiodata.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            case 1 :
                                Intent in = new Intent(getApplicationContext(), UpdateBiodata.class);
                                in.putExtra("nama", selection);
                                startActivity(in);
                                break;
                            case 2 :
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from biodataHero where nama = '"+selection+"'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
            }});
        ((ArrayAdapter)ListView01.getAdapter()).notifyDataSetInvalidated();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}