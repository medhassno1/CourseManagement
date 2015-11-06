package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.pojo.Files;
import com.ftd.schaepher.coursemanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件选择界面
 */
public class FileSelectActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private List<Files> fileListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_file_select);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("文件选择");

        initListView();
    }

    private void initListView() {
        initFileListData();
        FileAdapter mFileAdapter =
                new FileAdapter(FileSelectActivity.this, R.layout.list_item_file,fileListData );
        ListView lvFileList = (ListView) findViewById(R.id.lv_file_list);
        lvFileList.setAdapter(mFileAdapter);
        lvFileList.setOnItemClickListener(this);
    }

    private void initFileListData() {
        fileListData = new ArrayList<Files>();
        Files file = new Files("计算机专业.xls",R.drawable.excel_xls);
        fileListData.add(file);
    }

    //文件列表点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * 文件列表的适配器
     */
    class FileAdapter extends ArrayAdapter<Files> {
        private int resourceId;

        public FileAdapter(Context context, int resource, List<Files> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Files file = getItem(position);
            View view;
            viewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new viewHolder();
                viewHolder.fileImage = (ImageView) view.findViewById(R.id.fileImage);
                viewHolder.fileName = (TextView) view.findViewById(R.id.fileName);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (FileAdapter.viewHolder) view.getTag();
            }

            viewHolder.fileImage.setImageResource(file.getImageId());
            viewHolder.fileName.setText(file.getName());
            return view;
        }

        class viewHolder {
            ImageView fileImage;
            TextView fileName;
        }
    }
}
