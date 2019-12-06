package com.example.hanoipic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private Button rulesButton;
    private Button playButton;
    private Spinner numberOfDisksSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        GameAttributes ga = new GameAttributes();
        ga.populateSpinner(MainActivity.this, numberOfDisksSpinner);

        implementActions();
    }


    private void implementActions() {
        rulesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rulesPage = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(rulesPage);
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppState appState = AppState.getInstance();
                int lvl = Integer.valueOf(numberOfDisksSpinner.getSelectedItem().toString());
                appState.setChosenLevel(lvl);

                Log.d("on playButton click", "chosen lvl: " + appState.getChosenLevel());

                Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                startActivity(gameActivity);
            }
        });
    }

    private void findViews() {
        rulesButton = findViewById(R.id.read_rules_button);
        playButton = findViewById(R.id.play_button);
        numberOfDisksSpinner = findViewById(R.id.number_of_disks_spinner);
    }

}
