package cs.healthCare.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Random;

import cs.healthCare.R;
import cs.healthCare.model.LiveData;

public class ex_feedback extends AppCompatActivity {
    String a1 = "수행 시간, 근력 수치, 수행 횟수 모두 이전 측정에 비해 수치가 낮아졌습니다. 올바른 자세와 체력 증진이 필요해보입니다.";
    String a2 = "이전 측정보다 시행 횟수가 증가하였습니다. 다만 수행 시간이 오래 걸렸으며, 근력 수치 또한 낮게 측정됬습니다. ";
    String a3 = "이전 측정에 비해 수행 시간이 지연되었습니다. 근지속력의 강화가 필요해보입니다.";
    String a4 = "수행 시간, 근력 수치, 수행 횟수 모두 이전 측정에 비해 수치가 낮아졌습니다. 올바른 자세와 체력 증진이 필요해보입니다.";
    String a5 = "근력 수치가 증가하였지만,  수행 시간은 지연되고 측정 횟수도 감소되었습니다. 체력 증진이 필요해보입니다.";
    String a6 = "수행 시간이 지연되었지만, 근력 수치와 측정 횟수가 증가하였습니다. ";
    String a7 = "이전 측정에 비해 근력 수치만 감소하였습니다. 올바른 자세의 필요성이 느껴집니다.";
    String a8 = "이정 측정에 비해 측정 횟수만 감소하였습니다. 체력 부진과 속도가  원인인지 생각할 필요성이 느껴집니다.";
    String a9 = "이전 측정보다 측정 횟수가 증가하였습니다.";
    String a10 = "근력 수치만 증가하였습니다. 근지속력의 강화가 필요해보입니다.";
    String a11 = "Error : 측정 값이 정확하지 않습니다. 다시 측정해주시길 바랍니다.";
    String a12 = "수행 시간이 빨라지고, 측정 횟수가 증가하였습니다. 하지만 근력 수치가 감소했습니다.";
    String a13 = "이전 측정에 비해 수행시간이 빨라졌습니다.";
    String a14 = "Error : 측정 값이 정확하지 않습니다. 다시 측정해주시길 바랍니다.";
    String a15 = "Error : 측정 값이 정확하지 않습니다. 다시 측정해주시길 바랍니다.";
    String a16 = "수행 시간이 빨라지고, 측정 횟수가 증가하였습니다. 하지만 근력 수치가 감소했습니다.";
    Random rnd = new Random();
    TextView feedBack;
    LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_feedback);
        feedBack=findViewById(R.id.textView15);
        chart = findViewById(R.id.result);
        int p = rnd.nextInt(15);
//        switch(p){
//            case 0:
//                feedBack.setText(a1);
//            case 1:
//                feedBack.setText(a2);
//            case 2:
//                feedBack.setText(a3);
//            case 3:
//                feedBack.setText(a4);
//            case 4:
//                feedBack.setText(a5);
//            case 6:
//                feedBack.setText(a6);
//            case 7:
//                feedBack.setText(a7);
//            case 8:
//                feedBack.setText(a8);
//            case 9:
//                feedBack.setText(a9);
//            case 10:
//                feedBack.setText(a10);
//            case 11:
//                feedBack.setText(a11);
//            case 12:
//                feedBack.setText(a12);
//            case 13:
//                feedBack.setText(a13);
//            case 14:
//                feedBack.setText(a14);
//            case 15:
//                feedBack.setText(a15);
//            case 16:
//                feedBack.setText(a16);
//        }

        String result =   getIntent().getExtras().getString("feed_back");
        feedBack.setText(result);
        setChart();
    }


    private void setChart() {
        chart.invalidate(); //차트 초기화 작업
        chart.clear();

        ArrayList<Entry> values = new ArrayList<Entry>();

        for (LiveData record : TrainingActivity.live) { //values에 데이터를 담는 과정
            float dateTime = (float) record.getTime();
            float strength = record.getStength();
            values.add(new Entry(dateTime, strength));
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
        xAxis.setLabelCount(10, true); //X축의 데이터를 최대 몇개 까지 나타낼지에 대한 설정 5개 force가 true 이면 반드시 보여줌


        YAxis yAxisRight = chart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함

        chart.setVisibleXRangeMinimum(60); //라인차트에서 최대로 보여질 X축의 데이터 설정
        chart.setDescription(null); //차트에서 Description 설정 저는 따로 안했습니다.
        Legend legend = chart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)

        chart.setData(lineData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TrainingActivity.live.clear();
    }
}
