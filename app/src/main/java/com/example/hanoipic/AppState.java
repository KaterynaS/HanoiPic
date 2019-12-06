package com.example.hanoipic;

public class AppState {

    private int chosenLevel;

    private AppState() {

    }

    private static AppState instance = null;

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return(instance);
    }


    public void setChosenLevel(int chosenLevel) {
        this.chosenLevel = chosenLevel;
    }

    public int getChosenLevel() {
        return chosenLevel;
    }
}
