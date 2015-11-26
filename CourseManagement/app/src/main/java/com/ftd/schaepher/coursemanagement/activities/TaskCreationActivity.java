package com.ftd.schaepher.coursemanagement.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.tools.ExcelTools;
import com.ftd.schaepher.coursemanagement.widget.WheelView;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by sxq on 2015/10/31.
 * 发布任务界面
 */
public class TaskCreationActivity extends AppCompatActivity
        implements View.OnFocusChangeListener, View.OnClickListener {

    private EditText edtTxDepartmentDeadline;
    private EditText edtTxTeacherDeadline;
    private EditText edtTxTaskName;
    private EditText edtTxTaskTeam;
    private EditText edtTxTaskRemark;
    private ImageView imgvFileImg;
    private TextView tvFileName;
    private Button btnImportFile;
    private ProgressDialog progress;
    private CourseDBHelper dbHelper;
    private String filePath;
    private String fileName;
    private String tableCourseName;
    private String year;
    private String semester;

    private static final int RELEASE = 1;
    private static final int RELEASE_FAILURE = 2;
    private static final String[] SEMESTER = new String[]{"01", "02", "03", "04"};

    private List<String> termYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_add_task);
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("发布报课任务");
        initWidgetAndListener();

        dbHelper = new CourseDBHelper(TaskCreationActivity.this);
    }

    // 初始化控件及绑定监听事件
    private void initWidgetAndListener() {
        edtTxDepartmentDeadline = (EditText) findViewById(R.id.edtTx_add_task_department_deadline);
        edtTxTeacherDeadline = (EditText) findViewById(R.id.edtTx_add_task_teacher_deadline);
        edtTxTaskName = (EditText) findViewById(R.id.edtTx_add_task_name);
        edtTxTaskTeam = (EditText) findViewById(R.id.edtTx_add_task_team);
        edtTxTaskRemark = (EditText) findViewById(R.id.edtTx_add_task_note);
        btnImportFile = (Button) findViewById(R.id.btn_add_task_import_file);
        imgvFileImg = (ImageView) findViewById(R.id.imgv_add_task_file_img);
        tvFileName = (TextView) findViewById(R.id.tv_add_task_file_name);

        edtTxTeacherDeadline.setOnFocusChangeListener(this);
        edtTxDepartmentDeadline.setOnFocusChangeListener(this);
        edtTxTaskName.setOnFocusChangeListener(this);
        edtTxTaskTeam.setOnFocusChangeListener(this);

        edtTxTaskName.setOnClickListener(this);
        btnImportFile.setOnClickListener(this);
        edtTxDepartmentDeadline.setOnClickListener(this);
        edtTxTeacherDeadline.setOnClickListener(this);
        edtTxTaskTeam.setOnClickListener(this);
        edtTxDepartmentDeadline.setInputType(InputType.TYPE_NULL);
        edtTxTeacherDeadline.setInputType(InputType.TYPE_NULL);
        edtTxTaskName.setInputType(InputType.TYPE_NULL);
        edtTxTaskTeam.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_creation_activity_actions, menu);
        return true;
    }

    // 标题栏图标的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_release_task:
                if (!isPassValidate()) {
                    return true;
                }
                final SimpleDialog notificationDialog = new SimpleDialog(TaskCreationActivity.this);
                notificationDialog.title("是否确认发布此任务？")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                notificationDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationDialog.cancel();
                        progress = new ProgressDialog(TaskCreationActivity.this);
                        progress.setMessage("发布任务中...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setCancelable(false);
                        progress.show();
                        Message msg = new Message();
                        msg.what = RELEASE;
                        mHandler.sendMessage(msg);
                    }
                });
                notificationDialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notificationDialog.cancel();
                    }
                });
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isPassValidate() {
        if (edtTxTaskName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择开课专业", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tvFileName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请导入文件", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //建表
    private void createTable() {
        SQLiteDatabase db = openOrCreateDatabase("teacherclass.db", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS TableCourseMultiline");
        db.execSQL(CourseDBHelper.CREATE_TABLE_COURSE_MULTILINE);

        TableCourseMultiline course = new TableCourseMultiline();
        //解析Excel表格
        ExcelTools excelTools = new ExcelTools();
        excelTools.setPath(filePath);
        List<TableCourseMultiline> courseList = excelTools.readCourseExcel();
        //数据存入数据库
        for (int i = 0; i < courseList.size(); i++) {
            course = courseList.get(i);
            dbHelper.insert(course);
        }
        //改名
        db.execSQL("ALTER TABLE TableCourseMultiline RENAME TO " + tableCourseName);
        db.close();
    }

    // 获取即将发布的任务的信息,未完成
    private TableTaskInfo getNewTaskInformation() {
        TableTaskInfo newTask = new TableTaskInfo();
        newTask.setYear(year);
        newTask.setSemester(semester);
        newTask.setTaskState("0");
        newTask.setDepartmentDeadline(edtTxDepartmentDeadline.getText().toString());
        newTask.setTeacherDeadline(edtTxTeacherDeadline.getText().toString());
        newTask.setRemark(edtTxTaskRemark.getText().toString());
        newTask.setRelativeTable(transferTaskNameToEnglish(edtTxTaskName.getText().toString()) + year + semester);
        return newTask;
    }

    // 控件的焦点监听事件
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(v);
        }
    }

    // 控件的点击事件
    @Override
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        final SimpleDialog simpleDialog = new SimpleDialog(TaskCreationActivity.this);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        switch (v.getId()) {
            case R.id.edtTx_add_task_team:
                selectTerm();
                break;

            case R.id.edtTx_add_task_department_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxDepartmentDeadline.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            case R.id.edtTx_add_task_teacher_deadline:
                new DatePickerDialog(TaskCreationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        edtTxTeacherDeadline.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
                break;

            case R.id.edtTx_add_task_name:
                simpleDialog.items(new String[]{"计算机（实验班）", "计算机（卓越班）", "计算机专业",
                        "软件工程专业", "数学类（实验班）", "数学类", "网络工程专业", "信息安全专业"}, 0)
                        .title("选择专业")
                        .positiveAction("确认")
                        .negativeAction("取消")
                        .show();
                simpleDialog.positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edtTxTaskName.setText(simpleDialog.getSelectedValue());
                        simpleDialog.cancel();
                    }
                });
                simpleDialog.negativeActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        simpleDialog.cancel();
                    }
                });
                break;

            case R.id.btn_add_task_import_file:
                startActivityForResult(new Intent(TaskCreationActivity.this, FileSelectActivity.class), 1);
                break;

            default:
                break;
        }
    }

    private void selectTerm() {
        //计算年份，给出近十年可供选择
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());
        int yearCur = mCalendar.get(Calendar.YEAR);
