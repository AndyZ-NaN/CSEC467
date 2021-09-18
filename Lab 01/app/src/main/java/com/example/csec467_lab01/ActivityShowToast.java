package com.example.csec467_lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivityShowToast extends AppCompatActivity {

    private String toastMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_toast);

        // Retrieve data from previous activity
        Intent i = getIntent();
        // Data from prev activity is passed to toastMsg
        toastMsg = i.getStringExtra("InputData");

        Button showToast = (Button)findViewById(R.id.btwShowToast);
        View.OnClickListener O = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        };
        showToast.setOnClickListener(O);
    }
}