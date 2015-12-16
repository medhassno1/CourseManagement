package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.rey.material.app.SimpleDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * Created by sxq on 2015/10/31.
 * 任务详情页面
 */
public class TaskDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView cardvTaskDetail;
    private TextView tvDepartmentDeadline;
    private TextView tvTeacherDeadline;
    private TextView tvTaskState;
    private TextView tvTaskName;
    private TextView tvTaskTerm;
    private ProgressDialog progress;

    private String relativeTable;
    private TableTaskInfo task;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String filePath;
    private String excelTitle;
    private String taskTerm;
    private String taskName;
    //    private String workNumber;
    private String toTableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);
        toTableName = TableCourseMultiline.class.getSimpleName();

//        workNumber = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE).getString(ConstantStr.USER_WORK_NUMBER, "");
        relativeTable = getIntent().getStringExtra("relativeTable");
        Loger.i("TAG", "relativeTable" + relativeTable);
        dbHelper = new CourseDBHelper(this);
        initWidgetValue();
    }

    // 初始化控件数据
    private void initWidgetValue() {
        tvTaskTerm = (TextView) findViewById(R.id.tv_task_detail_term);
        tvTeacherDeadline = (TextView) findViewById(R.id.tv_task_detail_teacher_deadline);
        tvDepartmentDeadline = (TextView) findViewById(R.id.tv_task_detail_department_deadline);
//        tvTaskRemark = (TextView) findViewById(R.id.tv_task_detail_remark);
        tvTaskState = (TextView) findViewById(R.id.tv_task_detail_state);
        tvTaskName = (TextView) findViewById(R.id.tv_task_detail_name);
        cardvTaskDetail = (CardView) findViewById(R.id.cardv_task_detail);
        cardvTaskDetail.setOnClickListener(this);

        Loger.d("relativeTable", relativeTable);
        task = dbHelper.findById(relativeTable, TableTaskInfo.class);
        Loger.d("TAG", task.toString());
        taskTerm = task.getYear() + task.getSemester();
        tvTaskTerm.setText(taskTerm);
        tvDepartmentDeadline.setText(task.getDepartmentDeadline());
        tvTeacherDeadline.setText(task.getTeacherDeadline());
        tvTaskState.setText(TaskListActivity.taskStateMap(task.getTaskState()));
        taskName = TaskListActivity.transferTableNameToChinese(task.getRelativeTable());
        tvTaskName.setText(taskName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail_activity_actions, menu);
        return true;
    }

    // 标题栏图标点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_export_file:
                final SimpleDialog notificationDialog = new SimpleDialog(TaskDetailActivity.this);
                notificationDialog.title("是否导出文件")
                        .positiveAction("确定")
                        .positiveActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notificationDialog.cancel();
                                progress = new ProgressDialog(TaskDetailActivity.this);
                                progress.setMessage("导出中...");
                                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progress.setCancelable(false);
                                progress.show();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            exportFile();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            closeProgress();
                                        }
                                    }
                                }.start();
                            }
                        }).negativeAction("取消")
                        .negativeActionClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                notificationDialog.cancel();
                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void closeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.cancel();
            }
        });
    }

    //导出文件
    private void exportFile() throws Exception {

        filePath = Environment.getExternalStorageDirectory().getAbsoluteFile().toString()
                + "/" + tvTaskName.getText().toString();
        tableName = task.getRelativeTable();

        dbHelper.dropTable(toTableName);
        dbHelper.changeTableName(tableName, toTableName);

        copyExcel(dbHelper.findAll(TableCourseMultiline.class));

        dbHelper.changeTableName(toTableName, tableName);
    }

    public void copyExcel(List<TableCourseMultiline> list) throws IOException, BiffException, WriteException {
        File file = new File(filePath + ".xls");
        InputStream ins = getResources().openRawResource(R.raw.blank_table);
        OutputStream os = new FileOutputStream(file);
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();

        Workbook book = Workbook.getWorkbook(file);
        Sheet sheet = book.getSheet(0);
        // 获取行
        int row = sheet.getRows();
        System.out.println(row);
        int column;
        WritableWorkbook wbook = Workbook.createWorkbook(file, book); // 根据book创建一个操作对象
        WritableSheet sh = wbook.getSheet(0);// 得到一个工作对象

        CellFormat cellFormat;
        if (!sh.getCell(0, 1).getContents().equals("")) {
            cellFormat = sh.getCell(0, 1).getCellFormat();
        } else {
            cellFormat = sh.getCell(0, 2).getCellFormat();
        }
        WritableCellFormat cellFormatLeft = new WritableCellFormat(cellFormat);//相同格式，左对齐
        cellFormatLeft.setAlignment(Alignment.LEFT);
        WritableCellFormat cellFormatCenter = new WritableCellFormat(cellFormat);//相同格式，居中
        cellFormatCenter.setAlignment(Alignment.CENTRE);

        //设置excel表格标题
        String term = taskTerm.substring(4, 6);
        if (term.equals("01")) {
            term = "上学期";
        } else if (term.equals("02")) {
            term = "下学期";
        }
        excelTitle = taskTerm.substring(0, 4) + "学年" + term + taskName + "开课计划书";
        Label label = new Label(0, 0, excelTitle, sh.getCell(0, 0).getCellFormat());
        sh.addCell(label);

        // 从最后一行开始加
        for (int i = 0; i < list.size(); i++, row++) {
            column = 0;
            label = new Label(column++, row, list.get(i).getGrade(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getMajor(), cellFormatLeft);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getPeople(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getCourseName(), cellFormatLeft);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getCourseType(), cellFormatLeft);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getCourseCredit(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getCourseHour(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getPracticeHour(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getOnMachineHour(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getTimePeriod(), cellFormatCenter);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getTeacherName(), cellFormatLeft);
            sh.addCell(label);
            label = new Label(column++, row, list.get(i).getRemark(), cellFormatLeft);
            sh.addCell(label);
        }
        wbook.write();
        wbook.close();

    }

    // 点击查看文件跳转逻辑
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(TaskDetailActivity.this, ExcelDisplayActivity.class);
        intent.putExtra("tableName", task.getRelativeTable());
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbHelper.close();
    }

}
