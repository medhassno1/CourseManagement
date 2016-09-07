package com.ftd.schaepher.coursemanagement.tools;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ftd.schaepher.coursemanagement.CMApp;
import com.ftd.schaepher.coursemanagement.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sxq on 2016/9/7.
 * 中文和英文转换工具
 * eg:数据库表的英文名与专业名称的中文转换
 * eg:任务状态代码0,1,2与中文的转换
 */
public class TransferUtils {

    public static String[] majorArr;
    public static String[] tableArr;
    public static int length;

    /**
     * zh:中文
     * en:英文
     */
    public static String zh2En(@NonNull String major) {
        if (length == 0) {
            initMajor();
        }
        for (int i = 0; i < length; i++) {
            if (majorArr[i].equals(major)){
                return tableArr[i];
            }
        }
        return "";
    }

    public static String en2Zh(@NonNull String table) {
        if (length == 0) {
            initMajor();
        }
        StringBuilder tablePrefix = new StringBuilder();
        Pattern pattern = Pattern.compile("[a-zA-Z_]*");
        Matcher matcher = pattern.matcher(table);
        if (matcher.find()) {
            tablePrefix.append(matcher.group());
        }
        for (int i=0; i<length; i++) {
            if (tableArr[i].equals(tablePrefix.toString())) {
                return majorArr[i];
            }
        }
        return "";
    }

    /**
     * 任务状态映射
     */
    public static String stateCode2Zh(String stateCode) {
        if (TextUtils.isEmpty(stateCode)) {
            return null;
        }
        switch (stateCode) {
            case "0":
                return "进行中";
            case "1":
                return "审核中";
            case "2":
                return "已结束";
            default:
                return null;
        }
    }


    public static void initMajor() {
        if (length == 0) {
            majorArr = CMApp.getAppContext().getResources().getStringArray(R.array.major_name);
            tableArr = CMApp.getAppContext().getResources().getStringArray(R.array.table_name);
            if (majorArr.length != tableArr.length) {
                throw new UnsupportedOperationException("专业和数据库表配置文件出错，请检查专业与数据库表名是否一致且对应");
            }
            length = majorArr.length;
        }
    }

}
