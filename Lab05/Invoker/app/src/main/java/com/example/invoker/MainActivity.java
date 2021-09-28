package com.example.invoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.putExtra("message", "Hello World");
        i.setComponent(new ComponentName("com.example.creator", "com.example.creator.Wifi"));
        startService(i);


    }
}