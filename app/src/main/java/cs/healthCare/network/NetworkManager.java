package cs.healthCare.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
    public static final int NETWORK_CONNECT_ERROR = 0;
    public static final int NETWORK_CONNECT = 1;

    public static int getInternet(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return 1;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return 2;
            }
        }
        return 0;
    }

}

/// 인터넷 연결 로직
//    int network;
//                network = NetworkManager.Get_Internet(LoginActivity.this);
//                        Log.i("asdasd123","dddd"+network);
//                        if (network == NETWORK_CONNECT_ERROR)
//                        {
//                        Toast.makeText(LoginActivity.this,"인터넷 연결을 확인해주세요.",Toast.LENGTH_SHORT).show();
//                        Log.i("123123","123123"+network);
//                        }



