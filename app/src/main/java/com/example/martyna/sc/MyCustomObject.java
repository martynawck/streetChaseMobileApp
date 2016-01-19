package com.example.martyna.sc;

import android.view.View;

/**
 * Created by Martyna on 2016-01-18.
 */
public class MyCustomObject {

    private MyCustomObjectListener listener;

    // Step 1 - This interface defines the type of messages I want to communicate to my owner
    public interface MyCustomObjectListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);
        // or when data has been loaded
      //  public void onDataLoaded(SomeData data);
    }

    public MyCustomObject() {
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    // Assign the listener implementing events interface that will receive the events
    public void setCustomObjectListener(MyCustomObjectListener listener) {
        this.listener = listener;
    }


}