package com.example.ckobieyisi.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



/**
 * Created by C.k Obieyisi on 3/3/2018.
 */

public class HomeActivity extends Activity {

    private EditText canivInput;
    private EditText herbivInput;
    private EditText frameInput;

    private Button simButton;
    private Button helpButton;
    private Button aboutButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        //Define variables
        canivInput = (EditText) findViewById(R.id.CanivoreText);
        herbivInput = (EditText) findViewById(R.id.HerbivoreText);
        frameInput = (EditText) findViewById(R.id.frameText);
        // Create an instance of environment to be able to work with on click

        simButton = (Button) findViewById(R.id.simulateButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);


        //implementation
        simButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the input fields are empty before proceeding
                if (!(herbivInput.getText().toString().equals("")) && !(canivInput.getText().toString().equals("")) && !(frameInput.getText().toString().equals(""))){

                    Environment.setNumHerbivores(Integer.parseInt(herbivInput.getText().toString()));
                    Environment.setNumCarnivores(Integer.parseInt(canivInput.getText().toString()));
                    MainActivity.setFrames(Integer.parseInt(frameInput.getText().toString()));

                    openSimPage();
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openhelpPage();
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openaboutPage();
            }
        });

    }

    public void openSimPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openhelpPage(){
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void openaboutPage(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }
}
