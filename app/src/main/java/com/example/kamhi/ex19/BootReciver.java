package com.example.kamhi.ex19;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Kamhi on 8/11/2017.
 */

public class BootReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Hello Shahar", Toast.LENGTH_LONG).show();
    }
}
