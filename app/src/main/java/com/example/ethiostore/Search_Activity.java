package com.example.ethiostore;

import static android.media.CamcorderProfile.get;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.Manifest;
import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.View_Holder.Game_VerticalAdapter;
import com.example.ethiostore.View_Holder.VerticalAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Search_Activity extends AppCompatActivity {

    private EditText search_input;
    private ImageView searchBtn, backArrow, searchVoice;
    private DatabaseReference appRef, gameRef;
    private RecyclerView verticalRecyclerView;
    private String text;
    private  int speech_rec = 102;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        verticalRecyclerView = findViewById(R.id.vertical_searchRecycler);
        search_input = findViewById(R.id.searchInput);
        searchBtn = findViewById(R.id.searchBtn);
        backArrow = findViewById(R.id.close_search);
        searchVoice = findViewById(R.id.search_voice1);


        text = getIntent().getStringExtra("input");


        appRef = FirebaseDatabase.getInstance().getReference().child("Apps");
        gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        search_input.setText(text);
        searchVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndAskSpeechInput();
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchIn = search_input.getText().toString();
                searchTheKeyword(searchIn);
            }
        });
    }


    private void checkPermissionAndAskSpeechInput() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            askSpeechInput();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                askSpeechInput();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == speech_rec && resultCode == Activity.RESULT_OK) {
            // Assuming 'data' is an Intent object
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                search_input.setText(result.get(0));
            } else {
                // Handle case where result is null or empty
            }
        }
    }

    private void askSpeechInput() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Toast.makeText(this, "Speech recognition not available", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
            startActivityForResult(intent, speech_rec);
        }
    }

    private void searchTheKeyword(String searchIn) {
        String searchLowerCase = searchIn.toLowerCase();
        String searchUpperCase = searchIn.toUpperCase();

        Query appsQuery = appRef.orderByChild("app_name");
        Query gamesQuery = gameRef.orderByChild("sname");

        List<Apps> searchResults = new ArrayList<>();
        List<Games> searchResultGame = new ArrayList<>();

        appsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Apps apps = dataSnapshot.getValue(Apps.class);
                    if (apps != null && (apps.getApp_name().toLowerCase().contains(searchLowerCase) || apps.getApp_name().toUpperCase().contains(searchUpperCase))) {
                        searchResults.add(apps);
                    }
                }
                updateRecyclerView(searchResults);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });

        gamesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Games games = dataSnapshot.getValue(Games.class);
                    if (games != null && (games.getApp_name().toLowerCase().contains(searchLowerCase) || games.getApp_name().toUpperCase().contains(searchUpperCase))) {
                        searchResultGame.add(games);
                    }
                }
                updateRecyclerViewForGames(searchResultGame);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }

    private void updateRecyclerView(List<Apps> searchResults) {
        if (!searchResults.isEmpty()) {
            VerticalAdapter verticalAdapter = new VerticalAdapter(searchResults, Search_Activity.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(Search_Activity.this, 1);
            verticalRecyclerView.setLayoutManager(gridLayoutManager);
            verticalRecyclerView.setAdapter(verticalAdapter);
        }
    }

    private void updateRecyclerViewForGames(List<Games> searchResultsGame) {
        if (!searchResultsGame.isEmpty()) {
            Game_VerticalAdapter verticalAdapter = new Game_VerticalAdapter(searchResultsGame, Search_Activity.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(Search_Activity.this, 1);
            verticalRecyclerView.setLayoutManager(gridLayoutManager);
            verticalRecyclerView.setAdapter(verticalAdapter);
        }
    }
}
