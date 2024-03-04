package com.example.ethiostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ethiostore.Model.Account;
import com.example.ethiostore.Prevalent.Continity;
import com.example.ethiostore.Prevalent.LanguageManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 2000;

    private Button getStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager.loadLocale(this);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        String userPhonekey = Paper.book().read(Continity.userPhonekey);
        String userPassword = Paper.book().read(Continity.userPassword);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(userPhonekey) && !TextUtils.isEmpty(userPassword)) {
                    authenticate(userPhonekey, userPassword);
                } else {
                    startLoginActivity();
                }
            }
        }, SPLASH_TIMEOUT);
    }

    private void authenticate(final String phoneNumber, final String password) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Users").child(phoneNumber).exists()) {
                    Account userData = snapshot.child("Users").child(phoneNumber).getValue(Account.class);
                    if (userData != null && userData.getPhone().equals(phoneNumber) && userData.getPassword().equals(password)) {
                        Toast.makeText(MainActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                        Continity.currentOnlineUser = userData;
                        startHomeActivity();
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "This Phone Number is not Registered!", Toast.LENGTH_LONG).show();
                    Toast.makeText(MainActivity.this, "Create Account First", Toast.LENGTH_SHORT).show();
                    startLoginActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(MainActivity.this, Home_Activity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent coming back to it when pressing back button from Home activity
    }

    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, Login_Activity.class);
        startActivity(intent);
        finish(); // Finish this activity to prevent coming back to it when pressing back button from Login activity
    }
}