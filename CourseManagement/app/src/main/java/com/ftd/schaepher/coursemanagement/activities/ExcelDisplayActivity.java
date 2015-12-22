package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import com.rey.material.app.SimpleDialog;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/30.
 * 文件查看界面以及点击弹出修改弹窗
 * 还需改进：如果已经有数据，且状态不为3，则不发送网络请求
 */
public class ExcelDisplayActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ExcelDisplayActivity::";
    private List<TableCourseMultiline> excelListData;
    private CourseDBHelper dbHelper;
    private String tableName;
    private String workNumber;
    private String identity;
    private boolean hasCommitted;
    private String taskState;
    private String commonTableName;
    private ProgressDialog progress;
    private ExcelAdapter mExcelAdapter;

    private static final TableCourseMultiline EXCEL_HEADER = new TableCourseMultiline("年级", "专业", "专业人数",
            "课程名称", "选修类型", "学分", "学时", "实验学时", "上机学时", "起讫周序",
            "任课教师", "备注");

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
        commonTableName = TableCourseMultiline.class.getSimpleName();
        hasCommitted = true;
        actionBar.setTitle(TaskListActivity.transferTableNameToChinese(tableName));

        SharedPreferences sharedPre = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);
        workNumber = sharedPre.getString(ConstantStr.USER_WORK_NUMBER, "");
        identity = sharedPre.getString(ConstantStr.USER_IDENTITY, "");
        taskState = dbHelper.findById(tableName, TableTaskInfo.class).getTaskState();

        getServerData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            initListData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListData() {

        if (excelListData != null) {
            excelListData.clear();
        }
        // 根据表名查找数据库对应表数据
        dbHelper.dropTable(commonTableName);
        try {
            dbHelper.changeTableName(tableName, commonTableName);
        } catch (Exception e) {
            e.printStackTrace();
            dbHelper.createNewCourseTable();
        }
        List<TableCourseMultiline> list = dbHelper.findAll(TableCourseMultiline.class);

        removeFirstThree(list);

        excelListData.addAll(list);
        if (excelListData.size() > 0) {
            excelListData.add(0, EXCEL_HEADER);
        }
        dbHelper.changeTableName(commonTableName, tableName);

        mExcelAdapter = new
                ExcelAdapter(ExcelDisplayActivity.this, R.layout.list_item_excel_display, excelListData);
        ListView excelListView;
        excelListView = (ListView) findViewById(R.id.lv_excel_display);
        excelListView.setEmptyView(findViewById(R.id.tv_empty_excel));
        excelListView.setAdapter(mExcelAdapter);
        excelListView.setOnItemClickListener(this);
    }

    private void removeFirstThree(List<TableCourseMultiline> list) {
        // 去掉表格前三行
        if (list.size() != 0) {
            int three = 0;
            int size = list.size();
            for (int index = 0; three < 3 && index < size; ) {
                String insertTime = list.get(index).getInsertTime();
                if (insertTime.equals("1") || insertTime.equals("2") || insertTime.equals("3")) {
                    list.remove(index);
                    three++;
                } else {
                    index++;
                }
            }
        }
    }

    private void getServerData() {
        try {
            NetworkManager.getTeacherSelect(tableName, workNumber, new NetworkManager.ResponseCallback() {
                @Override
                public void onResponse(Response response) throws IOException {
                    String responseStr = response.body().string();
                    Loger.w(TAG + "GetServerData", "ResponseData:" + responseStr);
                    List<TableCourseMultiline> list =
                            JsonTools.getJsonList(responseStr, TableCourseMultiline.class);
                    //有返回数据代表交互成功
                    if (list != null) {
                        Loger.d(TAG + "GetServerData",
                                "TaskState:" + taskState + "\ndata:" + excelListData.size());

                        // 插入数据库
                        dbHelper.dropTable(commonTableName);
                        try {
                            dbHelper.changeTableName(tableName, commonTableName);
                        } catch (Exception e) {
                            e.printStackTrace();
                            dbHelper.createNewCourseTable();
                        }
                        hasCommitted = dbHelper.hasCommitted(workNumber);
                        Loger.d(TAG + "GetServerData", "isFinish: " + String.valueOf(hasCommitted));
                        dbHelper.deleteAll(TableCourseMultiline.class);
                        dbHelper.insertAll(list);
                        dbHelper.changeTableName(commonTableName, tableName);

                        // 显示在界面上
                        removeFirstThree(list);
                        excelListData.clear();
                        excelListData.addAll(list);
                        if (excelListData.size() > 0) {
                            excelListData.add(0, EXCEL_HEADER);
                        }
                        if (mExcelAdapter != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mExcelAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initListData();
                                }
                            });
                        }

                    }
                }

                @Override
                public void onFailure(Request request, IOException e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 点击弹出修改弹窗
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != 0) {
            AlertDialog mAlertDialog = initRowWindow(position);
            mAlertDialog.show();
        }
    }

    // 初始化一行数据的窗口
    public AlertDialog initRowWindow(final int position) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ExcelDisplayActivity.this);
        LayoutInflater mInflater = ExcelDisplayActivity.this.getLayoutInflater();
        final View alertDialogView = mInflater.inflate(R.layout.dialog_excel_modify, null);
        initRowWindowData(position, alertDialogView);
        mBuilder.setView(alertDialogView);
        if (hasCommitted || taskState.equals("2")||
                (identity.equals(ConstantStr.ID_TEACHER) && !taskState.equals("0")) ) {
            mBuilder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        } else {
            mBuilder
                    .setPositiveButton("确认填写", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText edtTxDialogTeacher =
                                    (EditText) alertDialogView.findViewById(R.id.edtTx_dialog_teacher);
                            EditText edtTxDialogFromToEnd =
                                    (EditText) alertDialogView.findViewById(R.id.edtTx_dialog_from_to_end);
                            EditText edtTxDialogNote =
                                    (EditText) alertDialogView.findViewById(R.id.edtTx_dialog_note);

                            try {
                                dbHelper.dropTable(commonTableName);
                                dbHelper.changeTableName(tableName, commonTableName);

                                TableCourseMultiline courseModify = new TableCourseMultiline();
                                courseModify.setCourseName(excelListData.get(position).getCourseName());
                                courseModify.setTimePeriod(edtTxDialogFromToEnd.getText().toString());
                                courseModify.setRemark(edtTxDialogNote.getText().toString());
                                courseModify.setTeacherName(edtTxDialogTeacher.getText().toString());
                                courseModify.setWorkNumber(workNumber);
                                dbHelper.update(courseModify);

                                dbHelper.changeTableName(commonTableName, tableName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            onResume();
                        }
                    })
                    .setNeutralButton("删除填写", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                dbHelper.dropTable(commonTableName);
                                dbHelper.changeTableName(tableName, commonTableName);

                                TableCourseMultiline courseModify = new TableCourseMultiline();
                                courseModify.setCourseName(excelListData.get(position).getCourseName());
                                courseModify.setTimePeriod("");
                                courseModify.setRemark("");
                                courseModify.setTeacherName("");
                                courseModify.setWorkNumber("");
                                dbHelper.update(courseModify);

                                dbHelper.changeTableName(commonTableName, tableName);

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
    private void initRowWindowData(int position, View v) {
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
        EditText edtTxDialogTeacher = (EditText) v.findViewById(R.id.edtTx_dialog_teacher);

        TableCourseMultiline rowData = excelListData.get(position);
        tvDialogGrade.setText(rowData.getGrade());
        tvDialogMajor.setText(rowData.getMajor());
        tvDialogNum.setText(rowData.getPeople());
        tvDialogCourseName.setText(rowData.getCourseName());
        tvDialogType.setText(rowData.getCourseType());
        tvDialogCredit.setText(rowData.getCourseCredit());
        tvDialogClassHour.setText(rowData.getCourseHour());
        tvDialogExperimentHour.setText(rowData.getPracticeHour());
        tvDialogComputerHour.setText(rowData.getOnMachineHour());
        edtTxDialogTeacher.setText(rowData.getTeacherName());
        edtTxDialogFromToEnd.setText(rowData.getTimePeriod());
        edtTxDialogNote.setText(rowData.getRemark());
        if (hasCommitted||taskState.equals("2")) {
            edtTxDialogTeacher.setFocusable(false);
            edtTxDialogTeacher.setEnabled(false);
            edtTxDialogFromToEnd.setFocusable(false);
            edtTxDialogFromToEnd.setEnabled(false);
            edtTxDialogNote.setFocusable(false);
            edtTxDialogNote.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.excel_display_activity_actons, menu);
        switch (identity) {
            case ConstantStr.ID_TEACHER:
                menu.findItem(R.id.action_commit_task).setVisible(true);
                break;
//            case ConstantStr.ID_DEPARTMENT_HEAD:
//                menu.findItem(R.id.action_commit_check).setVisible(true);
//                break;
//            case ConstantStr.ID_TEACHING_OFFICE:
//                menu.findItem(R.id.action_finish_task).setVisible(true);
//                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_commit_task:
                Loger.d("TAG", "commit task");
                //点击提交报课逻辑
                if (taskState.equals("2")) {
                    showForbidCommitDialog("报课任务已结束，无法进行提交");
                } else if (taskState.equals("1")) {
                    showForbidCommitDialog("报课信息正在审核中，请等待...");
                } else {
                    if (hasCommitted) {
                        showForbidCommitDialog("您已进行过提交，不能再次提交！");
                    } else {
                        showCommitTaskDialog("是否提交报课", "一旦提交将不能再次修改报课信息！");
                    }
                }
                return true;
            case R.id.action_commit_check:
                if (taskState.equals("2")) {
                    showForbidCommitDialog("报课任务已结束，无法进行提交");
                } else if (taskState.equals("1")) {
                    showForbidCommitDialog("您已进行过提交，不能再次提交！");
                } else {
                    showCommitTaskDialog("是否提交审核", "一旦提交将不能再次修改报课信息！");
                }
                return true;
            case R.id.action_finish_task:
                if (taskState.equals("2")) {
                    showForbidCommitDialog("报课任务已结束");
                } else {
                    showCommitTaskDialog("是否公开报课", "一旦公共将无法再进行修改！");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCommitTaskDialog(String title, String message) {
        final SimpleDialog commitTaskDialog = new SimpleDialog(ExcelDisplayActivity.this);
        commitTaskDialog.message(message)
                .title(title)
                .positiveAction("确定")
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commitTaskDialog.cancel();
                        progress = new ProgressDialog(ExcelDisplayActivity.this);
                        progress.setMessage("提交中...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setCancelable(false);
                        progress.show();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    postToServer();
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

    private void showForbidCommitDialog(String message) {
        final SimpleDialog commitTaskDialog = new SimpleDialog(ExcelDisplayActivity.this);
        commitTaskDialog.message(message)
                .title("提示")
                .positiveAction("确定")
                .positiveActionClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commitTaskDialog.cancel();
                    }
                }).show();
    }

    private void postOneLineToServer() {

    }

    // 提交报课信息到服务器
    private void postToServer() {
        dbHelper.dropTable(commonTableName);
        dbHelper.changeTableName(tableName, commonTableName);
        if (identity.equals(ConstantStr.ID_TEACHER)) {
            List<TableCourseMultiline> commitData =
                    dbHelper.findAllByWhere(TableCourseMultiline.class, "workNumber = '" + workNumber + "'");
            Loger.d("commitData", String.valueOf(commitData));
            Loger.d("commitData", JsonTools.getJsonString(commitData));
            try {
                String result = NetworkManager.postToServerSync(tableName,
                        JsonTools.getJsonString(commitData), NetworkManager.SUBMIT_SELECTED_COURSE);

                sendToast("提交成功");
                hasCommitted = true;
            } catch (IOException e) {
                e.printStackTrace();
                sendToast("提交失败，请重新提交");
            }
        } else {
            List<TableCourseMultiline> commitData = dbHelper.findAll(TableCourseMultiline.class);
            String cbTableName = "cb_" + tableName;
            try {
                String result = NetworkManager.postToServerSync(cbTableName,
                        JsonTools.getJsonString(commitData), NetworkManager.INSERT_OR_UPDATE_CB_TABLE);
                sendToast("提交成功");
                if (identity.equals(ConstantStr.ID_DEPARTMENT_HEAD)) {
                    TableTaskInfo task = new TableTaskInfo("1", tableName);
                    dbHelper.update(task);
                } else {
                    TableTaskInfo task = new TableTaskInfo("2", tableName);
                    dbHelper.update(task);
                }
                taskState = dbHelper.findById(tableName, TableTaskInfo.class).getTaskState();
            } catch (IOException e) {
                e.printStackTrace();
                sendToast("提交失败，请重新提交");
            }
        }
        dbHelper.changeTableName(commonTableName, tableName);
    }

    public void sendToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ExcelDisplayActivity.this, message,
                        Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        if (!hasCommitted && identity.equals(ConstantStr.ID_TEACHER)) {
            final SimpleDialog commitTaskDialog = new SimpleDialog(ExcelDisplayActivity.this);
            commitTaskDialog.message("退出将清除所有修改")
                    .title("提示")
                    .positiveAction("确定")
                    .positiveActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ExcelDisplayActivity.this.finish();
                        }
                    })
                    .negativeAction("取消")
                    .negativeActionClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commitTaskDialog.cancel();
                        }
                    })
                    .show();
        }
    }
}
