package com.example.ethiostore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import io.paperdb.Paper;

public class Settings_Activity extends AppCompatActivity {

    private ImageView backArrow;
    private TextView language, logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        Paper.init(this);
        language = (TextView) findViewById(R.id.lang);
        logout = (TextView) findViewById(R.id.logout);
        backArrow = (ImageView) findViewById(R.id.close_setting);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Settings_Activity.this, Home_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeLanguageDialog();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(Settings_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showChangeLanguageDialog()
    {
        final String[] listItems = {"English","አማርኛ","Afaan Oromo"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Settings_Activity.this);
        mBuilder.setTitle("ChooseLanguage");
        mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    setLocale("en");
                    recreate();
                }
                else if (which == 1)
                {
                    setLocale("am");
                    recreate();
                }
                else if (which == 2)
                {
                    setLocale("om");
                    recreate();
                }
                dialog.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }

    private void setLocale(String language)
    {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My lang", language);
        editor.apply();
    }

    public void loadLocale()
    {
        SharedPreferences preferences = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String lang = preferences.getString("My lang","");
        setLocale(lang);
    }
}