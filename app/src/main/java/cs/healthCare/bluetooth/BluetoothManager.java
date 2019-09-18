package cs.healthCare.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BluetoothManager {
    public static int BLUETOOTH_REQUEST_CODE = 100;
    private BluetoothAdapter mBluetoothAdapter;
    public List<Map<String, String>> detectedDevices = new ArrayList<>(); //검색된 디바이스 저장
    public List<Map<String, String>> pairedDevices = new ArrayList<>(); //페어링된 기기 저장
    public List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    public int selectDevice = -1; // 선택된 디바이스

    public boolean setAdapter (BluetoothAdapter adapter)
    {
        mBluetoothAdapter = adapter;
        if(mBluetoothAdapter == null)
        {
            return false;
        }
        return  true;
    }

    public void initDevices()
    {
        if(selectDevice != -1)
        {
                //
        }
    }

    public void findBluetoothDevices()
    {
        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    public void sstPairing()
    {

    }

}
