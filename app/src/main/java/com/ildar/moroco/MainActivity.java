package com.ildar.moroco;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
    final int REQUEST_ENABLED_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnConnection(View view) {
        BluetoothAdapter bluetooth= BluetoothAdapter.getDefaultAdapter();
        if(bluetooth!=null)
        {
            if (!bluetooth.isEnabled()){
                //значит bluetooth выключен, попросить пользователя включить его
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLED_BLUETOOTH);
            }
        } else {
            //bluetooth модуля не сущесвует
            Toast.makeText(this, "На данном устройстве нет Bluetooth", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            Intent intent = new Intent(this, DeviceSelection.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Для дальнейшей работы необходимо Bluetooth соединение!", Toast.LENGTH_LONG).show();
        }
    }
}
