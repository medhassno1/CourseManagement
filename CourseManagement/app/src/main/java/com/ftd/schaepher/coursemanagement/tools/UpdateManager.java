package com.ftd.schaepher.coursemanagement.tools;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {

    private Context context;
    private JSONObject json;
    private ProgressDialog proDialog;
    private static final String savePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/Download/";
    private boolean interceptFlag = false;

    public UpdateManager(Context context) {
        this.context = context;
    }

    // 外部接口让主Activity调用
    public void updateVersion() {
        float serverVersionCode = 0;
        try {
            String version = NetworkManager.updateApk();
            Loger.d("VersionDataFromServer", version);
            json = new JSONObject(version);
            serverVersionCode = Float.parseFloat(json.getString("serverVersionCode"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return;
        }

        float localVersionCode = getLocalVersionCode();
        if (serverVersionCode > localVersionCode) {
            showNoticeDialog(localVersionCode, serverVersionCode);
        }
    }


    //获取当前版本号
    private float getLocalVersionCode() {
        float versionCode = 0;
        try {
            versionCode = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    private void showNoticeDialog(float localVersionCode, float serverVersionCode) {
        //提示语
        String updateMsg = "软件有更新，是否更新？" +
                "\n当前版本为：" + localVersionCode +
                "\n最新版本为：" + serverVersionCode;

        Looper.prepare();

        AlertDialog.Builder builder = new Builder(context);
        builder.setTitle("更新");
        builder.setMessage(updateMsg);
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });
        builder.setNegativeButton("以后再说", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog noticeDialog = builder.create();
        noticeDialog.show();
        Looper.loop();
    }

    private void showDownloadDialog() {

        proDialog = new ProgressDialog(context);
        proDialog.setTitle("更新");
        proDialog.setMessage("正在下载最新版本...");
        proDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        proDialog.setMax(100);
        proDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        proDialog.show();

        downloadApk();
    }

    private void downloadApk() {
        Thread downLoadThread = new Thread(downApkRunnable);
        downLoadThread.start();
    }

    private Runnable downApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                String serverApkUrl = json.getString("apkURL");
                URL url = new URL(serverApkUrl);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                Loger.e("路径", file.getPath());
                String downloadDir = file.getAbsolutePath();
                String apkName = json.getString("apkName");

                File apkFile = new File(downloadDir, apkName);
                FileOutputStream fos = new FileOutputStream(apkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numRead = is.read(buf);
                    count += numRead;
                    int proDownloading = (int) (((float) count / length) * 100);
                    proDialog.setProgress(proDownloading);
                    if (numRead <= 0) {
                        proDialog.dismiss();
                        break;
                    }
                    fos.write(buf, 0, numRead);
                } while (!interceptFlag); // 点击取消就停止下载.

                fos.close();
                is.close();
                if (!interceptFlag) {
                    installApk(apkFile);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private void installApk(File apkFile) {
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(apkFile);
        Loger.e("uri", uri.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);

    }
}