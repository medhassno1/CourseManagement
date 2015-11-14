package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.tools.ExcelTools;

import net.tsz.afinal.annotation.sqlite.Table;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件选择界面
 */
public class FileSelectActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private ListView lvFileList;
    private TextView tvPath;
    private FileListAdapter mFileAdpter;
    private TextView tvItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_file_select);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("选择文件");

        initView();
    }

    private void initView() {
        lvFileList = (ListView) findViewById(R.id.lv_file_list);
        tvPath = (TextView) findViewById(R.id.path);
        tvItemCount = (TextView) findViewById(R.id.item_count);
        lvFileList.setOnItemClickListener(this);

        String sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i("path", sdCardRoot);
        File folder = new File(sdCardRoot);
        initData(folder);
    }


    private void initData(File folder) {
        boolean isRoot = (folder.getParent() == null);//是否为根目录
        tvPath.setText(folder.getAbsolutePath());
        ArrayList<File> files = new ArrayList<File>();
        if (!isRoot) {
            files.add(folder.getParentFile());
        }
        File[] filterFiles = folder.listFiles();
        tvItemCount.setText(filterFiles.length + "项");
        if (null != filterFiles && filterFiles.length > 0) {
            for (File file : filterFiles) {
                files.add(file);
            }
        }
        mFileAdpter = new FileListAdapter(this, files, isRoot);
        lvFileList.setAdapter(mFileAdpter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        File file = (File) mFileAdpter.getItem(position);
        if (!file.canRead()) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("权限不足").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        } else if (file.isDirectory()) {
            initData(file);
        } else {
            importFile(file);
        }
    }

    private void importFile(File file) {
        final String path = file.getAbsolutePath();
        boolean isTeacherFile = getIntent().getBooleanExtra("isRequireImportTeacherFile", false);
        Log.i("path", path);

        if (!path.endsWith(".xls")) {   //直接调用excelTools.isTrueFileName()会出错，暂时无解
            new AlertDialog.Builder(this).setTitle("提示").setMessage("文件类型错误，请选择excel文件").setPositiveButton
                    (android.R.string.ok, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();

        } else if (isTeacherFile) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("是否导入教师表").setPositiveButton
                    (android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ExcelTools excelTools = new ExcelTools();
                            excelTools.setPath(path);
                            List<TableUserTeacher> teachersList = excelTools.readTeacherExcel();
                            //导入教师表
                            for (int i = 0; i < teachersList.size(); i++) {
                                CourseDBHelper dbHelper = new CourseDBHelper();
                                dbHelper.createDataBase(FileSelectActivity.this);
                                TableUserTeacher teacher = teachersList.get(i);
                                dbHelper.insert(teacher);
                            }
                            finish();
                        }
                    }).setNegativeButton
                    (android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        } else {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("是否导入开课表").setPositiveButton
                    (android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setResult(RESULT_OK,new Intent().putExtra("fileName",path));
                            finish();
                        }
                    }).setNegativeButton
                    (android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    /**
     * 文件选择的适配器
     */
    private class FileListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<File> files;
        private boolean isRoot;
        private LayoutInflater mInflater;

        public FileListAdapter(Context context, ArrayList<File> files, boolean isRoot) {
            this.context = context;
            this.files = files;
            this.isRoot = isRoot;
            mInflater = LayoutInflater.from(context);
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
                convertView.setTag(viewHolder);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_file_title);
                viewHolder.type = (TextView) convertView.findViewById(R.id.tv_file_type);
                viewHolder.data = (TextView) convertView.findViewById(R.id.tv_file_date);
                viewHolder.size = (TextView) convertView.findViewById(R.id.tv_file_size);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            File file = (File) getItem(position);
            if (position == 0 && !isRoot) {
                viewHolder.title.setText("返回上一级");
                viewHolder.data.setVisibility(View.GONE);
                viewHolder.size.setVisibility(View.GONE);
                viewHolder.type.setVisibility(View.GONE);
            } else {
                String fileName = file.getName();
                viewHolder.title.setText(fileName);
                if (file.isDirectory()) {
                    viewHolder.size.setText("文件夹");
                    viewHolder.size.setTextColor(Color.RED);
                    viewHolder.type.setVisibility(View.GONE);
                    viewHolder.data.setVisibility(View.GONE);
                } else {
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
                    int dot = fileName.indexOf('.');
                    if (dot > -1 && dot < (fileName.length() - 1)) {
                        viewHolder.type.setText(fileName.substring(dot + 1) + "文件");
                    }
                    viewHolder.data.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(file.lastModified()));
                }
            }
            return convertView;
        }

        class ViewHolder {
            private TextView title;
            private TextView type;
            private TextView data;
            private TextView size;
        }
    }

}
