package com.ftd.schaepher.coursemanagement.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取网络连接管理者
        ConnectivityManager connectivityManager=(ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        NetworkInfo mobNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo  wifiNetInfo=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
            Toast.makeText(context, "网络不可用",
                    Toast.LENGTH_SHORT).show();
            Log.i("net", "网络不可用");
            Log.i("net1",mobNetInfo.getTypeName());
        } else {
            Toast.makeText(context, "网络恢复",
                    Toast.LENGTH_SHORT).show();
            Log.i("net2","网络恢复");
        }
    }
}
