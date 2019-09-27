package cs.healthCare.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cs.healthCare.R;
import cs.healthCare.bluetooth.BluetoothClient;
import cs.healthCare.bluetooth.BluetoothManager;
import cs.healthCare.receiver.BluetoothSearchReciever;

import static android.content.pm.PackageManager.FEATURE_MANAGED_USERS;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class TrainingActivity extends Activity  implements BluetoothClient  {
    int timeCount = 0;
    int TIME_MAX = 60;

    Handler timeHandler , counterHander ;
    TimerTask timerTask;
    Timer timer;

    BluetoothManager manager;
    TextView counter ;
    SeekBar timeBar;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_activity);
        setView();
        setViewSize();
        setListeners();

        manager = new BluetoothManager(this);

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

    private void setView()
    {
        counter = findViewById(R.id.counter);
        timeBar = findViewById(R.id.timebar);
        timeBar.setMax(TIME_MAX);
    }
    private  void setViewSize()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int timeTextPx = (int) (metrics.heightPixels * 0.3);
        counter.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextPx);
    }
    private void setListeners()
    {
        timeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                timeBar.setProgress(msg.what);
            }
        };

        counterHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                counter.setText(msg.obj.toString());
            }
        };
    }

    private void startTimeTask()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(timeCount == TIME_MAX)
                {
                    finish();
                    return;
                }
                ++timeCount;
                timeHandler.sendEmptyMessage(timeCount);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask , 0 ,1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        manager.findBluetoothDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == BluetoothManager.BLUETOOTH_REQUEST_CODE  && resultCode == Activity.RESULT_OK)
        {
             manager.getPairedDevice();
             manager.findBluetoothDevices();
        }
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


    // bluetooth client
    @Override
    public void receiveData(String data) {
        Message  msg = new Message();
        msg.obj = data;
        Log.i(" 데이터 "," ==> " + data);
        counterHander.sendMessage(msg);
    }

    @Override
    public void sendData() {
    }

    @Override
    public void Binded() {
        startTimeTask();
    }
}