//        Log.d("TaskCreationActivity", yearCur);
        if (termYear == null) {
            termYear = new ArrayList<String>();
            for (int i = yearCur - 5; i < yearCur + 5; i++) {
                termYear.add(String.valueOf(i));
            }
        }
        View wheelView = LayoutInflater.from(this).inflate(R.layout.dialog_wheel_view, null);
        final WheelView wvSelectTermYear = (WheelView) wheelView.findViewById(R.id.wheel_view_term_year);
        final WheelView wvSelectTermDay = (WheelView) wheelView.findViewById(R.id.wheel_view_term_day);
        wvSelectTermYear.setOffset(1);
        wvSelectTermYear.setItems(termYear);
        wvSelectTermYear.setSeletion(5);
        wvSelectTermDay.setOffset(1);
        wvSelectTermDay.setItems(Arrays.asList(SEMESTER));
        wvSelectTermDay.setSeletion(0);

        new AlertDialog.Builder(this)
                .setTitle("选择学期")
                .setView(wheelView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        year = wvSelectTermYear.getSeletedItem();
                        semester = wvSelectTermDay.getSeletedItem();
                        edtTxTaskTeam.setText(year+semester);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RELEASE:
                    new Thread() {
                        @Override
                        public void run() {
                            TableTaskInfo task = getNewTaskInformation();
                            tableCourseName = task.getRelativeTable();
                            try {
                                createTable();
                                dbHelper.insert(task);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Message msg = new Message();
                                msg.what = RELEASE_FAILURE;
                                mHandler.sendMessage(msg);
                            } finally {
                                progress.cancel();
                                finish();
                            }
                        }
                    }.start();
                    break;

                case RELEASE_FAILURE:
                    Toast.makeText(TaskCreationActivity.this, "发布错误，请重新发布", Toast.LENGTH_SHORT).show();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imgvFileImg.setVisibility(View.VISIBLE);
            filePath = data.getStringExtra("fileName");
            fileName = filePath.split("/")[filePath.split("/").length - 1];
            tvFileName.setText(fileName);
            Log.d("filePath", filePath);
        }
    }

    // 任务名映射
    public String transferTaskNameToEnglish(String string) {
        switch (string) {
            case "计算机（卓越班）":
                return "tc_com_exc";
            case "计算机专业":
                return "tc_com_nor";
            case "计算机（实验班）":
                return "tc_com_ope";
            case "信息安全专业":
                return "tc_inf_sec";
            case "数学类":
                return "tc_math_nor";
            case "数学类（实验班）":
                return "tc_math_ope";
            case "网络工程专业":
                return "tc_net_pro";
            case "软件工程专业":
                return "tc_soft_pro";
            default:
                return "";
        }
    }
}