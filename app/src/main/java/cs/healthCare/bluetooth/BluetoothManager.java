package cs.healthCare.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
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

import cs.healthCare.service.BluetoothDataService;
import cs.healthCare.service.CharacterService;

public class BluetoothManager {
    public static int BLUETOOTH_REQUEST_CODE = 100;
    public static String DEVICE_NAME = "Moon";

    private Context context;
    private Intent serviceIntent;

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
                Log.i("소캣" ,"ㅋㅋㅋㅋ");
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

        // 이미 페어링된 디바이스 연결
        Set<BluetoothDevice> bounded = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : bounded)
        {
            if(device.getName().equals(DEVICE_NAME))
            {
                // bind
                Log.i(device.getName() ,"페어링");
                this.device = device;
                setService();
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
            setService();
        }
    }

    private void setService()
    {
        serviceIntent = new Intent( context , BluetoothDataService.class);
        context.bindService(serviceIntent , _connection , Context.BIND_AUTO_CREATE);
        context.startService(serviceIntent);
    }

    public void destroy()
    {
        context.unbindService(_connection);
        context.stopService(serviceIntent);
    }

}
