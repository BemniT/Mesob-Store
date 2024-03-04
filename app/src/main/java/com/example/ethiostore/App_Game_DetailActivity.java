package com.example.ethiostore;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethiostore.Model.AdditionalImages;
import com.example.ethiostore.Model.Apps;
import com.example.ethiostore.Model.Books;
import com.example.ethiostore.Model.Comment;
import com.example.ethiostore.Model.Games;
import com.example.ethiostore.Prevalent.Continity;
import com.example.ethiostore.View_Holder.Book_VerticalAdapter;
import com.example.ethiostore.View_Holder.CommentAdapter;
import com.example.ethiostore.View_Holder.ContentImageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.ControllableTask;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class App_Game_DetailActivity extends AppCompatActivity {

    private TextView softwareName, softwareCategories, softwareDescription,
            contentLike, softwareSize, softwareNumDownload, softwareDescriptionBtn, softwareReviewBtn;
    private ImageView softwareImage, softwareBack, image_like, softwareSaved;
    private ImageButton comment_sendBtn;
    private Button softwareInstall;
    private EditText comment_area;
    private RecyclerView image_slider, commentsRecyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 1;
    private String isDownloaded;
    private String fileName;
    int currentProgress;
    private ProgressDialog progressDialog;
    private String Sid, type;
    private int likeCount = 0;
    private DatabaseReference userLikesRef, userSaved;
    private DatabaseReference commentsRef, commentData, imageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_game_detail);



        softwareName = (TextView) findViewById(R.id.software_name);
        softwareCategories = (TextView) findViewById(R.id.software_categories);
        softwareSize = (TextView) findViewById(R.id.app_size);
        softwareDescription = (TextView) findViewById(R.id.software_description);
        contentLike = (TextView) findViewById(R.id.content_like);
        // softwareNumDownload = (TextView) findViewById(R.id.software_numDownload);
        //softwareDescriptionBtn = (TextView) findViewById(R.id.software_descriptionBtn);
        // softwareReviewBtn = (TextView) findViewById(R.id.software_reviewBtn);

        softwareInstall = (Button) findViewById(R.id.software_download);
        softwareSaved = (ImageView) findViewById(R.id.image_saved);
        softwareImage = (ImageView) findViewById(R.id.software_image);
        softwareBack = (ImageView) findViewById(R.id.soft_backBtn);
        image_like = (ImageView) findViewById(R.id.image_like);
        comment_sendBtn = (ImageButton) findViewById(R.id.submit_comment_btn);
        comment_area = (EditText) findViewById(R.id.comment_input);

        image_slider = (RecyclerView) findViewById(R.id.software_image_slider);
        commentsRecyclerView = findViewById(R.id.comments_recycler_view);
        createNotificationChannel();


        type = getIntent().getStringExtra("type").toString();
        Sid = getIntent().getStringExtra("sid").toString();
        String userId = Continity.currentOnlineUser.getPhone();

        getInitialLikeCount();

        userSaved = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Continity.currentOnlineUser.getPhone()).child("Saved");
        imageRef = FirebaseDatabase.getInstance().getReference().child("Apps").child(Sid)
                .child("additional_images");
        commentsRef = FirebaseDatabase.getInstance().getReference().child("Comments")
                .child(Sid).child(userId);
        commentData = FirebaseDatabase.getInstance().getReference().child("Comments").child(Sid);
        userLikesRef = FirebaseDatabase.getInstance().getReference().child("UserLikes")
                .child(Continity.currentOnlineUser.getPhone()).child(Sid);
        userLikesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean isLiked = snapshot.getValue(Boolean.class);
                    image_like.setSelected(isLiked);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(App_Game_DetailActivity.this, "Failed to retrieve like status", Toast.LENGTH_SHORT).show();
            }
        });

        // METHOD CALLS ARE HERE
        getDownloadStatus();
        fetchCommentInfo();
        fetchAdditionalImages();
                fetchSavedStatus();


        if (type.equals("Apps")) {
            fetchAppDataToDisplay();
        } else if (type.equals("Games")) {
            fetchGamesDataToDisplay();
        } else {
        }

        softwareSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                boolean isSaved = v.isSelected();
                if (isSaved) {
                    // If the item is being saved, add it to the Saved node
                    userSaved.child(Sid).child("saved").setValue(true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Successfully added to Saved node
                                    DatabaseReference savedRefe = FirebaseDatabase.getInstance().getReference().child(type)
                                                    .child(Sid);
                                    savedRefe.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (type.equals("Apps"))
                                            {
                                                Apps apps = snapshot.getValue(Apps.class);
                                                HashMap<String,Object> appInfo = new HashMap<>();
                                                appInfo.put("app_name",apps.getApp_name());
                                                appInfo.put("categories",apps.getCategories());
                                                appInfo.put("app_id",apps.getApp_id());
                                                appInfo.put("app_icon_url",apps.getApp_icon_url());
                                                appInfo.put("developer_name",apps.getDeveloper_name());
                                                appInfo.put("developer_email",apps.getDeveloper_email());
                                                appInfo.put("developer_phone",apps.getDeveloper_phone());
                                                appInfo.put("apk_file",apps.getApk_file());
                                                appInfo.put("app_size",apps.getApp_size());
                                                appInfo.put("type","Apps");

                                                userSaved.child(Sid).updateChildren(appInfo);

                                            } else if (type.equals("Games")) {
                                                Games games = snapshot.getValue(Games.class);
                                                HashMap<String, Object> gameInfo = new HashMap<>();
                                                gameInfo.put("app_name",games.getApp_name());
                                                gameInfo.put("categories",games.getCategories());
                                                gameInfo.put("app_id",games.getApp_id());
                                                gameInfo.put("app_icon_url",games.getApp_icon_url());
                                                gameInfo.put("developer_name",games.getDeveloper_name());
                                                gameInfo.put("developer_email",games.getDeveloper_email());
                                                gameInfo.put("developer_phone",games.getDeveloper_phone());
                                                gameInfo.put("apk_file",games.getApk_file());
                                                gameInfo.put("app_size",games.getApp_size());
                                                gameInfo.put("type","Games");
                                                userSaved.child(Sid).updateChildren(gameInfo);

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    Toast.makeText(App_Game_DetailActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to add to Saved node
                                    Toast.makeText(App_Game_DetailActivity.this, "Failed to save app/game", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // If the item is being unsaved, remove it from the Saved node
                    userSaved.child(Sid).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Successfully removed from Saved node
                                    Toast.makeText(App_Game_DetailActivity.this, "Unsaved", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to remove from Saved node
                                    Toast.makeText(App_Game_DetailActivity.this, "Failed to remove app/game from saved", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }

        });

        image_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                boolean isLiked = v.isSelected();

                // Update the user's like status in the database
                userLikesRef.setValue(isLiked)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (isLiked) {
                                    likeCount++;
                                } else {
                                    likeCount--;
                                }
                                updateLikeCountInDatabase(likeCount);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
            }
        });

        softwareBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App_Game_DetailActivity.this, Home_Activity.class);
                startActivity(intent);
            }
        });

        comment_sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = comment_area.getText().toString().trim();
                if (commentText.equals("")) {
                    Toast.makeText(App_Game_DetailActivity.this, "Write comment first", Toast.LENGTH_SHORT).show();
                } else {
                    saveCommentToDatabase(commentText);
                }

            }
        });


    }

    private void fetchSavedStatus()
    {
        userSaved.child(Sid).child("saved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    boolean savedStatus = snapshot.getValue(Boolean.class);
                    if (savedStatus == true)
                    {
                        softwareSaved.setSelected(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fetchAdditionalImages() {

        imageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> imageUrls = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String imageUrl = dataSnapshot.getValue(String.class);
                    imageUrls.add(imageUrl);
                }

                ContentImageAdapter contentImageAdapter = new ContentImageAdapter(imageUrls);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App_Game_DetailActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                image_slider.setLayoutManager(linearLayoutManager);
                image_slider.setAdapter(contentImageAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchCommentInfo() {

        commentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Comment> booksVerticalItems = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Comment comment = dataSnapshot.getValue(Comment.class);
                    booksVerticalItems.add(comment);
                }

                CommentAdapter commentAdapter = new CommentAdapter(booksVerticalItems);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App_Game_DetailActivity.this);
                commentsRecyclerView.setLayoutManager(linearLayoutManager);
                commentsRecyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void saveCommentToDatabase(String commentText) {

        String userName = Continity.currentOnlineUser.getName();
        String userImage = Continity.currentOnlineUser.getImage();
        String data_time;
        SimpleDateFormat time = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
        data_time = time.format(new Date());


        // Create a new Comment object

        HashMap<String, Object> commentData = new HashMap<>();
        commentData.put("userName", userName);
        commentData.put("comment", commentText);
        commentData.put("timestamp", data_time);
        commentData.put("userImage",userImage);
        commentsRef.updateChildren(commentData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    comment_area.setText("");
                    fetchCommentInfo();
                }
            }
        });
    }

    private void getInitialLikeCount() {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(Sid);
        likesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    likeCount = snapshot.getValue(Integer.class);
                    contentLike.setText(String.valueOf(likeCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void updateLikeCountInDatabase(int likeCount) {
        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(Sid);
        likesRef.setValue(likeCount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        contentLike.setText(String.valueOf(likeCount));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


    private void getDownloadStatus() {
        if (type.equals("Apps")) {
            DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("Apps").child(Sid);
            appRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Apps apps = snapshot.getValue(Apps.class);
                    //  isDownloaded = apps.getIsDownloaded();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else if (type.equals("Games")) {
            DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("Games").child(Sid);
            gameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Games games = snapshot.getValue(Games.class);
//                    isDownloaded = games.getIsDownloaded();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    private void fetchAppDataToDisplay() {
        DatabaseReference appRef = FirebaseDatabase.getInstance().getReference().child("Apps");

        appRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Apps apps = snapshot.getValue(Apps.class);
                softwareName.setText(apps.getApp_name());
                softwareCategories.setText(apps.getCategories());
                Picasso.get().load(apps.getApp_icon_url()).into(softwareImage);
                softwareDescription.setText(apps.getApp_description());
                softwareSize.setText(apps.getApp_size() + " MB");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        softwareInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isDownloaded.equals("false"))
//                {
                appRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Apps apps = snapshot.getValue(Apps.class);

                        String fileUrl = apps.getApk_file().toString();
                        fileName = apps.getApp_name().toString();
                        DownloadFile(fileUrl, fileName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }



    private String getMimeType(String absolutePath) {

        return "application/vnd.android.package-archive";
    }

    private void DownloadFile(String fileUrl, String fileName) {
        boolean isAppDownloaded = checkDownloadStatus();

        File externalFileDir = getExternalFilesDir(null);

        if (externalFileDir != null) {
            File downloadDir = new File(externalFileDir, "Download_EthioStore");
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }
        if (isAppDownloaded)
        {
            softwareInstall.setText("Open");
            softwareInstall.setBackgroundColor(getResources().getColor(R.color.base));
            softwareInstall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open the app or install it again
                    openOrInstallApp(downloadDir);
                }
            });
        }else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Downloading...");
            progressDialog.setProgress(0);
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReferenceFromUrl(fileUrl);


                File loaclFile = new File(downloadDir, fileName + ".apk");

                storageReference.getFile(loaclFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // showNotification("Download Complete", "File has been downloaded successfully", loaclFile);
                                //softwareInstall.setText("Open");
//                            setIsDownloadedTrue();
                                updateDownloadStatus(true);
                                softwareInstall.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        installApp(loaclFile);
                                    }
                                });

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
                                    showDownloadCompleteNotification();
                                }
                            }
                        });
            }
        }
    }


    private void fetchGamesDataToDisplay() {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference().child("Games");

        gameRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Games games = snapshot.getValue(Games.class);
                softwareName.setText(games.getApp_name());
                softwareCategories.setText(games.getCategories());
                Picasso.get().load(games.getApp_icon_url()).into(softwareImage);
                softwareDescription.setText(games.getApp_description());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        softwareInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    gameRef.child(Sid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Games games = snapshot.getValue(Games.class);

                            String fileUrl = games.getApk_file().toString();
                            fileName = games.getApp_name().toString();
                            DownloadFile(fileUrl, fileName);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


            }
        });

    }

    private void showDownloadCompleteNotification() {
        // Create an explicit intent for the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // Create an intent to open the folder where the downloaded file is located
        Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", getFileStreamPath(fileName));
        openFileIntent.setDataAndType(fileUri, MimeTypeMap.getSingleton().getMimeTypeFromExtension("apk"));
        openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent openFilePendingIntent = PendingIntent.getActivity(this, 0, openFileIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification with actions
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifiy)
                .setContentTitle("Download Complete")
                .setContentText("The file has been downloaded successfully")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.baseline_info_24, "Open Folder", openFilePendingIntent)
                .setAutoCancel(true);

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
    private void installApp(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri = FileProvider.getUriForFile(this, "com.example.ethiostore.fileprovider", file);
        intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for download notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void updateDownloadStatus(boolean downloaded) {
        SharedPreferences sharedPreferences = getSharedPreferences("download_status", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Sid, downloaded);
        editor.apply();
    }

    private boolean checkDownloadStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("download_status", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Sid, false);
    }

    private void openOrInstallApp(File downDir) {
        File file = new File(downDir, fileName+".apk");

        if (file.exists()) {
            installApp(file); // Install the app if the file exists
        } else {
            // The APK file does not exist, handle the scenario (e.g., show a message to the user)
            Toast.makeText(this, "APK file not found. Please download the app again.", Toast.LENGTH_SHORT).show();

            // Optionally, update the download status to false in SharedPreferences or database
            updateDownloadStatus(false);
        }
    }

}