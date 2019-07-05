package cs.healthCare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import cs.healthCare.R;
import cs.healthCare.activity.ExListActivity;
import cs.healthCare.adapter.RecyclerAdapter;
import cs.healthCare.model.Data;


public class ExPartFragment extends Fragment {

    private RecyclerAdapter adapter;
    private ViewGroup parent;


    public static ExPartFragment newInstance(int page, String title) {
        ExPartFragment fragment = new ExPartFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parent = (ViewGroup)inflater.inflate(R.layout.ex_part_fragment, container, false);
        RecyclerView recyclerView = parent.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        adapter.setItemClickListener(new RecyclerAdapter.ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Toast.makeText(getContext() , ""+position , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(parent.getContext() , ExListActivity.class);
                intent.putExtra("list_id", (int)view.getTag());
                Log.i("규규규",(int)view.getTag()+"");
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        sendRequest();
        return parent;

    }
    public void sendRequest(){

        // RequestQueue를 새로 만들어준다.
        RequestQueue queue = Volley.newRequestQueue(getContext());

        // Request를 요청 할 URL
        String url ="http://61.84.24.251:3000/list";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for(int i=0;i< jsonArray.length();i++)
                            {
                                    Data data = new Data();
                                     data.setTitle(jsonArray.getJSONObject(i).getString("health_name"));
                                     data.setResld(jsonArray.getJSONObject(i).getInt("health_index"));
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
            }
        });

        queue.add(stringRequest);

    }
}
