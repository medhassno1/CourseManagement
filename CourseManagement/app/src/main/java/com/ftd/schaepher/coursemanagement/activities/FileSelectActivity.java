package com.ftd.schaepher.coursemanagement.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.adapter.FileListAdapter;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.tools.ExcelTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件选择界面
 */
public class FileSelectActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    //  尝试改成调用系统的文件管理器
    private ListView lvFileList;
    private TextView tvPath;
    private FileListAdapter mFileAdapter;
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
        Loger.i("path", sdCardRoot);
        File folder = new File(sdCardRoot);
        initData(folder);
    }


    private void initData(File folder) {
        boolean isRoot = (folder.getParent() == null); // 是否为根目录
        tvPath.setText(folder.getAbsolutePath());
        ArrayList<File> files = new ArrayList<>();
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
        mFileAdapter = new FileListAdapter(this, files, isRoot);
        lvFileList.setAdapter(mFileAdapter);
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
        File file = (File) mFileAdapter.getItem(position);
        if (!file.canRead()) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("权限不足")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        } else if (file.isDirectory()) {
            initData(file);
        } else {
            importFile(file);
        }
    }

    private void importFile(File file) {
        final String path = file.getAbsolutePath();
        boolean isTeacherFile = getIntent().getBooleanExtra("isRequireImportTeacherFile", false);
        Loger.i("path", path);

        // 直接调用excelTools.isTrueFileName()会出错，暂时无解
        if (!path.endsWith(".xls")) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("文件类型错误，请选择excel文件")
                    .setPositiveButton
                            (android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                    .show();

        } else if (isTeacherFile) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否导入教师表")
                    .setPositiveButton
                            (android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ExcelTools excelTools = new ExcelTools();
                                    excelTools.setPath(path);
                                    List<TableUserTeacher> teachersList = excelTools.readTeacherExcel();
                                    //导入教师表
                                    for (int i = 0; i < teachersList.size(); i++) {
                                        CourseDBHelper dbHelper = new CourseDBHelper(FileSelectActivity.this);
                                        TableUserTeacher teacher = teachersList.get(i);
                                        try {
                                            dbHelper.insert(teacher);
                                        } catch (Exception e) {
                                        }
                                    }
                                    finish();
                                }
                            })
                    .setNegativeButton
                            (android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("是否导入开课表")
                    .setPositiveButton
                            (android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    setResult(RESULT_OK, new Intent().putExtra("fileName", path));
                                    finish();
                                }
                            })
                    .setNegativeButton
                            (android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                    .show();
        }
    }
}
