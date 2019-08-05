package cs.healthCare.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import cs.healthCare.R;

public class HomeFragment extends Fragment {
        Context _context;
        View  root;
        ImageView character;


    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_fragment, container, false);
        _context = container.getContext();
        character = (ImageView) root.findViewById(R.id.character);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(character);
        Glide.with(this).load(R.drawable.character_default).into(gifImage);
        return root;
    }


   /* public void sendRequest(){    //-----------------------------제이슨

        // RequestQueue를 새로 만들어준다.
        RequestQueue queue = Volley.newRequestQueue(_context);

        // Request를 요청 할 URL
        String url ="http://61.84.24.251:3000/list";



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONArray array = new JSONArray(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        textView.setText("Response is: "+ response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        queue.add(stringRequest);

    }
    */    //-----------------------------제이슨

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        sendRequest();    //제이슨

    }
}


