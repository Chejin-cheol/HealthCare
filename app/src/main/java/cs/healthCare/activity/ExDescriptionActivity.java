package cs.healthCare.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;

import cs.healthCare.R;

public class ExDescriptionActivity extends Activity {
    TextView tex,title;
    static String dd;
    static String[] sa;
    ImageView iv;
    ViewGroup top;
    FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setlayoutSize();


        Intent intent =  getIntent();
        Log.i("인덱스" ,"" + intent.getExtras().getInt("list_id"));


        tex = findViewById(R.id.tex);
        title=findViewById(R.id.textitle);
        iv = findViewById(R.id.iv);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int textpaddding = (int)(metrics.heightPixels *0.04);

        top = findViewById(R.id.top);

        title.setTextSize(TypedValue.COMPLEX_UNIT_PX , metrics.heightPixels *0.04f );
        tex.setTextSize(TypedValue.COMPLEX_UNIT_PX , metrics.heightPixels *0.022f );
        tex.setPadding(textpaddding , tex.getPaddingTop() , textpaddding ,tex.getPaddingBottom());

        sendRequest(intent.getExtras().getInt("list_id"));
    }


    private void setlayoutSize() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 1.0);
        int screenHeight = (int) (metrics.heightPixels * 1.0);
        setContentView(R.layout.ex_description_activity);
        getWindow().setLayout(screenWidth, screenHeight); //set below the setContentview
    }

    public void sendRequest(final int index){

        // RequestQueue를 새로 만들어준다.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // Request를 요청 할 URL
        String url ="http://61.84.24.251:3000/extext?id="+index;
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.


                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            File imgFile = new  File(getFilesDir() + "/images/"  + index + ".jpg");
                            if(imgFile.exists()){
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                iv.setImageBitmap(myBitmap);
                            }


                            dd = jsonArray.getJSONObject(0).getString("ex_text");


                            sa=dd.split("#");
                            Log.i("5555555",sa.length+"");
                            for(int i=0;i<sa.length;i++)
                            {
                                tex.setText(tex.getText() +"\n\n"+sa[i]);
                            }
                            title.setText(jsonArray.getJSONObject(0).getString("ex_name"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }


}