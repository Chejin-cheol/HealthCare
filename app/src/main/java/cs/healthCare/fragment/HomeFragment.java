package cs.healthCare.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import cs.healthCare.R;
import cs.healthCare.activity.GrowUpExerciseActivity;
import cs.healthCare.service.CharacterService;

public class HomeFragment extends Fragment implements View.OnClickListener {
        public static Context _context;
        Intent _intent;
        CharacterService _service;
        View  root;
        ImageView character;
        ViewGroup button_set;

        private final float ButtonRate = 0.15f;
        private final float ButtonMarginRate = 0.05f;

        //서비스 연결
        ServiceConnection _connection = new ServiceConnection() {
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                // 서비스와 연결되었을 때 호출되는 메서드
                CharacterService.CharacterBinder binder =
                        (CharacterService.CharacterBinder) service;
                _service = binder.getService();
                _service.setCharacterView(character);
            }
            public void onServiceDisconnected(ComponentName name) {
            }
        };

    public static HomeFragment newInstance()
    {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.home_fragment, container, false);
            _context = container.getContext();
            character = (ImageView) root.findViewById(R.id.character);
            button_set = (ViewGroup)root.findViewById(R.id.button_set);
            character.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(button_set.getVisibility() == View.VISIBLE)
                    {
                        button_set.setVisibility(View.GONE);
                    }
                    else
                    {
                        button_set.setVisibility(View.VISIBLE);
                    }
                }
            });

            _intent = new Intent(_context , CharacterService.class);
            _context.bindService(_intent , _connection ,Context.BIND_AUTO_CREATE);
            _context.startService(_intent);
            setLayout();
        }
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.home_exercise :
                Intent intent = new Intent(_context , GrowUpExerciseActivity.class);
                startActivity(intent);
                break;
            case  R.id.home_friendly :
                break;
            case R.id.home_feed :
                break;
            case R.id.home_play :
                break;
        }
    }
    private void setLayout()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) _context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        int buttonSize = (int)(metrics.widthPixels * ButtonRate);
        int buttonMargin = (int)( metrics.widthPixels * ButtonMarginRate );

        for(int i=0;i<button_set.getChildCount() ;i++)
        {
            View v = button_set.getChildAt(i);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonSize , buttonSize);
            params.setMargins(buttonMargin , buttonMargin ,buttonMargin ,buttonMargin);
            v.setLayoutParams(params);
            v.setOnClickListener(this);
        }
    }
}


