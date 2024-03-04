package com.example.ethiostore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Notification_Activity extends AppCompatActivity {

    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        backArrow = (ImageView) findViewById(R.id.close_notification);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Notification_Activity.this, Home_Activity.class);
                startActivity(intent);
            }
        });
    }
}