package com.example.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View v) {
        // Get username and password
        EditText username = (EditText)findViewById(R.id.editUsername);
        EditText password = (EditText)findViewById(R.id.editPassword);
        AccountManager am = AccountManager.get(this);
        // Check if username exists under AccountManager
        Account[] accounts = am.getAccountsByType("com.example.lab06.useracc");
        for (Account account : accounts) {
            // If user exists, validate credentials
            if (username.getText().toString().equals(account.name)) {
                String accountPass = am.getPassword(account);
                if (password.getText().toString().equals(accountPass)) {
                    Log.d("success", "password success");
                    // Go to file encryption screen if authentication success
                    Intent i = new Intent(v.getContext(), FileEncrypt.class);
                    startActivity(i);
                } else {
                    Log.d("failure", "password failure");
                    Toast.makeText(getApplicationContext(), "Incorrect Password.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
        // If doesn't return error stating user not found
        Log.d("error", "User " + username.getText().toString() + " not found");
        Toast.makeText(getApplicationContext(), "User not found.", Toast.LENGTH_LONG).show();
    }

    public void register(View v) {
        // Goes to the registration activity
        Intent i = new Intent(v.getContext(), AccountReg.class);
        startActivity(i);
    }

    public void getUsers(View V) {
        AccountManager am = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        Account[] accounts = am.getAccountsByType("com.example.lab06.useracc");
    }
}