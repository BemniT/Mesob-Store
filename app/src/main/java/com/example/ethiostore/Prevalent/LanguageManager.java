package com.example.ethiostore.Prevalent;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManager {
    public static void loadLocale(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String lang = preferences.getString("My lang", "");
        setLocale(activity, lang);
    }

    public static void setLocale(Activity activity, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        Resources resources = activity.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
