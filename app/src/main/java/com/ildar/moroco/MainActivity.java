package com.ildar.moroco;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity {

    static final String TAG = "iRobot";
    final int REQUEST_ENABLED_BLUETOOTH = 1;

    BluetoothAdapter bluetooth;
    BluetoothDevice device;
    BluetoothSocket btSocket = null;

    static OutputStream outStream = null;
    static InputStream inputStream = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    boolean checkConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean connected = false;
        for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
                try {
                    Method m = device.getClass().getMethod("createRfcommSocket",new Class[] { int.class });
                    try {
                        BluetoothSocket bs = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
                        bs.connect();
                        try {
                            outStream = bs.getOutputStream(); //прием команд в iRobot
                            inputStream = bs.getInputStream(); //отправка команд в iRobot
                        } catch (IOException e) {
                            Log.e(TAG, "Output stream creation failed.", e);
                        }
                        connected = true;
                        Log.d(TAG, device.getName() + " - connected");
                        break;
                    } catch (IOException e) {
                        Log.e(TAG, "IOException: "+e.getLocalizedMessage());
                        Log.d(TAG, device.getName() + " - not connected");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
        }
        return connected;
    }

    public void onCreateActivity(final View view){
        progressDialog = ProgressDialog.show(this, "Поиск устроиств", "Подождите...");
        SearchDevice searchDevice = new SearchDevice();
        if (searchDevice.isConnected()){
            Toast.makeText(this, "Устройство подключено!", Toast.LENGTH_LONG).show();
            inputStream = searchDevice.getInputStream();
            outStream = searchDevice.getOutStream();
            Intent intent = new Intent(this, Control.class);
            progressDialog.dismiss();
            startActivity(intent);
        }else{
            progressDialog.dismiss();
            new AlertDialog.Builder(this).setTitle("Подключение не обнаружено!")
                    .setMessage(searchDevice.getMessage())
                    .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("Проверить заново", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onCreateActivity(view);
                        }
                    })
                    .create()
                    .show();
        }

        /*
        Intent intent = new Intent(this, SetMap.class);
        startActivity(intent);
        */
    }

    public static OutputStream getOutStream() {
        return outStream;
    }

    public static InputStream getInputStream() {
        return inputStream;
    }

}
