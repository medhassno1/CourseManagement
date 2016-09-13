package com.ftd.schaepher.coursemanagement.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.tools.TransferUtils;
import com.rey.material.app.SimpleDialog;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

    private CardView cardvTaskInfo;
    private CardView cardvTaskDetail;
    private TextView tvDepartmentDeadline;
    private TextView tvTeacherDeadline;
    private TextView tvTaskState;
    private TextView tvTaskName;
    private TextView tvTaskTerm;
    private ProgressDialog progress;

    private String relativeTable;
    private TableTaskInfo task;
    private TableTaskInfo editedTask;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String filePath;
    private String excelTitle;
    private String taskTerm;
    private String taskName;
    //    private String workNumber;
    private String toTableName;
    private String identity;

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

        relativeTable = getIntent().getStringExtra("relativeTable");
        Loger.i("TAG", "relativeTable" + relativeTable);
        dbHelper = new CourseDBHelper(this);
        initWidgetValue();
        initUserInformation();
    }

    // 初始化当前用户的数据
    private void initUserInformation() {
        SharedPreferences sharedPre =
                getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);

        identity = sharedPre.getString(ConstantStr.USER_IDENTITY, "");

        switch (identity) {
            case ConstantStr.ID_DEPARTMENT_HEAD:
            case ConstantStr.ID_TEACHING_OFFICE:
                cardvTaskInfo.setOnClickListener(this);
                break;
            case ConstantStr.ID_TEACHER:
            default:
                break;
        }
    }

    // 初始化控件数据
    private void initWidgetValue() {
        tvTaskTerm = (TextView) findViewById(R.id.tv_task_detail_term);
        tvTeacherDeadline = (TextView) findViewById(R.id.tv_task_detail_teacher_deadline);
        tvDepartmentDeadline = (TextView) findViewById(R.id.tv_task_detail_department_deadline);
        tvTaskState = (TextView) findViewById(R.id.tv_task_detail_state);
        tvTaskName = (TextView) findViewById(R.id.tv_task_detail_name);
        cardvTaskDetail = (CardView) findViewById(R.id.cardv_task_detail);
        cardvTaskInfo = (CardView) findViewById(R.id.cardv_task_info);
        cardvTaskDetail.setOnClickListener(this);

        Loger.d("relativeTable", relativeTable);
        task = dbHelper.findById(relativeTable, TableTaskInfo.class);
        Loger.d("TAG", task.toString());
        taskTerm = task.getYear() + task.getSemester();
        tvTaskTerm.setText(taskTerm);
        tvDepartmentDeadline.setText(task.getDepartmentDeadline());
        tvTeacherDeadline.setText(task.getTeacherDeadline());
        tvTaskState.setText(TransferUtils.stateCode2Zh(task.getTaskState()));
        initTaskStateColor();
        taskName = TransferUtils.en2Zh(task.getRelativeTable());
        tvTaskName.setText(taskName);
    }

    private void initTaskStateColor() {
        switch (task.getTaskState()) {
            case "0":
                tvTaskState.setTextColor(Color.RED);
                break;
            case "1":
                tvTaskState.setTextColor(Color.GREEN);
                break;
            case "2":
                tvTaskState.setTextColor(Color.GRAY);
                break;
            default:
                tvTaskState.setTextColor(Color.RED);
                break;
        }
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
                                            sendToast("导出成功");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            sendToast("导出失败");
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

    public void sendToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TaskDetailActivity.this, message,
                        Toast.LENGTH_SHORT).show();
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
        //删除空表的前三行会出问题，暂时解决办法是删除表格的前三行
        list.remove(0);
        list.remove(0);
        list.remove(0);
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
        switch (v.getId()) {
            case R.id.cardv_task_detail:
                Intent intent = new Intent(TaskDetailActivity.this, ExcelDisplayActivity.class);
                intent.putExtra("tableName", task.getRelativeTable());
                startActivity(intent);
                break;
            case R.id.cardv_task_info:
                if (!task.getTaskState().equals(2)) {
                    showTaskEditDialog();
                }
                break;
            default:
                break;
        }

    }

    private void showTaskEditDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_task_info, null, false);
        TextView termTv = (TextView) view.findViewById(R.id.tv_task_term);
        final EditText depDeadlineEdt = (EditText) view.findViewById(R.id.edtTx_task_department_deadline);
        final EditText teacherDeadlineEdt = (EditText) view.findViewById(R.id.edtTx_task_teacher_deadline);
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        termTv.setText(tvTaskTerm.getText());
        String depDeadlineStr = tvDepartmentDeadline.getText().toString();
        depDeadlineEdt.setText(depDeadlineStr);
        depDeadlineEdt.setEnabled(false);

        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            depDeadlineEdt.setEnabled(true);
            depDeadlineEdt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            depDeadlineEdt.setText(String.format(Locale.CHINA,
                                    "%d%02d%02d", year, monthOfYear + 1, dayOfMonth));
                        }
                    }, year, month, day).show();
                }
            });
        }

        String teacherDeadline = tvTeacherDeadline.getText().toString();
        teacherDeadlineEdt.setText(teacherDeadline);
        teacherDeadlineEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TaskDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        teacherDeadlineEdt.setText(String.format(Locale.CHINA,
                                "%d%02d%02d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("修改截止时间")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String depDeadline = depDeadlineEdt.getText().toString();
                        String teacherDeadline = teacherDeadlineEdt.getText().toString();

                        if (Integer.valueOf(depDeadline) > Integer.valueOf(teacherDeadline)) {
                            updateTaskInfo(depDeadline, teacherDeadline);
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TaskDetailActivity.this,
                                            "截止日期必须小于审核日期", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTaskInfo(final String depDeadline, final String teacherDeadline) {
        editedTask = new TableTaskInfo(task);
        editedTask.setDepartmentDeadline(depDeadline);
        editedTask.setTeacherDeadline(teacherDeadline);
        NetworkManager.updateTaskInfo(JsonTools.getJsonString(editedTask), new NetworkManager.ResponseCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.body() != null) {
                    String responseStr = response.body().string();
                    Loger.d("updateTime", responseStr);
                    if (responseStr.equals("true")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dbHelper.update(editedTask);
                                tvDepartmentDeadline.setText(depDeadline);
                                tvTeacherDeadline.setText(teacherDeadline);
                                Toast.makeText(TaskDetailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TaskDetailActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("!!!!!!!!!!!!!!", "error");
            }
        });

    }
}
