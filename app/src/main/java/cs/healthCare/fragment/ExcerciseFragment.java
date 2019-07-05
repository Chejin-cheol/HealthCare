package cs.healthCare.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs.healthCare.R;
import cs.healthCare.adapter.RecyclerAdapter;
import cs.healthCare.adapter.ViewPagerAdaptor;


public class ExcerciseFragment extends Fragment{
    private ViewPager viewPager;
    private ViewPagerAdaptor adator;
    private TabLayout tabLayout;
    private RecyclerAdapter adapter;
    private ViewGroup parent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(parent != null)
        {
            return parent;
        }

        parent = (ViewGroup)inflater.inflate(R.layout.excercise_fragment, container, false);

        viewPager = parent.findViewById(R.id.pager);
        adator = new ViewPagerAdaptor(getFragmentManager());
        tabLayout = parent.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adator);

        return parent;
    }




    public static ExcerciseFragment newInstance()
    {
        return new ExcerciseFragment();
    }

}

