package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class register extends AppCompatActivity {
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    Button mButtonRegister;
    TextView mTextViewlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextPassword= (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword= (EditText)findViewById(R.id.edittext_cnf_password);
        mButtonRegister = (Button) findViewById(R.id.button_login);
        mTextViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(register.this,login.class);
                startActivity(LoginIntent);
            }
        });
    }
}
