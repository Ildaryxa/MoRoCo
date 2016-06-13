package com.ildar.moroco;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ildar on 12.06.2016.
 */
public class SearchDevice {
    static final String TAG = "iRobot";

    private OutputStream outStream = null;
    private InputStream inputStream = null;
    private boolean connected = false;
    private String message;

    SearchDevice(){
        start();
    }

    public OutputStream getOutStream() {
        return outStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getMessage() {
        return message;
    }

    public boolean isConnected() {
        return connected;
    }


    private void start() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter!=null) {
            if (mBluetoothAdapter.isEnabled()){
                for (BluetoothDevice device : mBluetoothAdapter.getBondedDevices()) {
                                try {
                                    Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
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
                                        Log.e(TAG, "IOException: " + e.getLocalizedMessage());
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
            }else{
                message = "Включите Bluetooth, а затем подключитесь к устройству.";
            }
        }else{
            message = "На данном устройстве нет Bluetooth. Приложение не может быть запущено!";
        }
        if (!connected)
            message = "Подключитесь по Bluetooth к iRobot Create, а затем откройте приложение.";
    }
}
