package cs.healthCare.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    public static int BLUETOOTH_REQUEST_CODE = 100;
    private Context context;

    private BluetoothAdapter mBluetoothAdapter ;
    private BluetoothDevice device;
    private BluetoothSocket socket;

    OutputStream outputStream;
    InputStream inputStream;

    public BluetoothManager(Context context)
    {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //블루투스 기능 ON 시킴
            ((Activity)context).startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }

        // 페어링 디바이스 연결
        Set<BluetoothDevice> bounded = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : bounded)
        {
            if(device.getName().equals("Moon"))
            {
                this.device = device;
                setSocket();
            }
        }
    }

    public void findBluetoothDevices()
    {
        if(mBluetoothAdapter.isDiscovering())
        {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void bind(BluetoothDevice device)
    {
        this.device = device;
        this.device.createBond();

    }

    public void pairing()
    {
        if(device != null) {
            try {

                byte[] pin = "1234".getBytes();
                device.setPin(pin);
                device.setPairingConfirmation(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setSocket();
    }

    private void setSocket()
    {
        try
        {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            inputStream =  socket.getInputStream();
            outputStream = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void disConnect()
    {
        try
        {
            inputStream.close();
            outputStream.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
