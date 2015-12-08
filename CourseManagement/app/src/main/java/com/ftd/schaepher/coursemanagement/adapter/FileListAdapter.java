package com.ftd.schaepher.coursemanagement.adapter;

/**
 * Created by sxq on 2015/12/8.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * 文件选择的适配器
 */
public class FileListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<File> files;
    private boolean isRoot;
    private LayoutInflater mInflater;

    public FileListAdapter(Context context, ArrayList<File> files, boolean isRoot) {
        this.context = context;
        this.files = files;
        this.isRoot = isRoot;
        mInflater = LayoutInflater.from(context);
        Log.d("file", "fileName:" + files.get(0).getName());
        Log.d("file", "filePath:" + files.get(0).getAbsolutePath());
        Log.d("file", "fileParent:" + files.get(0).getParent());
        Log.d("file", "isRoot:" + isRoot);
        Log.d("file", "----------------------");
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_file, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tv_file_title);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tv_file_type);
            viewHolder.data = (TextView) convertView.findViewById(R.id.tv_file_date);
            viewHolder.size = (TextView) convertView.findViewById(R.id.tv_file_size);
            viewHolder.imgvSplitOne = (ImageView) convertView.findViewById(R.id.imgv_spilt_one);
            viewHolder.imgvSplitTwo = (ImageView) convertView.findViewById(R.id.imgv_spilt_two);
            viewHolder.imgvType = (ImageView) convertView.findViewById(R.id.imgv_file_type);
            viewHolder.layoutImgType = (LinearLayout) convertView.findViewById(R.id.layout_img_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        File file = (File) getItem(position);
        if (position == 0 && !isRoot) {
            viewHolder.title.setText("返回上一级");
            viewHolder.data.setVisibility(View.GONE);
            viewHolder.size.setVisibility(View.GONE);
            viewHolder.type.setVisibility(View.GONE);
            viewHolder.imgvType.setVisibility(View.GONE);
            viewHolder.imgvSplitOne.setVisibility(View.GONE);
            viewHolder.imgvSplitTwo.setVisibility(View.GONE);
            viewHolder.layoutImgType.setVisibility(View.GONE);
        } else {
            viewHolder.data.setVisibility(View.VISIBLE);
            viewHolder.size.setVisibility(View.VISIBLE);
            viewHolder.type.setVisibility(View.VISIBLE);
            viewHolder.imgvType.setVisibility(View.VISIBLE);
            viewHolder.imgvSplitOne.setVisibility(View.VISIBLE);
            viewHolder.imgvSplitTwo.setVisibility(View.VISIBLE);
            viewHolder.layoutImgType.setVisibility(View.VISIBLE);
            String fileName = file.getName();
            viewHolder.title.setText(fileName);
            if (file.isDirectory()) {
                viewHolder.imgvType.setImageResource(R.drawable.folder);
                viewHolder.size.setVisibility(View.GONE);
                viewHolder.imgvSplitOne.setVisibility(View.GONE);
                viewHolder.type.setText("文件夹");
            } else {
                viewHolder.size.setVisibility(View.VISIBLE);
                viewHolder.imgvSplitOne.setVisibility(View.VISIBLE);
                long fileSize = file.length();
                if (fileSize > 1024 * 1024) {
                    float size = fileSize / (1024f * 1024f);
                    viewHolder.size.setText(new DecimalFormat("#.00").format(size) + "MB");
                } else if (fileSize >= 1024) {
                    float size = fileSize / 1024;
                    viewHolder.size.setText(new DecimalFormat("#.00").format(size) + "KB");
                } else {
                    viewHolder.size.setText(fileSize + "B");
                }
                viewHolder.imgvType.setImageResource(R.drawable.other_file);
                int dot = fileName.indexOf('.');
                if (dot > -1 && dot < (fileName.length() - 1)) {
                    String fileType = fileName.substring(dot + 1);
                    if (fileType.length() < 6) {
                        viewHolder.type.setText(fileName.substring(dot + 1) + "文件");
                        if (fileName.substring(dot + 1).equals("xls")) {
                            viewHolder.imgvType.setImageResource(R.drawable.xls_file);
                        }
                    } else {
                        viewHolder.type.setText("未识别");
                    }
                }
            }
            viewHolder.data.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(file.lastModified()));
        }
        return convertView;
    }

    class ViewHolder {
        private TextView title;
        private TextView type;
        private TextView data;
        private TextView size;
        private ImageView imgvType;
        private ImageView imgvSplitOne;
        private ImageView imgvSplitTwo;
        private LinearLayout layoutImgType;
    }
}
