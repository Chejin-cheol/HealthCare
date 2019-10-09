package cs.healthCare.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cs.healthCare.R;
import cs.healthCare.bluetooth.BluetoothClient;
import cs.healthCare.bluetooth.BluetoothManager;
import cs.healthCare.network.Resource;
import cs.healthCare.receiver.BluetoothSearchReciever;

import static android.content.pm.PackageManager.FEATURE_MANAGED_USERS;
import static android.content.pm.PackageManager.PERMISSION_DENIED;

public class TrainingActivity extends Activity  implements BluetoothClient  {
    int timeCount = 0;
    int TIME_MAX = 60;
    private long _time = 0;
    private int _count = 0;
    private Calendar calendar = Calendar.getInstance();
    private DateFormat dateFormat;

    private JSONObject jsonObject;
    private String data = "";
    private static List<String> dataSet  = new ArrayList<String>();

    Handler timeHandler , dataHander ;
    TimerTask timerTask;
    Timer timer;

    private RequestQueue queue;

    BluetoothManager manager;
    TextView timeText ;
    TextView Ex , strength , number, sec;
    ImageView health;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100 ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.training_activity);
        setView();
        setViewSize();
        setListeners();

        manager = new BluetoothManager(this);
        jsonObject = new JSONObject();

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("ss");
        _count = 0;

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
        health = findViewById(R.id.health);
        File imgFile = new  File(getFilesDir() + "/images/"  + 2007 + ".jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            health.setImageBitmap(myBitmap);
        }


        timeText = findViewById(R.id.time);
        Ex = findViewById(R.id.Ex);
        strength = findViewById(R.id.Strength);
        number = findViewById(R.id.number);
        sec = findViewById(R.id.sec);

    }
    private  void setViewSize()
    {

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int timeHeight =( metrics.heightPixels / 15 ) * 4;
        int timeTextPx = (int) (timeHeight* 0.7);
        int bt1=(int)(timeHeight*0.3);
        int bt2=(int)(timeHeight*0.3);
        timeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeTextPx);
    }
    private void setListeners()
    {
        timeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                timeText.setText(msg.what+"");
            }
        };

        dataHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(dataSet.size() == 0)
                {
                    _time = System.currentTimeMillis();
                }

                data = msg.obj.toString();
                String[] datas =  data.split("-");
                number.setText(datas[2]);
                strength.setText(datas[1]);
                datas[2] = datas[2].replace("\r","").replace(" ","");

                if(_count != Integer.parseInt(datas[2]) )
                {
                    long current = System.currentTimeMillis();
                    double secTime =( current - _time )/1000.0;
                    _time = current;
                    datas[2] = datas[2].replace("\r","");
                    _count = Integer.parseInt(datas[2] );
                    sec.setText( secTime +"" );
                    dataSet.add(data);
                }
            }
        };
    }

    private void startTimeTask()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.i("디디" , data +"<==");
                if(timeCount == TIME_MAX) {
                    try
                    {
                        JSONArray ja = new JSONArray();
                        for (int i = 0; i < dataSet.size(); i++) {
                            JSONObject jo = new JSONObject();
                            jo.put("value", dataSet.get(i));
                            ja.put(jo);
                        }
                        jsonObject.put("id",MainActivity.mid);
                        jsonObject.put("data", ja);
                        jsonObject.put("type",1);
                        Log.i("종료","종료 " +data.toString());
                        dataSet.clear();
                        cancel();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    return;
                }
//                if(timeCount % 2 == 0)
//                {
//                    dataSet.add(data);
//
//                }
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
        sendData(jsonObject.toString());
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
        dataHander.sendMessage(msg);
    }

    @Override
    public void sendData() {
    }

    @Override
    public void Binded() {
        startTimeTask();
    }





    private void sendData(String query){
        final String jsonString = query;
        queue = Volley.newRequestQueue(this);
        String url = Resource.getUrl("users/Pushpull");

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Fail"))
                {

                }
                else
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", jsonString);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}