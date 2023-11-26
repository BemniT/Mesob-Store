package com.example.ethiostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Books;
import com.example.ethiostore.Model.Games;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class App_Game_DetailActivity extends AppCompatActivity {

    private TextView softwareName, softwareCategories, softwareDescription,
            softwareRating, softwareSize, softwareNumDownload, softwareDescriptionBtn, softwareReviewBtn;
    private ImageView softwareImage, softwareBack;
    private Button softwareInstall;
    private RecyclerView image_slider;

    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1;
    private String isDownloaded;
    private String fileName;
    int currentProgress;
    private ProgressDialog progressDialog;
    private String Sid, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_game_detail);

        softwareName = (TextView) findViewById(R.id.software_name);
        softwareCategories = (TextView) findViewById(R.id.software_categories);
        softwareSize = (TextView) findViewById(R.id.software_size);
        softwareDescription = (TextView) findViewById(R.id.software_description);
        softwareRating = (TextView) findViewById(R.id.software_rating);
        softwareNumDownload = (TextView) findViewById(R.id.software_numDownload);
        softwareDescriptionBtn = (TextView) findViewById(R.id.software_descriptionBtn);
        softwareReviewBtn = (TextView) findViewById(R.id.software_reviewBtn);

        softwareInstall = (Button) findViewById(R.id.software_download);
        softwareImage = (ImageView) findViewById(R.id.software_image);

        image_slider = (RecyclerView) findViewById(R.id.software_image_slider);

         type = getIntent().getStringExtra("type").toString();
         Sid = getIntent().getStringExtra("sid").toString();

        getDownloadStatus();

        if (type.equals("Apps"))
        {
            fetchAppDataToDisplay();
        } else if (type.equals("Games")) {
            fetchGamesDataToDisplay();
        }else {

        }


    }



    private void getDownloadStatus()
    {
        if (type.equals("Apps"))
        {
            DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("Apps").child(Sid);
            appRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    Apps apps = snapshot.getValue(Apps.class);
                    isDownloaded = apps.getIsDownloaded();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (type.equals("Games")) {
            DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("Games").child(Sid);
            gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    Games games = snapshot.getValue(Games.class);
                    isDownloaded = games.getIsDownloaded();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void fetchAppDataToDisplay()
    {
       DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("Apps");

        appRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Apps apps = snapshot.getValue(Apps.class);
                softwareName.setText(apps.getSname());
                softwareCategories.setText(apps.getDate());
                Picasso.get().load(apps.getImage()).into(softwareImage);
                softwareDescription.setText(apps.getDescription());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        softwareInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isDownloaded.equals("false"))
                {
                    appRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            Apps apps = snapshot.getValue(Apps.class);

                            String fileUrl = apps.getApkFile().toString();
                            fileName = apps.getSname().toString();
                            DownloadPDF(fileUrl, fileName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (isDownloaded.equals("true")) {
                    softwareInstall.setText("Open");
                    openDownloadedFile(new File(getExternalFilesDir(null), "Download_EthioStore/" + fileName + ".apk"));
                }
            }
        });


    }

    private void openDownloadedFile(File file)
    {
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", file);


        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        openIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(openIntent);
            Log.d("InstallAPK","Appk Installation started");
        } catch (Exception e) {
            // Handle exceptions, for example, if there is no app to handle the file type
            Toast.makeText(this, "No app found to open the file", Toast.LENGTH_SHORT).show();
            Log.d("InstallAPK","Error opening the apk file");
        }
    }

    private String getMimeType(String absolutePath)
    {

        return "application/vnd.android.package-archive";
    }
    private void DownloadPDF(String fileUrl, String fileName)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Downloading the APK file");
        progressDialog.setProgress(0);
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(fileUrl);

        File externalFileDir = getExternalFilesDir(null);

        if (externalFileDir != null) {
            File downloadDir = new File(externalFileDir, "Download_EthioStore");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
            File loaclFile = new File(downloadDir, fileName + ".apk");

            storageReference.getFile(loaclFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                           // showNotification("Download Complete", "File has been downloaded successfully", loaclFile);
                            softwareInstall.setText("Open");
                            setIsDownloadedTrue();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(App_Game_DetailActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                            currentProgress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setProgress(currentProgress);
                            if (currentProgress == 100) {
                                progressDialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void setIsDownloadedTrue()
    {
        if (type.equals("Apps"))
        {
            DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("Apps").child(Sid);


            Map<String, Object> updates = new HashMap<>();
            updates.put("isDownloaded", "true");

// Update the specific node with the new values
            appRef.updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //bookDownload.setText("Open");
                            Toast.makeText(App_Game_DetailActivity.this, "Is downloaded is set to true", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (type.equals("Games")) {
            DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Games").child(Sid);

            Map<String, Object> updates = new HashMap<>();
            updates.put("isDownloaded", "true");

// Update the specific node with the new values
            bookRef.updateChildren(updates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //bookDownload.setText("Open");
                            Toast.makeText(App_Game_DetailActivity.this, "Is downloaded is set to true", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void fetchGamesDataToDisplay()
    {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        gameRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Games games = snapshot.getValue(Games.class);
                softwareName.setText(games.getSname());
                softwareCategories.setText(games.getDate());
                Picasso.get().load(games.getImage()).into(softwareImage);
                softwareDescription.setText(games.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        softwareInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (isDownloaded.equals("false"))
                {
                    gameRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            Games games = snapshot.getValue(Games.class);

                            String fileUrl = games.getApkFile().toString();
                            fileName = games.getSname().toString();
                            DownloadPDF(fileUrl, fileName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if(isDownloaded.equals("true"))
                {
                    openDownloadedFile(new File(getExternalFilesDir(null),"Download_EthioStore/Games/" + fileName + ".apk"));
                }

            }
        });
    }

//    private void showNotification(String title, String message,File directory) {
//        // Create a notification channel (for devices running Android 8.0 or higher)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "DownloadChannel";
//            String description = "Channel for file download notifications";
//
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        Intent openIntent = new Intent(Intent.ACTION_VIEW);
//        Uri contentUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", directory);
//        openIntent.setDataAndType(contentUri, getMimeType(directory.getAbsolutePath()));
//        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                0,
//                openIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Create the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.baseline_notifications_active_24)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                ;
//
//        // Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//
//    }
}