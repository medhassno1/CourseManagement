package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件查看界面以及点击弹出修改弹窗
 */
public class ExcelDisplayActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<TableCourseMultiline> excelListData;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String userName;
    private String workNumber;
    private boolean isFinishCommitTask;
    private String toTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_display);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_excel_display);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        excelListData = new ArrayList<>();
        dbHelper = new CourseDBHelper(this);

        tableName = getIntent().getStringExtra("tableName");
        actionBar.setTitle(TaskListActivity.transferTableNameToChinese(tableName));
        toTableName = TableCourseMultiline.class.getSimpleName();

        SharedPreferences sharedPre = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);
        isFinishCommitTask = sharedPre.getBoolean("isFinishCommitTask", false);
        userName = sharedPre.getString(ConstantStr.USER_NAME, "");
        workNumber = sharedPre.getString(ConstantStr.USER_WORKNUMBER, "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        TableCourseMultiline excelHeader = new TableCourseMultiline("年级", "专业", "专业人数",
                "课程名称", "选修类型", "学分", "学时", "实验学时", "上机学时", "起讫周序",
                "任课教师", "备注");
        excelListData.add(excelHeader);

        // 根据表名查找数据库对应表数据
        dbHelper.dropTable(toTableName);
        dbHelper.changeTableName(tableName, toTableName);
        excelListData.addAll(dbHelper.findAll(TableCourseMultiline.class));
        dbHelper.changeTableName(toTableName, tableName);

        ExcelAdapter mExcelAdapter = new
                ExcelAdapter(ExcelDisplayActivity.this, R.layout.list_item_excel_display, excelListData);
        ListView excelListView;
        excelListView = (ListView) findViewById(R.id.lv_excel_display);
        excelListView.setAdapter(mExcelAdapter);
        excelListView.setOnItemClickListener(this);
    }

    // 点击弹出修改弹窗
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            AlertDialog mAlertDialog = initAlertDialog(position);
            mAlertDialog.show();
        }
    }

    public AlertDialog initAlertDialog(final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExcelDisplayActivity.this);
        LayoutInflater mInflater = ExcelDisplayActivity.this.getLayoutInflater();
        final View alertDialogView = mInflater.inflate(R.layout.dialog_excel_modify, null);
        initAlertDialogData(position, alertDialogView);
        mBuilder.setView(alertDialogView);
        if (isFinishCommitTask) {
            mBuilder.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            mBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText edtTxDialogFromToEnd =
                            (EditText) alertDialogView.findViewById(R.id.edtTx_dialog_from_to_end);
                    EditText edtTxDialogNote =
                            (EditText) alertDialogView.findViewById(R.id.edtTx_dialog_note);

                    try {
                        dbHelper.dropTable(toTableName);
                        dbHelper.changeTableName(tableName, toTableName);

                        TableCourseMultiline courseModify = new TableCourseMultiline();
                        courseModify.setCourseName(excelListData.get(position).getCourseName());
                        courseModify.setTimePeriod(edtTxDialogFromToEnd.getText().toString());
                        courseModify.setRemark(edtTxDialogNote.getText().toString());
                        courseModify.setTeacherName(userName);
                        courseModify.setWorkNumber(workNumber);
                        dbHelper.update(courseModify);

                        dbHelper.changeTableName(toTableName, tableName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onResume();
                }
            })
                    .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dbHelper.dropTable(toTableName);
                                dbHelper.changeTableName(tableName, toTableName);

                                TableCourseMultiline courseModify = new TableCourseMultiline();
                                courseModify.setCourseName(excelListData.get(position).getCourseName());
                                courseModify.setTimePeriod("");
                                courseModify.setRemark("");
                                courseModify.setTeacherName("");
                                courseModify.setWorkNumber("");
                                dbHelper.update(courseModify);

                                dbHelper.changeTableName(toTableName, tableName);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onResume();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        return mBuilder.create();
    }

    // 设置弹窗的数据
    private void initAlertDialogData(int position, View v) {
        TextView tvDialogGrade = (TextView) v.findViewById(R.id.tv_dialog_grade);
        TextView tvDialogMajor = (TextView) v.findViewById(R.id.tv_dialog_major);
        TextView tvDialogNum = (TextView) v.findViewById(R.id.tv_dialog_sum);
        TextView tvDialogCourseName = (TextView) v.findViewById(R.id.tv_dialog_course_name);
        TextView tvDialogType = (TextView) v.findViewById(R.id.tv_dialog_type);
        TextView tvDialogCredit = (TextView) v.findViewById(R.id.tv_dialog_credit);
        TextView tvDialogClassHour = (TextView) v.findViewById(R.id.tv_dialog_class_hour);
        TextView tvDialogExperimentHour = (TextView) v.findViewById(R.id.tv_dialog_experiment_hour);
        TextView tvDialogComputerHour = (TextView) v.findViewById(R.id.tv_dialog_computer_hour);
        EditText edtTxDialogFromToEnd = (EditText) v.findViewById(R.id.edtTx_dialog_from_to_end);
        EditText edtTxDialogNote = (EditText) v.findViewById(R.id.edtTx_dialog_note);

        tvDialogGrade.setText(excelListData.get(position).getGrade());
        tvDialogMajor.setText(excelListData.get(position).getMajor());
        tvDialogNum.setText(excelListData.get(position).getPeople());
        tvDialogCourseName.setText(excelListData.get(position).getCourseName());
        tvDialogType.setText(excelListData.get(position).getCourseType());
        tvDialogCredit.setText(excelListData.get(position).getCourseCredit());
        tvDialogClassHour.setText(excelListData.get(position).getCourseHour());
        tvDialogExperimentHour.setText(excelListData.get(position).getPracticeHour());
        tvDialogComputerHour.setText(excelListData.get(position).getOnMachineHour());
        edtTxDialogFromToEnd.setText(excelListData.get(position).getTimePeriod());
        edtTxDialogNote.setText(excelListData.get(position).getRemark());
        if (isFinishCommitTask) {
            edtTxDialogFromToEnd.setEnabled(false);
            edtTxDialogNote.setEnabled(false);
        }
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

    /**
     * 文件查看界面listView布局的适配器，引用的布局文件为list_item_excel_display.xml
     */
    public class ExcelAdapter extends ArrayAdapter<TableCourseMultiline> {
        private int resourceId;

        public ExcelAdapter(Context context, int resource, List<TableCourseMultiline> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableCourseMultiline courseCur = getItem(position);
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
            mViewHolder.tvSum.setText(courseCur.getPeople());
            mViewHolder.tvCourseName.setText(courseCur.getCourseName());
            mViewHolder.tvCredit.setText(courseCur.getCourseCredit());
            mViewHolder.tvClassHour.setText(courseCur.getCourseHour());
            mViewHolder.tvType.setText(courseCur.getCourseType());
            mViewHolder.tvExperimentHour.setText(courseCur.getPracticeHour());
            mViewHolder.tvComputerHour.setText(courseCur.getOnMachineHour());
            mViewHolder.tvFromToEnd.setText(courseCur.getTimePeriod());
            mViewHolder.tvTeacher.setText(courseCur.getTeacherName());
            mViewHolder.tvNote.setText(courseCur.getRemark());
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
