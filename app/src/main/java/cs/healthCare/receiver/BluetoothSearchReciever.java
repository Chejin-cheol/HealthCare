package cs.healthCare.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cs.healthCare.activity.ExBluetoothTest;

public class BluetoothSearchReciever extends BroadcastReceiver {
    cs.healthCare.bluetooth.BluetoothManager _bluetoothManager;
    BluetoothDevice device;
    public BluetoothSearchReciever(cs.healthCare.bluetooth.BluetoothManager bluetoothManager)
    {
        _bluetoothManager = bluetoothManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch(action){

            //블루투스 디바이스 검색 종료
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED: //bluetooth Adapter가 원격장치 검색을 시작했을 때,
                break;

            //블루투스 디바이스를 찾음
            case BluetoothDevice.ACTION_FOUND:
                //검색한 블루투스 디바이스의 객체를 구한다.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getName() != null)
                {
                    if(device.getName().equals(_bluetoothManager.DEVICE_NAME))
                    {
                        Log.i("페어링",device.getName());
                        _bluetoothManager.bind(device);
                    }
                }
                break;

            //블루투스 디바이스 검색 종료
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                break;


            //블루투스 디바이스 페어링 상태 변화
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED: //원격장치의 연결 상태가 변경 되었음을 알려줬을 때,
                Log.i("이름","페어링 변경");
//                _bluetoothManager.setPairedDevice();
                break;

            case BluetoothDevice.ACTION_PAIRING_REQUEST :
                _bluetoothManager.pairing();
                 break;
        }
    }
}
