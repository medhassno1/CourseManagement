package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
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
    private String taskId;
    private TableTaskInfo task;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String filePath;
    private static final int EXPORT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_task_detail);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务详情");
        mActionBar.setDisplayHomeAsUpEnabled(true);

        taskId = getIntent().getStringExtra("taskId");
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

        Log.d("TASKID", taskId);
        task = (TableTaskInfo) dbHelper.findById(taskId, TableTaskInfo.class);
        Log.d("TAG", task.toString());
        String taskTerm = task.getYear() + task.getSemester();
        tvTaskTerm.setText(taskTerm);
        tvDepartmentDeadline.setText(task.getDepartmentDeadline());
        tvTeacherDeadline.setText(task.getTeacherDeadline());
        tvTaskRemark.setText(task.getRemark());
        tvTaskState.setText(TaskListActivity.taskStateMap(task.getTaskState()));
        tvTaskName.setText(TaskListActivity.transferTableNameToEnglish(task.getRelativeTable()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        identity = getSharedPreferences("userInformation", MODE_PRIVATE).getString("identity", null);
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
                                Message msg = new Message();
                                msg.what = EXPORT;
                                mHandler.sendMessage(msg);
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
                Log.d("TAG", "commit task");
                //点击提交报课逻辑
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        int length = sheet.getRows();
        System.out.println(length);
        int column;
        WritableWorkbook wbook = Workbook.createWorkbook(file, book); // 根据book创建一个操作对象
        WritableSheet sh = wbook.getSheet(0);// 得到一个工作对象
        // 从最后一行开始加
        for (int i = 0; i < list.size(); i++, length++) {
            column = 0;
            Label label = new Label(column++, length, list.get(i).getGrade());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getMajor());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getPeople());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getCourseName());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getCourseType());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getCourseCredit());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getCourseHours());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getPracticeHour());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getOnMachineHour());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getTimePeriod());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getTeacherName());
            sh.addCell(label);
            label = new Label(column++, length, list.get(i).getRemark());
            sh.addCell(label);
        }
        wbook.write();
        wbook.close();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case EXPORT:
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                exportFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                progress.cancel();
                            }
                        }
                    }.start();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    // 点击查看文件跳转逻辑
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(TaskDetailActivity.this, ExcelDisplayActivity.class);
        intent.putExtra("tableName", task.getRelativeTable());
        startActivity(intent);
    }
}
