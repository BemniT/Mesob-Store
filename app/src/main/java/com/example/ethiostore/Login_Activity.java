package com.example.ethiostore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ethiostore.Model.Account;
import com.example.ethiostore.Prevalent.Continity;
import com.example.ethiostore.Prevalent.LanguageManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;

public class Login_Activity extends AppCompatActivity {

    private ImageButton  log_signup;
    private Button loginButton;
    private EditText loginPhoneNumber, login_Password;
    private ProgressDialog loadingBar;
    private CheckBox rememberMe;
    private TextView AdminLink,notAdminLink, log_signUp;
    private boolean isFormatting;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManager.loadLocale(this);
        setContentView(R.layout.activity_login);

        loginPhoneNumber = (EditText) findViewById(R.id.login_phonenumber);
        login_Password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.login_btn);
        log_signup = (ImageButton) findViewById(R.id.log_signUp);
        loadingBar = new ProgressDialog(this);
        log_signUp = (TextView) findViewById(R.id.login_signup);

//        rememberMe = (CheckBox) findViewById(R.id.rememberMe_check);

        loginPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateMobile(loginPhoneNumber.getText().toString()))
                {
                    loginButton.setEnabled(true);
                }else {
                    loginPhoneNumber.setError("Invalid mobile No");
                    loginButton.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        log_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login_Activity.this, SignUp_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String password = login_Password.getText().toString();
                String phoneNumber = loginPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(Login_Activity.this, "Password Missing!", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(phoneNumber))
                {
                    Toast.makeText(Login_Activity.this, "Phone Number Missing!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Logining");
                    loadingBar.setMessage("Logining in");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    Autenticate(phoneNumber, password);

                }
            }
        });


    }
    boolean validateMobile(String input)
    {
        Pattern p = Pattern.compile("[0][9][0-9]{8}");
        Matcher matcher = p.matcher(input);
        return matcher.matches();

    }


    //WHEN THE PERSON LOGIN
    private void Autenticate(final String phoneNumber, String password)
    {
//        if (rememberMe.isChecked())
//        {
            Paper.init(this);
            Paper.book().write(Continity.userPhonekey, phoneNumber);
            Paper.book().write(Continity.userPassword, password);
//        }


        final DatabaseReference Rootref;
        Rootref = FirebaseDatabase.getInstance().getReference().child("Users");

        Rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(phoneNumber).exists())
                {
                    Account userData = snapshot.child(phoneNumber).getValue(Account.class);

                    if (userData.getPhone().equals(phoneNumber))
                    {
                        if (userData.getPassword().equals(password))
                        {
                                Toast.makeText(Login_Activity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
                                Continity.currentOnlineUser = userData;
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                        }
                        else
                        {
                            Toast.makeText(Login_Activity.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Login_Activity.this, "This Phone Number is not Registered!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(Login_Activity.this, "Create Account First", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
    }
