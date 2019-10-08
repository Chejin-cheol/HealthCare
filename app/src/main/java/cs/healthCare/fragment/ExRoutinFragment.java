package cs.healthCare.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import cs.healthCare.R;
import cs.healthCare.activity.ExDescriptionActivity;
import cs.healthCare.activity.ExListActivity;
import cs.healthCare.activity.MainActivity;
import cs.healthCare.adapter.HealthListRecyclerAdapter;
import cs.healthCare.adapter.RecyclerAdapter;
import cs.healthCare.model.Data;
import cs.healthCare.model.HealthListData;
import cs.healthCare.network.Resource;


public class ExRoutinFragment extends Fragment {
    private View view;
    private RecyclerAdapter adapter;

    public static ExRoutinFragment newInstance(int page, String title) {
        ExRoutinFragment fragment = new ExRoutinFragment();
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
        view = (ViewGroup)inflater.inflate(R.layout.ex_routin_fragment,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();

        adapter.setItemClickListener(new RecyclerAdapter.ItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Toast.makeText(getContext() , ""+position , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext() , ExListActivity.class);
                intent.putExtra("list_id", (int)view.getTag());
                startActivity(intent);
            }
        });


        Data data = new Data();
        Data data1 = new Data();
        Data data2 = new Data();Data data3 = new Data();Data data4 = new Data();Data data5 = new Data();Data data6 = new Data();
        Data data7 = new Data();






        data.setTitle("풀 업");
        adapter.addItem(data);
        data1.setTitle("벤치프레스");
        adapter.addItem(data1);
        data2.setTitle("딥스");
        adapter.addItem(data2);
        data3.setTitle("에어 스쿼트");
        adapter.addItem(data3);
        data4.setTitle("런지");
        adapter.addItem(data4);
        data5.setTitle("루마니안 데드리프트");
        adapter.addItem(data5);
        data6.setTitle("플랭크");
        adapter.addItem(data6);
        data7.setTitle("크런치");
        adapter.addItem(data7);

        recyclerView.setAdapter(adapter);

//        sendRequest();
        return view;
    }


}