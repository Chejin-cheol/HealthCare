package cs.healthCare.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import cs.healthCare.R;

public class JoinActivity extends Activity {


    //1일때 중복 아닐때 가능한거
    private boolean validate = false;
    private RequestQueue queue;
    private AlertDialog dialog;
    private String lpw;
    private String us;
    private Boolean Bcheck;
    private int icheck,pcheck=0;
    private  Float bmi;

    public final StringBuffer LockPassword(String password) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));

            StringBuffer hexString = new StringBuffer();
            String spw2;


            for (int i = 0; i < hash.length; i++) {

                String hex = Integer.toHexString(0xff & hash[i]);

                if (hex.length() == 1)
                    hexString.append('0');

                hexString.append(hex);

            }
            System.out.println(hexString.toString());
            return hexString;

        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);
        final Button btCheck = (Button)findViewById(R.id.btn_idCheck);
        Button btNext = (Button) findViewById(R.id.bt_mem_join_next);
        final EditText userId = (EditText)findViewById(R.id.ed_user_id);
        final EditText firstPassWord = (EditText)findViewById(R.id.ed_user_password);
        final EditText secondPassWord = (EditText)findViewById(R.id.ed_user_password_chck);
        final EditText userAge = (EditText)findViewById(R.id.ed_user_age);
        final Spinner userSex = (Spinner)findViewById(R.id.spinnerSex);
        final EditText userHeight = (EditText)findViewById(R.id.ed_user_height);
        final EditText userWeight = (EditText)findViewById(R.id.ed_user_weight);
        final TextView userBMI = (TextView)findViewById(R.id.tv_user_bmi);
        final EditText userNickname = (EditText)findViewById(R.id.ed_user_nickname);
        final Spinner spinner_sex = (Spinner)findViewById(R.id.spinnerSex); //스피너(comboBox)객체 생성
        Button btBmiCalc=(Button)findViewById(R.id.btn_BmiCalc);

        queue = Volley.newRequestQueue(this);
        String url = "http://61.84.24.251:3000/users/Regist";

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (Bcheck)
                {
                    if (response.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),"중복된 아이디 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        icheck=1;
                        Bcheck =false;
                        if(userId.getText().toString().equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"아이디를 입력해주세요",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "사용 가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(response);
                        String token = json.getString("token");
                        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",token); // key, value를 이용하여 저장하는 형태
                        Log.i("토큰",token);
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                if(Bcheck){
                    params.put("user_id", userId.getText().toString());
                    params.put("regist","0");
                }
                else {
                    params.put("regist","1");
                    params.put("user_id", userId.getText().toString());
                    lpw = LockPassword(firstPassWord.getText().toString()).toString();
                    params.put("user_password", lpw);
                    params.put("user_age", userAge.getText().toString());
                    params.put("user_sex", us);
                    params.put("user_height", userHeight.getText().toString());
                    params.put("user_weight", userWeight.getText().toString());
                    params.put("user_bmi", bmi.toString());
                    params.put("user_nickname", userNickname.getText().toString());
                }


                return params;



            }
        };


        spinner_sex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(userSex.getSelectedItem().equals("남성"))
                {
                    us = "1";
                }
                if(userSex.getSelectedItem().equals("여성"))
                {
                    us = "2";
                }

            } //end of onItemSelectedListener
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { } //end of onNothingSelected
        });//end of setOnItemSelectedListner




        final Drawable img_o = getApplicationContext().getResources().getDrawable(R.drawable.o);
        final Drawable img_x = getApplicationContext().getResources().getDrawable(R.drawable.x);

        secondPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(firstPassWord.getText().toString().equals(secondPassWord.getText().toString())){
                    img_o.setBounds(0,0,60,60);
                    secondPassWord.setCompoundDrawables(null,null,img_o,null);

                } else {
                    img_x.setBounds(0,0,60,60);
                    secondPassWord.setCompoundDrawables(null,null,img_x,null);

//                    firstPassWord.getBackground().setColorFilter(, PorterDuff.Mode.SRC_ATOP);

                }
            }



            @Override
            public void afterTextChanged(Editable editable) {

            }

        }); //end of addTextChangedListener



        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userId.getText().toString().equals("")||firstPassWord.getText().toString().equals("")||secondPassWord.getText().toString().equals("")||userAge.getText().toString().equals("")||userBMI.getText().toString().equals("")||userHeight.getText().toString().equals("")||userId.getText().toString().equals("")||userNickname.getText().toString().equals("")||userWeight.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    dialog = builder.setMessage("비어있삼").setNegativeButton("OK",null).create();
                    dialog.show();
                    return;
                }
                else{
                    if (firstPassWord.getText().toString().equals(secondPassWord.getText().toString()))
                    {
                        pcheck=1;
                    }
                    else
                    {
                        firstPassWord.getBackground().setTint(Color.parseColor("#ff0000"));
                        secondPassWord.getBackground().setTint(Color.parseColor("#ff0000"));
                        Toast.makeText(getApplicationContext(),"비밀번호가 같지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                    if (icheck==0)
                    {
                        Toast.makeText(getApplicationContext(),"아이디 중복 확인 해주세요.",Toast.LENGTH_SHORT).show();
                    }
                    else if (pcheck+icheck==2){
                        queue.add(stringRequest);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("nickname",userNickname.getText().toString());
                        intent.putExtra("id",userId.getText().toString());
                        startActivity(intent);
                    }
                }


            }
        });
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bcheck= true;
                Log.i("ㅂㅈㄷㅂㅈㄷㅂㅈㄷ","ㅇㅇㅇㅇㅇ");
                queue.add(stringRequest);


            }
        });


        btBmiCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Float height = Float.parseFloat(userHeight.getText().toString())/100; //회원가입에 입력된 유저의 키 받아서 변수(float)에 삽입
                Float weight = Float.parseFloat(userWeight.getText().toString()); //회원가입에 입력된 유저의 몸무게 받아서 변수(float)에 삽입
                //float로 받는 이유 : bmi를 계산하기 위해서는 소수점까지 계산이 필요하다고 한다.

                bmi = weight/(height*height);
                String result_bmi = String.format("%.2f", bmi);

                String result_status = "";


                //bmi가 나이마다 다르긴 한데 일단 성인 기준으로 계산
                if(bmi<18.5){
                    result_status = "저체중";
                } else if (bmi>=18.5 && bmi<23) {
                    result_status = "정상";
                } else if (bmi>=23 && bmi<25) {
                    result_status = "과체중";
                } else if (bmi>=25 && bmi<30){
                    result_status = "비만";
                } else if (bmi>=30) {
                    result_status = "고도비만";
                }
                userBMI.setText(result_bmi + " (" + result_status + ")");
                //userBMI.setText(userHeight.getText());

            }
        });//end of btBmiCalc


    }//end of void onCreate
}//end of class ModeMemJoinActivity