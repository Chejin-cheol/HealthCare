package cs.healthCare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import cs.healthCare.R;

public class SplashActivity extends AppCompatActivity {
    private static final String URL = "http://61.84.24.251:3000/source/";
    Queue daownloadQueue = new LinkedList();
    ImageDownload downloadAsync;
    private String id,name;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_activity);

        SharedPreferences sf = getSharedPreferences("files", MODE_PRIVATE);
        boolean isSaved = sf.getBoolean("fileSaved", false);

        if (isSaved) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                sendToken();
        }},3000);

        } else {
            saveFiles();
        }
    }


        private void saveFiles() {
            for (int i = 1000; i <= 1007; i++) {
                daownloadQueue.add(i);
            }
            for (int i = 2000; i <= 2020; i++) {
                daownloadQueue.add(i);
            }

        downloadAsync = new ImageDownload();
        downloadAsync.execute(daownloadQueue);
    }


    private class ImageDownload extends AsyncTask<Queue, Integer, Queue> {        //이미지 다운로드
        private String fileName;
        private String savePath = getFilesDir() + File.separator + "images/";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Queue doInBackground(Queue... params) {

            download(params[0]);
            return params[0];
        }

        @Override
        protected void onPostExecute(Queue obj) {
            SharedPreferences sharedPreferences = getSharedPreferences("files", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("fileSaved", true); // key, value를 이용하여 저장하는 형태
            //최종 커밋
            editor.commit();
            Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
            startActivity(intent);
            finish();

            super.onPostExecute(obj);
        }


        private void download(Queue q) {
            File dir = new File(savePath);
            //상위 디렉토리가 존재하지 않을 경우 생성
            if (!dir.exists()) {
                dir.mkdirs();
            }

            fileName = q.poll().toString();
            String fileUrl = URL + fileName;
            String localPath = savePath + fileName + ".jpg";

            try {
                URL imgUrl = new URL(fileUrl);
                //서버와 접속하는 클라이언트 객체 생성
                HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
                int response = conn.getResponseCode();

                File file = new File(localPath);

                InputStream is = conn.getInputStream();
                OutputStream outStream = new FileOutputStream(file);

                byte[] buf = new byte[1024];
                int len = 0;

                while ((len = is.read(buf)) > 0) {
                    outStream.write(buf, 0, len);
                }
                outStream.close();
                is.close();
                conn.disconnect();

                if (!q.isEmpty()) {
                    download(q);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void sendToken(){


        queue = Volley.newRequestQueue(this);
        String url = "http://61.84.24.251:3000/users/Verify";
        SharedPreferences pref = getSharedPreferences("user",MODE_PRIVATE);
        final String token1 = pref.getString("token","");

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final Intent log1 =new Intent(SplashActivity.this, LoginActivity.class );
                final Intent log2 =new Intent(SplashActivity.this, MainActivity.class );
                if (response.equals("Fail"))
                {
                    Intent intent = new Intent(getApplicationContext() , LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    try {
                        JSONObject json = new JSONObject(response);
                        id=json.getString("id");
                        name=json.getString("nickname");
                        log2.putExtra("id",id);
                        log2.putExtra("nickname",name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(log2);
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
                params.put("token", token1);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
