package cs.healthCare.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

import cs.healthCare.R;


public class UserInfoUpdateActivity extends Activity {

    private Uri mImageCaptureUri;
    private ImageView iv_UserPhoto;
    ImageButton bt_myPagePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_update_activity);


        //https://jeongchul.tistory.com/287

    }//end of void onCreate
}//end of MyPageMyInfoActivity
