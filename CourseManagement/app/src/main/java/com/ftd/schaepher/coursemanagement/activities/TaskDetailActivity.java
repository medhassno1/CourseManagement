package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
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
    private TextView tvTaskRemark;
    private TextView tvTaskState;
    private TextView tvTaskName;
    private TextView tvTaskTerm;
    private ProgressDialog progress;

    private String identity;
    private String relativeTable;
    private TableTaskInfo task;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String filePath;
    private String excelTitle;
    private String taskTerm;
    private String taskName;
    private String workNumber;
    private boolean isFinishCommitTask;

    private SharedPreferences.Editor informationEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        informationEditor = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).edit();
        workNumber = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_WORKNUMBER, "");
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
        tvTaskRemark = (TextView) findViewById(R.id.tv_task_detail_remark);
        tvTaskState = (TextView) findViewById(R.id.tv_task_detail_state);
        tvTaskName = (TextView) findViewById(R.id.tv_task_detail_name);
        cardvTaskDetail = (CardView) findViewById(R.id.cardv_task_detail);
        cardvTaskDetail.setOnClickListener(this);

        Loger.d("relativeTable", relativeTable);
        task = (TableTaskInfo) dbHelper.findById(relativeTable, TableTaskInfo.class);
        Loger.d("TAG", task.toString());
        taskTerm = task.getYear() + task.getSemester();
        tvTaskTerm.setText(taskTerm);
        tvDepartmentDeadline.setText(task.getDepartmentDeadline());
        tvTeacherDeadline.setText(task.getTeacherDeadline());
        tvTaskRemark.setText(task.getRemark());
        tvTaskState.setText(TaskListActivity.taskStateMap(task.getTaskState()));
        taskName = TaskListActivity.transferTableNameToChinese(task.getRelativeTable());
        tvTaskName.setText(taskName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        identity = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_IDENTITY, null);
        getMenuInflater().inflate(R.menu.task_detail_activity_actions, menu);
        if (identity.equals(ConstantTools.ID_TEACHER)) {
            menu.removeItem(R.id.action_export_file);
            menu.findItem(R.id.action_commit_task).setVisible(true);
        }
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
            case R.id.action_commit_task:
                Loger.d("TAG", "commit task");
                //点击提交报课逻辑
                isFinishCommitTask = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getBoolean("isFinishCommitTask", false);
                if (isFinishCommitTask){
                    showForbidCommitDialog();
                }else {
                    showCommitTaskDialog();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCommitTaskDialog() {
        final SimpleDialog commitTaskDialog = new SimpleDialog(TaskDetailActivity.this);
        commitTaskDialog.message("一旦提交将不能再次修改报课信息！")
                .title("是否提交报课")
                .positiveAction("确定")
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commitTaskDialog.cancel();
                        progress = new ProgressDialog(TaskDetailActivity.this);
                        progress.setMessage("提交中...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setCancelable(false);
                        progress.show();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    commitTask();
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
                        commitTaskDialog.cancel();
                    }
                }).show();
    }

    private void showForbidCommitDialog() {
        final SimpleDialog commitTaskDialog = new SimpleDialog(TaskDetailActivity.this);
        commitTaskDialog.message("您已提交过报课，不能再次提交！")
                .title("提示")
                .positiveAction("确定")
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commitTaskDialog.cancel();
                    }
                }).show();
    }

    private void commitTask() {
        tableName = task.getRelativeTable();

        SQLiteDatabase db = openOrCreateDatabase("teacherclass.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS TableCourseMultiline");
        db.execSQL("ALTER TABLE " + tableName + " RENAME TO TableCourseMultiline");

        List<TableCourseMultiline> commitData = dbHelper.db.findAllByWhere(TableCourseMultiline.class, "workNumber = '" + workNumber+"'");
        Loger.d("commitData", String.valueOf(commitData));
        db.execSQL("ALTER TABLE TableCourseMultiline RENAME TO " + tableName);
        db.close();
        Loger.d("commitData", new JsonTools().getJsonString(commitData));
        try {
            NetworkManager.postJsonString(tableName,new JsonTools().getJsonString(commitData),ConstantTools.ACTION_INSERT_TABLE);
            showCommitTaskSucceed();
            informationEditor.putBoolean("isFinishCommitTask",true);
            informationEditor.apply();
        } catch (IOException e) {
            e.printStackTrace();
            showCommitTaskError();
        }

    }

    private void showCommitTaskError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TaskDetailActivity.this, "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCommitTaskSucceed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TaskDetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
            }
        });
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

        SQLiteDatabase db = openOrCreateDatabase("teacherclass.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS TableCourseMultiline");
        db.execSQL("ALTER TABLE " + tableName + " RENAME TO TableCourseMultiline");

        copyExcel(dbHelper.findAll(TableCourseMultiline.class));

        db.execSQL("ALTER TABLE TableCourseMultiline RENAME TO " + tableName);
        db.close();
    }

    public void copyExcel(List<TableCourseMultiline> list) throws IOException, BiffException, WriteException {
        File file = new File(filePath + ".xls");
        InputStream ins = getResources().openRawResource(R.raw.blank_table);
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
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
}
