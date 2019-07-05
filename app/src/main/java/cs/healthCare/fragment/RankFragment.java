package cs.healthCare.fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cs.healthCare.R;


public class RankFragment extends Fragment {
    Context _context;
    View  root;
    Button bt_c, bt_ex1, bt_ex2;

    public static RankFragment newInstance()
    {
        return new RankFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.rank_fragment, container, false);
        _context = container.getContext();


        return root;





    }




}

