package com.example.ethiostore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.ethiostore.Model.UserHelper;
import com.example.ethiostore.Prevalent.Continity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_profileActivity extends AppCompatActivity {
    private CircleImageView profileImageView;
    private EditText fullNameEditTxt , phoneNumberEditTxt, userAddressEditTxt;
    private TextView profileChangeBtn;
    private ImageView closeTextbtn;
    private UserHelper userHelper;

    private Uri imageUri;
    private String  myUri;
    private StorageReference profilePictureRef;
    private String checker = "";
    private Uri uriContent;
    private StorageTask uploadTask;
    private Context context;

    private Button securityButton,  saveTextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImageView = (CircleImageView) findViewById(R.id.setting_profile_image);

        fullNameEditTxt = (EditText) findViewById(R.id.profile_setting_name);
        phoneNumberEditTxt = (EditText) findViewById(R.id.profile_setting_phone);
        userAddressEditTxt = (EditText) findViewById(R.id.profile_setting_address);

        profileChangeBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextbtn = (ImageView) findViewById(R.id.close_setting);
        saveTextBtn = (Button) findViewById(R.id.update_settings);
//        securityButton = (Button) findViewById(R.id.security_question);

        userHelper = new UserHelper(this);
        context = this;

        userInfoDisplay(profileImageView, fullNameEditTxt, phoneNumberEditTxt, userAddressEditTxt);

        saveTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checker.equals("clicked"))
                {
                    userInfoSaved();
                }
                else
                {
                    updateOnlyUserInfo();
                }
            }
        });

        closeTextbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

//        securityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, ResetPasswordActivity.class);
//                intent.putExtra("check", "setting");
//                startActivity(intent);
//            }
//        });
        profileChangeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checker = "clicked";

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoLibraryLauncher.launch(intent);
                //checkers();
            }
        });
    }



    private void updateOnlyUserInfo()
    {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("name",fullNameEditTxt.getText().toString());
        userData.put("address",userAddressEditTxt.getText().toString());
        userData.put("email",phoneNumberEditTxt.getText().toString());
        ref.child(Continity.currentOnlineUser.getPhone()).updateChildren(userData);

        Intent intent = new Intent(Edit_profileActivity.this, MainActivity.class);
        //startActivity(intent);

        Toast.makeText(Edit_profileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();

    }



    private void checkers(){
        String profilePicture = userHelper.getProfilePicture().toString();

        if(!profilePicture.equals("")){
            //profileImageView.setImageURI(Uri.parse(profilePicture));

        }

    }



    private final ActivityResultLauncher<Intent> photoLibraryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        // photoUri is the URI that contain the image uri
                        // result.getdata()  this method enable us to get the uri
                        Uri photoUri = Objects.requireNonNull(result.getData()).getData();

                        CropImageOptions cropImageOptions = new CropImageOptions();
                        cropImageOptions.cropShape = CropImageView.CropShape.OVAL;
                        cropImageOptions.fixAspectRatio = true;

                        //optionals
                        cropImageOptions.minCropWindowHeight = 120;
                        cropImageOptions.minCropWindowWidth = 120;

                        cropImageLauncher.launch(new CropImageContractOptions(photoUri, cropImageOptions));
                    }
                }
            });
    public final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher =
            registerForActivityResult(new CropImageContract(), result ->
            {

                if (result.isSuccessful())
                {
                    // Use the returned uri.
                    uriContent = result.getUriContent();

                    // this were the image is displayed
                    profileImageView.setImageURI(uriContent);

                    assert uriContent != null;

                    userHelper.setProfilePicture(uriContent.toString());

                }
                else
                {
                    // An error occurred.
                    Exception exception = result.getError();
                    String message  = exception.toString();
                    Toast.makeText(context, "Error:" + message, Toast.LENGTH_SHORT).show();
                }


            });



    private void userInfoSaved()
    {

        String Fullname = fullNameEditTxt.getText().toString();
        String phone = phoneNumberEditTxt.getText().toString();
        String Address = userAddressEditTxt.getText().toString();

        if (TextUtils.isEmpty(Fullname))
        {
            Toast.makeText(this, "Full Name Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "phone Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Address))
        {
            Toast.makeText(this, "Address Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if (checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage()
    {
        final ProgressDialog loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Updating");
        loadingBar.setMessage("Your profile is being updated");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        String ImageUri = userHelper.getProfilePicture().toString();

        if (ImageUri != null)
        {
            final StorageReference fileRef = profilePictureRef
                    .child(Continity.currentOnlineUser.getPhone() + ".jpg");

            uploadTask = fileRef.putFile(uriContent);

            uploadTask.continueWithTask(new Continuation()
            {
                @Override
                public Object then(@NonNull Task task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userData = new HashMap<>();
                        userData.put("name",fullNameEditTxt.getText().toString());
                        userData.put("address",userAddressEditTxt.getText().toString());
                        userData.put("email",phoneNumberEditTxt.getText().toString());
                        userData.put("image",myUri);
                        ref.child(Continity.currentOnlineUser.getPhone()).updateChildren(userData);

                        loadingBar.dismiss();

                        Intent intent = new Intent(Edit_profileActivity.this, Home_Activity.class);
                        startActivity(intent);

                        Toast.makeText(Edit_profileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(Edit_profileActivity.this, "Error Occur", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        else
        {
            loadingBar.dismiss();
            Toast.makeText(this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }
    }


    private void userInfoDisplay(CircleImageView profileImageView, EditText fullNameEditTxt, EditText phoneNumberEditTxt, EditText userAddressEditTxt)
    {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Continity.currentOnlineUser.getPhone());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.exists())
                {
                    if (snapshot.child("image").exists())
                    {
                        String image = snapshot.child("image").getValue().toString();
                        String name = snapshot.child("name").getValue().toString();
                        String phone = snapshot.child("email").getValue().toString();
                        String address = snapshot.child("address").getValue().toString();

                        if (image.isEmpty())
                        {
                            Picasso.get().load(R.drawable.profile).into(profileImageView);
                        }else {
                            Picasso.get().load(image).into(profileImageView);
                        }

                        fullNameEditTxt.setText(name);
                        phoneNumberEditTxt.setText(phone);
                        userAddressEditTxt.setText(address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}