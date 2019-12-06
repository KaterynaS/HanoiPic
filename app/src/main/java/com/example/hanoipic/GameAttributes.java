package com.example.hanoipic;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GameAttributes {

    int[] disksImgResourcesList;


    public GameAttributes() {
        disksImgResourcesList = new int[]{
                R.drawable.rec_one, R.drawable.rec_two,
                R.drawable.rec_three, R.drawable.rec_four,
                R.drawable.rec_five, R.drawable.rec_six,
                R.drawable.rec_seven};
    }

    public int getDisksImgResourceId(int number)
    {
        return disksImgResourcesList[number];
    }


    public int[] getDisksImgResourcesList()
    {
        return disksImgResourcesList;
    }


    public void populateSpinner(Context context, Spinner spinnerName)
    {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.number_of_disks_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerName.setAdapter(adapter);
    }

}
