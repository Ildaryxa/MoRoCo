package com.ildar.moroco;

import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceSelection extends AppCompatActivity {

    BluetoothAdapter bluetooth;
    ArrayAdapter<String> mArrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_selection);

        listView = (ListView) findViewById(R.id.listView);
        mArrayAdapter = new ArrayAdapter<String>(this, R.layout.my_list_item);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bluetooth.cancelDiscovery();
                String str = (String) ((TextView)view).getText();
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
            }
        });

        bluetooth = BluetoothAdapter.getDefaultAdapter();
        String mydeviceaddress= bluetooth.getAddress();
        String mydevicename= bluetooth.getName();
        String status = mydevicename+" : "+ mydeviceaddress;

        //Toast.makeText(this, status, Toast.LENGTH_LONG).show();
        bluetooth.startDiscovery();
        searchDevice();
    }

    private void searchDevice(){
        final BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action= intent.getAction();
                // Когда найдено новое устройство
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    // Получаем объект BluetoothDevice из интента
                    BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Добавляем имя и адрес в array adapter, чтобы показвать в ListView
                    mArrayAdapter.add(device.getName()+"\n"+ device.getAddress());
                    //Toast.makeText(getApplicationContext(), device.getName(), Toast.LENGTH_LONG).show();
                }
            }
        };
        // Регистрируем BroadcastReceiver
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);// Не забудьте снять регистрацию в onDestroy

    }
}
