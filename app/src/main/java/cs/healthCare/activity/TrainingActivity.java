package cs.healthCare.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import cs.healthCare.R;
import cs.healthCare.bluetooth.BluetoothClient;
import cs.healthCare.bluetooth.BluetoothManager;
import cs.healthCare.receiver.BluetoothSearchReciever;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class TrainingActivity extends Activity  implements BluetoothClient {
    BluetoothManager manager;
    BluetoothSearchReciever searchReceiver ;

    TextView timer ;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_activity);

        manager = new BluetoothManager(this);
        setViewSize();
        setReceivers();

        //권한
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if(PERMISSION_DENIED == permissionCheck)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION },
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }

    private  void setViewSize()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int timeTextPx = (int) (metrics.heightPixels * 0.3);
        timer = findViewById(R.id.timer);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextPx);
    }
    private void setReceivers()
    {
        searchReceiver = new BluetoothSearchReciever(manager);
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); ////BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); //BluetoothDevice.ACTION_BOND_STATE_CHANGED : 원격장치의 연결 상태가 변경 되었음을 알려준다.
        searchFilter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(searchReceiver, searchFilter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        manager.findBluetoothDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchReceiver);
        manager.destroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }


    @Override
    public void receiveData(int data) {

    }

    @Override
    public void sendData() {

    }
}
