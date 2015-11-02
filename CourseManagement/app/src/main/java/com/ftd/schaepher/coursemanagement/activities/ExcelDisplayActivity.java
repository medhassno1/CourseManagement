package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.pojo.Course;
import com.ftd.schaepher.coursemanagement.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件查看界面以及点击弹出修改弹窗
 */
public class ExcelDisplayActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<Course> excelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_display);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_excel_display);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("计算机专业.xls");

        initExcelData();
        initExcelListView();
    }

    private void initExcelData() {
        excelData = new ArrayList<Course>();
        Course courseCur = new Course("年级", "专业", "专业人数", "课程名称", "选修类型", "学分", "学时",
                "实验学时", "上机学时", "起讫周序", "任课教师", "备注");
        excelData.add(courseCur);
    }

    private void initExcelListView() {
        ExcelAdapter mExcelAdapter = new
                ExcelAdapter(ExcelDisplayActivity.this, R.layout.list_item_excel_display, excelData);
        ListView excelListView = (ListView) findViewById(R.id.lv_excel_display);
        excelListView.setAdapter(mExcelAdapter);
        excelListView.setOnItemClickListener(this);
    }

    //点击弹出修改弹窗
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog mAlertDialog = initAlertDialog();
        mAlertDialog.show();
    }

    public AlertDialog initAlertDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExcelDisplayActivity.this);
        LayoutInflater mInflater = ExcelDisplayActivity.this.getLayoutInflater();
        mBuilder.setView(mInflater.inflate(R.layout.dialog_excel_modify, null))
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击确认修改逻辑
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击取消修改逻辑
                    }
                });
        return mBuilder.create();
    }

    /**
     * 文件查看界面listView布局的适配器，引用的布局文件为list_item_excel_display.xml
     */
    public class ExcelAdapter extends ArrayAdapter<Course> {
        private int resourceId;

        public ExcelAdapter(Context context, int resource, List<Course> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Course courseCur = getItem(position);
            View view;
            viewHolder mViewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                mViewHolder = new viewHolder();
                mViewHolder.tvGrade = (TextView) view.findViewById(R.id.tv_grade);
                mViewHolder.tvMajor = (TextView) view.findViewById(R.id.tv_major);
                mViewHolder.tvSum = (TextView) view.findViewById(R.id.tv_sum);
                mViewHolder.tvCourseName = (TextView) view.findViewById(R.id.tv_course_name);
                mViewHolder.tvType = (TextView) view.findViewById(R.id.tv_type);
                mViewHolder.tvCredit = (TextView) view.findViewById(R.id.tv_credit);
                mViewHolder.tvClassHour = (TextView) view.findViewById(R.id.tv_class_hour);
                mViewHolder.tvExperimentHour = (TextView) view.findViewById(R.id.tv_experiment_hour);
                mViewHolder.tvComputerHour = (TextView) view.findViewById(R.id.tv_computer_hour);
                mViewHolder.tvFromToEnd = (TextView) view.findViewById(R.id.tv_from_to_end);
                mViewHolder.tvTeacher = (TextView) view.findViewById(R.id.tv_teacher);
                mViewHolder.tvNote = (TextView) view.findViewById(R.id.tv_note);
                view.setTag(mViewHolder);
            } else {
                view = convertView;
                mViewHolder = (viewHolder) view.getTag();
            }
            mViewHolder.tvGrade.setText(courseCur.getGrade());
            mViewHolder.tvMajor.setText(courseCur.getMajor());
            mViewHolder.tvSum.setText(courseCur.getSum());
            mViewHolder.tvCourseName.setText(courseCur.getCourseName());
            mViewHolder.tvCredit.setText(courseCur.getCredit());
            mViewHolder.tvClassHour.setText(courseCur.getClassHour());
            mViewHolder.tvExperimentHour.setText(courseCur.getExperimentHour());
            mViewHolder.tvComputerHour.setText(courseCur.getComputerHour());
            mViewHolder.tvFromToEnd.setText(courseCur.getFromToEnd());
            mViewHolder.tvTeacher.setText(courseCur.getTeacher());
            mViewHolder.tvNote.setText(courseCur.getNote());
            return view;
        }

        class viewHolder {
            TextView tvGrade;
            TextView tvMajor;
            TextView tvSum;
            TextView tvCourseName;
            TextView tvType;
            TextView tvCredit;
            TextView tvClassHour;
            TextView tvExperimentHour;
            TextView tvComputerHour;
            TextView tvFromToEnd;
            TextView tvTeacher;
            TextView tvNote;
        }
    }

}
