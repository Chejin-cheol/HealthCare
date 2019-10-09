package cs.healthCare.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
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

import cs.healthCare.receiver.BluetoothSearchReciever;
import cs.healthCare.service.BluetoothDataService;
import cs.healthCare.service.CharacterService;

public class BluetoothManager {
    public static int BLUETOOTH_REQUEST_CODE = 100;
    public static String DEVICE_NAME = "Moon";

    private Context context;
    private Intent serviceIntent;

    private BluetoothSearchReciever searchReceiver ;
    private BluetoothAdapter mBluetoothAdapter ;
    private BluetoothDevice device;
    private BluetoothSocket socket;

    BluetoothDataService _service;

    ServiceConnection _connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            BluetoothDataService.BluetoothBinder binder =  (BluetoothDataService.BluetoothBinder) service;
            _service = binder.getService();
            if(device != null)
            {
                _service.setClient((BluetoothClient) context);
                _service.setSocket(device);
            }
        }
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public BluetoothManager(Context context)
    {
        this.context = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(!mBluetoothAdapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //블루투스 기능 ON 시킴
        ((Activity)context).startActivityForResult(intent, BLUETOOTH_REQUEST_CODE);
        }
        else
        {
            getPairedDevice();
        }
    }

    private void setReceivers()
    {
        searchReceiver = new BluetoothSearchReciever(this);
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); ////BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //BluetoothDevice.ACTION_BOND_STATE_CHANGED : 원격장치의 연결 상태가 변경 되었음을 알려준다.
        searchFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        context.registerReceiver(searchReceiver, searchFilter);
    }

    public void getPairedDevice()
    {
        // 이미 페어링된 디바이스 연결
        Set<BluetoothDevice> bounded = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : bounded)
        {
            if(device.getName().equals(DEVICE_NAME))
            {
                // bind
                this.device = device;
                setService();
            }
        }
        if(device == null)
        {
            setReceivers();
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
            setService();
        }
    }

    private void setService()
    {
        serviceIntent = new Intent( context , BluetoothDataService.class);
        context.bindService(serviceIntent , _connection , Context.BIND_AUTO_CREATE);
        context.startService(serviceIntent);
    }

    public void sendData(String data)
    {
            _service.sendData(data.getBytes());
    }

    public void destroy()
    {
        _service.sendData("x".getBytes());
        if(searchReceiver != null)
        {
            context.unregisterReceiver(searchReceiver);
        }
        context.unbindService(_connection);
        context.stopService(serviceIntent);
    }

}
