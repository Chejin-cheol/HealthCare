package cs.healthCare.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import cs.healthCare.R;


public class ExRoutinFragment extends Fragment {
    private String title;
    private int page;

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
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.ex_routin_fragment,container,false);
        EditText tvLabel = (EditText) view.findViewById(R.id.editText);
        tvLabel.setText(page+"--"+title);
        return view;
    }
}