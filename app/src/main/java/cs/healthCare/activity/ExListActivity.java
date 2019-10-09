package cs.healthCare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

public class ExListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private HealthListRecyclerAdapter adapter;
    private SwipeRefreshLayout swipeRefreshExList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ex_list);

         Intent i = getIntent();
         int id = i.getExtras().getInt("list_id");
         recyclerView = findViewById(R.id.ex_recycler);
         swipeRefreshExList = findViewById(R.id.swipeRefreshExList);
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
        sendRequest(id);
    }

    public void sendRequest(final int id){
        // RequestQueue를 새로 만들어준다.
        RequestQueue queue = Volley.newRequestQueue(this);
        // Request를 요청 할 URL
        String url = Resource.getUrl("list/items?id="+id);
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

    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable(){
            @Override
            public void run(){
                Snackbar.make(recyclerView,"Refresh Succes", Snackbar.LENGTH_SHORT).show();
                swipeRefreshExList.setRefreshing(false);
            }//run()
        },500); //recyclerView.postDelayed
    }//onRefresh()
}//class ExListActivity
