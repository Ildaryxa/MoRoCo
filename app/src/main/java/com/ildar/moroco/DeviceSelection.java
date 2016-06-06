package com.ildar.moroco;

import android.bluetooth.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DeviceSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_selection);

        BluetoothAdapter bluetooth= BluetoothAdapter.getDefaultAdapter();
        String mydeviceaddress= bluetooth.getAddress();
        String mydevicename= bluetooth.getName();
        String status = mydevicename+" : "+ mydeviceaddress;

        Toast.makeText(this, status, Toast.LENGTH_LONG).show();
    }


}
