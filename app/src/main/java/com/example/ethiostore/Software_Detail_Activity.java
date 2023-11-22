package com.example.ethiostore;

import static android.os.Environment.DIRECTORY_DOWNLOADS;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethiostore.Model.Books;
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
import java.util.Objects;

import io.paperdb.Book;

public class Software_Detail_Activity extends AppCompatActivity {

    private TextView bookName, bookAuthor, bookDate, bookDescription;
    private ImageView bookImage, bookBack;
    private Button bookDownload;

    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1;
    private String isDownloaded;
    private String fileName;
    int currentProgress;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_detail);

        bookName = (TextView) findViewById(R.id.book_name);
        bookAuthor = (TextView) findViewById(R.id.book_author);
        bookDate = (TextView) findViewById(R.id.book_date);
        bookDescription = (TextView) findViewById(R.id.book_description);

        bookImage = (ImageView) findViewById(R.id.book_image);
        bookBack = (ImageView) findViewById(R.id.book_backBtn);
        bookDownload = (Button) findViewById(R.id.book_download);

        String id = getIntent().getStringExtra("sid").toString();
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Books").child(id);
        bookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Books books = snapshot.getValue(Books.class);
                isDownloaded = books.getIsDownloaded();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        bookBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Software_Detail_Activity.this, Home_Activity.class);
                startActivity(intent);
            }
        });
        bookDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("sid").toString();

                if (isDownloaded.equals("false"))
                {
                    DatabaseReference bookRef;
                    bookRef = FirebaseDatabase.getInstance().getReference().child("Books");

                    bookRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Books books = snapshot.getValue(Books.class);

                            String fileUrl = books.getApkFile().toString();
                             fileName = books.getSname().toString();
                            DownloadPDF(fileUrl, fileName);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else if (isDownloaded.equals("true")) {
                    bookDownload.setText("Open");
                    openDownloadedFile(new File(getExternalFilesDir(null), "Download_EthioStore/" + fileName + ".pdf"));
                }
                }

        });


        DatabaseReference bookRef1;
        bookRef1 = FirebaseDatabase.getInstance().getReference().child("Books");

        bookRef1.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Books books = snapshot.getValue(Books.class);
                bookName.setText(books.getSname());
                bookDate.setText(books.getDate());
                Picasso.get().load(books.getImage()).into(bookImage);
                bookDescription.setText(books.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }


    private void openDownloadedFile(File file)
    {
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", file);
        String mimeType = getMimeType(file.getAbsolutePath());

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        openIntent.setDataAndType(contentUri, mimeType);
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(openIntent);
        } catch (Exception e) {
            // Handle exceptions, for example, if there is no app to handle the file type
            Toast.makeText(this, "No app found to open the file", Toast.LENGTH_SHORT).show();
        }
    }


    private void DownloadPDF(String fileUrl, String fileName) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Downloading the PDF file");
        progressDialog.setProgress(0);
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(fileUrl);

        File externalFileDir = getExternalFilesDir(null);

        if (externalFileDir != null)
        {
            File downloadDir = new File(externalFileDir, "Download_EthioStore");
            if (!downloadDir.exists())
            {
                downloadDir.mkdirs();
            }
            File loaclFile = new File(downloadDir, fileName + ".pdf");

            storageReference.getFile(loaclFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            showNotification("Download Complete", "File has been downloaded successfully", loaclFile);
                           bookDownload.setText("Open");
                           setIsDownloadedTrue();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String message = e.toString();
                            Toast.makeText(Software_Detail_Activity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                            currentProgress = (int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressDialog.setProgress(currentProgress);
                            if (currentProgress == 100)
                            {
                                progressDialog.dismiss();
                            }
                        }
                    });
        }

    }



    private void showNotification(String title, String message,File directory) {
        // Create a notification channel (for devices running Android 8.0 or higher)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "DownloadChannel";
            String description = "Channel for file download notifications";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent openIntent = new Intent(Intent.ACTION_VIEW);
        Uri contentUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", directory);
        openIntent.setDataAndType(contentUri, getMimeType(directory.getAbsolutePath()));
        openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                ;

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

    private String getMimeType(String absolutePath)
    {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(absolutePath);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    private void setIsDownloadedTrue() {

        String id = getIntent().getStringExtra("sid").toString();
        DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Books").child(id);


        Map<String, Object> updates = new HashMap<>();
        updates.put("isDownloaded", "true");

// Update the specific node with the new values
        bookRef.updateChildren(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Software_Detail_Activity.this, "Is downloaded is set to true", Toast.LENGTH_SHORT).show();
                    }
                });

    }

}