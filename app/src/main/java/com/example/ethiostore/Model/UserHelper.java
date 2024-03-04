package com.example.ethiostore.Model;



import android.content.Context;
import android.content.SharedPreferences;

public class UserHelper {
    private Context context;

    public UserHelper(Context context) {
        this.context = context;
    }

    public void setProfilePicture(String uriString){
        SharedPreferences picturePref = context.getSharedPreferences("profilePicture", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = picturePref.edit();
        editor.putString("profilePicture", uriString);
        editor.apply();
    }

    public String getProfilePicture(){
        SharedPreferences picturePref = context.getSharedPreferences("profilePicture", Context.MODE_PRIVATE);

        return picturePref.getString("profilePicture", "");
    }
}
