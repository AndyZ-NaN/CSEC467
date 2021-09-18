package com.example.csec467_lab01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Draws the interface
        setContentView(R.layout.activity_main);
        // maps the button xml
        Button btnA = (Button) findViewById(R.id.btnGoToAct2);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick (View v){
                EditText text = (EditText)findViewById(R.id.editTextAct2);
                String msg = text.getText().toString();
                // Intent passes appl context from view to activity
                Intent i = new Intent(v.getContext(), ActivityShowToast.class);
                // Pass data using a bundle
                i.putExtra("InputData", msg);
                startActivity(i);
            }
        };
        btnA.setOnClickListener(listener);
    }
}

// The app should launch an interface with 2 controls: EditText box that you can enter text into
// And a button which launches "Launch Second Activity"

// When the button is pressed, the app launches a new activity
// New Activity: A button that when pressed, displays text from the EditText using a Toast pop-up