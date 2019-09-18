//마이페이지

package cs.healthCare.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import cs.healthCare.R;
import cs.healthCare.activity.UserInfoUpdateActivity;

import static cs.healthCare.activity.MainActivity.mid;
import static cs.healthCare.activity.MainActivity.mname;


public class MyPageFragment extends Fragment {
    Context _context;
    View root = null;
    ImageButton bt_myInfo;
    TextView id,name;

    public static MyPageFragment newInstance()
    {
        return new MyPageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(root !=null)
        {
            return root;
        }
        root = inflater.inflate(R.layout.mypage_fragment, container, false);

        id=root.findViewById(R.id.tv_myPageId);
        name=root.findViewById(R.id.tv_myPageNick);
        id.setText("아이디 : "+mid);
        name.setText("닉네임 : "+mname);
        bt_myInfo = root.findViewById(R.id.btn_myInfo);

        bt_myInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoUpdateActivity.class);
                startActivity(intent);
            }
        });//end of btMember

        return root;
    }//end of onCreateView

}//end of class MainFragement2
