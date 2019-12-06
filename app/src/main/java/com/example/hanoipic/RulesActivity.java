package com.example.hanoipic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

public class RulesActivity extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        spinner = findViewById(R.id.number_of_disks_spinner);

        GameAttributes ga = new GameAttributes();
        ga.populateSpinner(RulesActivity.this, spinner);


    }
}
