package com.example.kamhi.ex19;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;



public class MainActivity extends Activity {

    private SmsResiver smsResiver;
    private final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;
    private final int MY_PERMISSIONS_REQUEST_BOOT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
     //   IntentFilter event = new IntentFilter(Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION);
      //  this.smsResiver = new SmsResiver();
       // registerReceiver(this.smsResiver, event);
        CheckPermissoins();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (smsResiver != null)
          unregisterReceiver(this.smsResiver);
    }

    private void CheckPermissoins(){
        if((checkSelfPermission(Manifest.permission.READ_SMS)) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, MY_PERMISSIONS_REQUEST_READ_SMS);
        }
        else {
            registerSMSReciver();
        }
        if ((checkSelfPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED)) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, MY_PERMISSIONS_REQUEST_BOOT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_READ_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    registerSMSReciver();
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void registerSMSReciver(){

        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        smsResiver = new SmsResiver();
        registerReceiver(smsResiver, filter);
    }

    public static class SmsResiver extends BroadcastReceiver{

        public SmsResiver(){}

        @Override
        public void onReceive(Context context, Intent intent) {
            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    final SmsResiver[] messages = new SmsResiver[pdusObj.length];
                    for (int i = 0; i < pdusObj.length; i++) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();
                        Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
                        // Show alert
                        Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + message, Toast.LENGTH_LONG).show();
                    } // end for loop
                } // bundle is null
            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);
            }
        }
    }



}
