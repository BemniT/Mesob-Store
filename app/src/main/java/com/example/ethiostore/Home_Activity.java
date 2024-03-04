package com.example.ethiostore;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethiostore.Prevalent.Continity;
import com.example.ethiostore.Prevalent.LanguageManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home_Activity extends AppCompatActivity {


    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    private int speech_rec = 12;
    private String text;
    BottomNavigationView bottomNavigationView;
    private ImageView notification, voiceSearch;
    private EditText searchBar;


    //        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//       if (savedInstanceState == null) {
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new App_fragment()).commit();
//        navigationView.setCheckedItem(R.id.nav_home);
    //}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager.loadLocale(this);
        setContentView(R.layout.activity_home);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        notification  = findViewById(R.id.notificationBtn);
        searchBar = findViewById(R.id.search_bar);
        voiceSearch = findViewById(R.id.search_voice);
        searchBar.setClickable(false);
        searchBar.setFocusable(false);

        voiceSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askSpeechInput();
            }
        });


        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Notification_Activity.class);
                startActivity(intent);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Search_Activity.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Activity.this, Notification_Activity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);

        replaceFragment(new App_fragment());

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            final int ID = item.getItemId();

            switch (ID) {
                case R.id.app:

                    replaceFragment(new App_fragment());
                    break;

                case R.id.games:
                    replaceFragment(new Game_Fragment());
                    break;

                case R.id.user:
                    showBottomDialog();
                    break;
            }

            return true;
        });
//       String fragmentLoadGame = getIntent().getStringExtra("fragmentToLoad");
//       String fragmentLoadApp = getIntent().getStringExtra("fragmentToLoad");
//       if (fragmentLoadGame.equals("bookFragment"))
//       {
//           getSupportFragmentManager().beginTransaction()
//                   .replace(R.id.fragment_layout,new BookFragment())
//                   .commit();
//       }
//       else if(fragmentLoadApp.equals("appFragment"))
//       {
//           getSupportFragmentManager().beginTransaction()
//                   .replace(R.id.fragment_layout,new App_fragment())
//                   .commit();
//       }else {
//
//       }

//       fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showBottomDialog();
//            }
//        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Load locale to refresh the language
        LanguageManager.loadLocale(this);
    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == speech_rec && resultCode == Activity.RESULT_OK) { // Fix here
            // Assuming 'data' is an Intent object
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                text = (result.get(0));
                Intent intent = new Intent(Home_Activity.this, Search_Activity.class);
                intent.putExtra("input",text);
                startActivity(intent);
            } else {
                // Handle case where result is null or empty
            }
        }
    }


    private void askSpeechInput()
    {
        if (!SpeechRecognizer.isRecognitionAvailable(this))
        {
            Toast.makeText(this, "Speech recognition not availble", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something!");
            startActivityForResult(intent, speech_rec);
        }
    }


    private void showBottomDialog() {
        TextView name, email;
        CircleImageView profilePic;
        DatabaseReference profileRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Continity.currentOnlineUser.getPhone());


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        name = dialog.findViewById(R.id.user_name);
        email = dialog.findViewById(R.id.user_phone);
        profilePic = dialog.findViewById(R.id.profile_picture);

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child("name").getValue().toString();
                String userEmail = snapshot.child("email").getValue().toString();
                String profilePicUrl = snapshot.child("image").getValue().toString();

                name.setText(userName);
                if (userEmail.isEmpty())
                {
                    email.setText("Insert email in profile");
                }else {
                    email.setText(userEmail);
                }
                if (profilePicUrl.isEmpty())
                {
                    Picasso.get().load(R.drawable.profile).into(profilePic);
                }else {
                    Picasso.get().load(profilePicUrl).into(profilePic);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        LinearLayout edit_profile = dialog.findViewById(R.id.edit_profile);
        LinearLayout downloads = dialog.findViewById(R.id.downloads);
        LinearLayout saved = dialog.findViewById(R.id.save_content);
        LinearLayout notification = dialog.findViewById(R.id.notifiy_offer);
        LinearLayout settings = dialog.findViewById(R.id.settings);
        LinearLayout helpFeed = dialog.findViewById(R.id.help_feedback);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Find the BottomNavigationItemView for the "Apps" item
                bottomNavigationView.setSelectedItemId(R.id.app);

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Home_Activity.this, Edit_profileActivity.class);
                startActivity(intent);
            }
        });


        downloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Home_Activity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });

        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Home_Activity.this, Saved_content_Activity.class);
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(Home_Activity.this, Settings_Activity.class);
                startActivity(intent);
            }
        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Home_Activity.this, Notification_Activity.class);
                startActivity(intent);
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        if (window != null)
        {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = MATCH_PARENT; // Set your desired width in pixels
            layoutParams.height = 1125; // Set your desired height in pixels
            int horizontalPadding = 20;
            window.setAttributes(layoutParams);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setWindowAnimations(R.style.DalogAnimation);
            window.setGravity(Gravity.CENTER);
            window.getDecorView().setPadding(horizontalPadding, 0, horizontalPadding, 0);
        }


    }

}
