package cs.healthCare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import cs.healthCare.R;
import cs.healthCare.adapter.HealthListRecyclerAdapter;
import cs.healthCare.model.HealthListData;
import cs.healthCare.network.Resource;

public class GrowUpExerciseActivity extends Activity {
    private RecyclerView recyclerView;
    private HealthListRecyclerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.growup_activity);

        recyclerView = findViewById(R.id.grou_up_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HealthListRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setItemClick(new HealthListRecyclerAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(view.getContext() , ExDescriptionActivity.class);
                intent.putExtra("list_id", (int)view.getTag());
                startActivity(intent);
            }
        });
        sendRequest();
    }


    public void sendRequest(){
        // RequestQueue를 새로 만들어준다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request를 요청 할 URL
        String url = Resource.getUrl("list/growup");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i=0;i<jsonArray.length() ; i++)
                            {
                                HealthListData data = new HealthListData();
                                data.setTitle(jsonArray.getJSONObject(i).getString("ex_name"));
                                data.setResld(jsonArray.getJSONObject(i).getInt("ex_id"));
                                adapter.addItem(data);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("VolleyError" ,error+"" );
            }
        });

        queue.add(stringRequest);
    }
}
