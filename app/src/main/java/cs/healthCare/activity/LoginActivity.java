package cs.healthCare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import cs.healthCare.R;


public class LoginActivity extends AppCompatActivity {

    private EditText ed_id;
    private EditText ed_pw;
    private TextView tv;
    private Button bt_login;//로그인
    private Button bt_reg;//회원가입
    private RequestQueue queue;
    private String spw;
    private AlertDialog dialog;


    public StringBuffer LockPassword(String password) {

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
            // 출력
            Log.i("22222222222"+hexString.toString(),"zzzzzzzzzzzzzzzzzzzzzzzzz");
            System.out.println(hexString.toString());
            return hexString;

        } catch (Exception ex) {
            throw new RuntimeException(ex);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        final Intent it = new Intent(getApplicationContext(), MainActivity.class);

        ed_id=(EditText)findViewById(R.id.et_id);
        ed_pw =(EditText)findViewById(R.id.et_pw);
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_reg=(Button)findViewById(R.id.bt_reg);

        queue = Volley.newRequestQueue(this);
        String url = "http://61.84.24.251:3000/users/Login";

        //------------------------------토큰 값 저장하기위해----------------------------
        SharedPreferences sf = getSharedPreferences("sFile",MODE_PRIVATE);
        String text = sf.getString("logintoken","");


        //------------------------------------------------------------------------------
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    if(response.equals("x"))
                    {
                    }
                    else
                    {
                        try {
                            JSONObject jobject = new JSONObject(response);
                            it.putExtra("id",jobject.getString("user_id"));
                            it.putExtra("nickname",jobject.getString("user_nickname"));
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("token", jobject.getString("token"));
                            editor.commit();
                            startActivity(it);

                        }
                        catch(Exception e)
                        {

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
                params.put("test", ed_id.getText().toString());
                spw =LockPassword(ed_pw.getText().toString()).toString();
                params.put("testpw", spw);
                return params;
            }
        };
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login_id = ed_id.getText().toString();
                String login_pw = ed_pw.getText().toString();
                if(login_id.equals("")||login_pw.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    dialog = builder.setMessage("비어있삼").setNegativeButton("OK",null).create();
                    dialog.show();
                    return;
                }
                else{

                    queue.add(stringRequest);
                }



            }
        });
        bt_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent join =new Intent(LoginActivity.this,JoinActivity.class);
                startActivity(join);
            }
        });
        }

            protected void onStop() {
                super.onStop();

            }
    }

