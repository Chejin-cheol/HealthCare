package cs.healthCare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

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
    TextView textView15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_feedback);
        textView15=findViewById(R.id.textView15);
        int p = rnd.nextInt(15);
        switch(p){
            case 0:
                textView15.setText(a1);
            case 1:
                textView15.setText(a2);
            case 2:
                textView15.setText(a3);
            case 3:
                textView15.setText(a4);
            case 4:
                textView15.setText(a5);
            case 6:
                textView15.setText(a6);
            case 7:
                textView15.setText(a7);
            case 8:
                textView15.setText(a8);
            case 9:
                textView15.setText(a9);
            case 10:
                textView15.setText(a10);
            case 11:
                textView15.setText(a11);
            case 12:
                textView15.setText(a12);
            case 13:
                textView15.setText(a13);
            case 14:
                textView15.setText(a14);
            case 15:
                textView15.setText(a15);
            case 16:
                textView15.setText(a16);


        }

    }
}
