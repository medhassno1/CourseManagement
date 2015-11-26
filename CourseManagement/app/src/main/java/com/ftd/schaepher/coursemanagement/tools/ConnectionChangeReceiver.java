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
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null && Data.isFirstLoseNetConnect()) {
            Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
            Data.setIsFirstLoseNetConnect(false);
            Data.setIsFirstNetConnect(true);
            Log.i("netstatus", "网络不可用");
        }
        if (networkInfo != null&&networkInfo.isConnected() && Data.isFirstNetConnect()) {
            Toast.makeText(context, "网络恢复", Toast.LENGTH_SHORT).show();
            Data.setIsFirstNetConnect(false);
            Data.setIsFirstLoseNetConnect(true);
            Log.i("netstatus", "网络恢复");
        }


    }
}
