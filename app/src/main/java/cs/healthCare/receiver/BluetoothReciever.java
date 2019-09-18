package cs.healthCare.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cs.healthCare.activity.ExBluetoothTest;

public class BluetoothReciever extends BroadcastReceiver {
    cs.healthCare.bluetooth.BluetoothManager _bluetoothManager;
    public  BluetoothReciever(cs.healthCare.bluetooth.BluetoothManager bluetoothManager)
    {
        _bluetoothManager = bluetoothManager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch(action){
            //블루투스 디바이스 검색 종료
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED: //bluetooth Adapter가 원격장치 검색을 시작했을 때,
                if (_bluetoothManager.bluetoothDevices  != null){
                    _bluetoothManager.bluetoothDevices.clear();
                }
                break;
            //블루투스 디바이스를 찾음
            case BluetoothDevice.ACTION_FOUND:
                //검색한 블루투스 디바이스의 객체를 구한다.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //데이터 저장
                Map map = new HashMap();
                map.put("name", device.getName()); //블루투스 디바이스의 이름
                map.put("adress", device.getAddress()); //블루투스 디바이스의 MAC 주소
                _bluetoothManager.detectedDevices.add(map);
                //리스트 목록 갱신
                //블루투스 디바이스 저장
                _bluetoothManager.bluetoothDevices.add(device);
                break;
            //블루투스 디바이스 검색 종료
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                break;
            //블루투스 디바이스 페어링 상태 변화
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED: //원격장치의 연결 상태가 변경 되었음을 알려줬을 때,
                BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(paired.getBondState()== BluetoothDevice.BOND_BONDED){ //BluethoothDevice.BOND_BONDED : 원격장치가 페어링 되었음 (cf. BOND_BONDING : 페어링 중)
                    //데이터 저장
                    Map map2 = new HashMap();
                    map2.put("name", paired.getName()); //블루투스 디바이스의 이름
                    map2.put("address", paired.getAddress()); //블루투스 디바이스의 MAC주소
                    _bluetoothManager.pairedDevices.add(map2);

                }//if
                break;
        }
    }
}
