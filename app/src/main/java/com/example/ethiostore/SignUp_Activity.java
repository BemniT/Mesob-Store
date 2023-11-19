package com.example.ethiostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class SignUp_Activity extends AppCompatActivity {
    private ImageButton createAccountbtn;
    private EditText inputName, inputPhonenumber, inputPassword, confirm_password;
    private TextView loginTxt;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createAccountbtn = (ImageButton) findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_username);
        inputPhonenumber = (EditText) findViewById(R.id.register_phonenumber);
        inputPassword = (EditText) findViewById(R.id.register_password);
        confirm_password = (EditText) findViewById(R.id.register_Confirmpassword);
        loginTxt = (TextView) findViewById(R.id.login_text);

        loadingBar = new ProgressDialog(this);

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SignUp_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        createAccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {

        String name = inputName.getText().toString();
        String phone = inputPhonenumber.getText().toString();
        String password = inputPassword.getText().toString();
        String confirmPassword = confirm_password.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Fill your Name", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please Provide your PhoneNumber", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Set your Password", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please confirm your Password", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Confirmation password doesn't match", Toast.LENGTH_SHORT).show();
        }
        else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, While we are checking your credential");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateUser(name, phone, password);
        }
    }

    private void ValidateUser(String name, String phone, String password)
    {
        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference();

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(!(snapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phone", phone);
                    userdataMap.put("name", name);
                    userdataMap.put("password", password);

                    Rootref.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(SignUp_Activity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(SignUp_Activity.this, Login_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        loadingBar.dismiss();
                                        Toast.makeText(SignUp_Activity.this, "Network Error: try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignUp_Activity.this, "This "+ phone +" already Exists!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    //Toast.makeText(RegisterActivity2.this, "Please try another phone number", Toast.LENGTH_SHORT).show();

                    /// Intent intent = new Intent(RegisterActivity2.this, MainActivity.class);
                    //startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}