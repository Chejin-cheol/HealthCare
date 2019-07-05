package cs.healthCare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import cs.healthCare.R;
import cs.healthCare.fragment.ExcerciseFragment;
import cs.healthCare.fragment.HomeFragment;
import cs.healthCare.fragment.MyPageFragment;
import cs.healthCare.fragment.RankFragment;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private ExcerciseFragment excerciseFragment = new ExcerciseFragment();
    private RankFragment rankFragment = new RankFragment();
    private MyPageFragment myPageFragment = new MyPageFragment();
    public static String mid,mname="";
    Intent info = getIntent();
    Fragment active ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction().commit();


        Intent intent = getIntent();

        try{
            mname=intent.getExtras().getString("nickname");
            mid=intent.getExtras().getString("id");
        }
        catch (NullPointerException e){

        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        // 첫 화면 지정

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, homeFragment).commitAllowingStateLoss();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                final  FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.replace(R.id.frame_layout, excerciseFragment).commit();
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.replace(R.id.frame_layout, myPageFragment).commit();
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.replace(R.id.frame_layout, homeFragment).commit();
//                        sendRequest();
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.replace(R.id.frame_layout, rankFragment).commit();
//                        sendRequest();
                        break;
                    }
                }

                return true;
            }
        });

    }





}
