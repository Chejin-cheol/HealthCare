package cs.healthCare.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

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
import cs.healthCare.model.LiveData;
import cs.healthCare.network.Resource;

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
    public static List<LiveData> live = new ArrayList<LiveData>();

    Handler timeHandler , dataHander ;
    TimerTask timerTask;
    Timer timer;

    private RequestQueue queue;

    BluetoothManager manager;
    TextView timeText ;
    TextView Ex , strength , number, sec;
    ImageView health;
    LineChart chart;

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

        // set chart
        chart = findViewById(R.id.line_chart);

    }

    private void setChart() {
        chart.invalidate(); //차트 초기화 작업
        chart.clear();

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (LiveData record : live) { //values에 데이터를 담는 과정
            float dateTime = (float) record.getTime();
            float strength = record.getStength();
            values.add(new Entry(dateTime, strength));
        }

        int size = 0;
        if( timeCount  == 0 || timeCount < 10  )
        {
                size = 10;
        }
        else
        {
            size = (timeCount / 10 ) * 10;
        }


        /*몸무게*/
        LineDataSet lineDataSet = new LineDataSet(values, "강도"); //LineDataSet 선언
        lineDataSet.setColor(ContextCompat.getColor(getApplicationContext(), R.color.purple)); //LineChart에서 Line Color 설정
        lineDataSet.setCircleColor(ContextCompat.getColor(getApplicationContext(), R.color.purple)); // LineChart에서 Line Circle Color 설정
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(getApplicationContext(), R.color.purple)); // LineChart에서 Line Hole Circle Color 설정

        LineData lineData = new LineData(); //LineDataSet을 담는 그릇 여러개의 라인 데이터가 들어갈 수 있습니다.
        lineData.addDataSet(lineDataSet);
        lineData.setValueTextSize(9);


        XAxis xAxis = chart.getXAxis(); // x 축 설정
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x 축 표시에 대한 위치 설정
        xAxis.setLabelCount(size, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌


        YAxis yAxisRight = chart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함

        chart.setVisibleXRangeMinimum(size); //라인차트에서 최대로 보여질 X축의 데이터 설정
        chart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.
        Legend legend = chart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)

        chart.setData(lineData);
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
                Toast.makeText(getApplicationContext(),"xxxx "+data,Toast.LENGTH_SHORT).show();
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

                    LiveData item = new LiveData();
                    item.setStength(Integer.parseInt( datas[1] ) );
                    item.setTime(timeCount);
                    live.add(item);
                    setChart();
                }
            }
        };
    }

    private void startTimeTask()
    {
        timerTask = new TimerTask() {
            @Override
            public void run() {
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
                    Intent intent = new Intent(TrainingActivity.this , ex_feedback.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        sendData(jsonObject.toString());
        super.onDestroy();
        manager.destroy();
        timer.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == BluetoothManager.BLUETOOTH_REQUEST_CODE  && resultCode == Activity.RESULT_OK)
        {
            manager.setPairedDevice();
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



    //implement

   class  AxisiValueFormatter extends IndexAxisValueFormatter {
       @Override
       public String getFormattedValue(float value) {
           return super.getFormattedValue(value);
       }
   }
}