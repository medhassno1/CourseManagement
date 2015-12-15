package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.widget.RefreshableView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sxq on 2015/10/31.
 * 教学办登录默认主界面---任务主界面
 */
public class TaskListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemLongClickListener, View.OnClickListener {
    //    删除任务还没做
    private static final String TAG = "TaskListActivity";
    private Toolbar mToolbar;
    private TextView tvOwnName;
    private Spinner spinnerSelectTerm;
    private List<TableTaskInfo> taskListData;
    private String workNumber;
    private String identity;
    private String selectedTerm;
    private CourseDBHelper dbHelper;
    private boolean isSupportDoubleBackExit;
    private long betweenDoubleBackTime;
    private SharedPreferences.Editor selfInforEditor;
    private TaskAdapter mTaskAdapter;
    private ListView mListView;
    private TextView tvDelete;
    private RefreshableView refreshableView;
    private PopupWindow popupWindow;
    private ProgressDialog mProgress;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_task_jxb);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("报课任务列表");
        setNavViewConfig();
        setSupportDoubleBackExit(true);
        mListView = (ListView) findViewById(R.id.lv_task_list);
        mListView.setEmptyView(findViewById(R.id.tv_empty_listview));
        refreshableView = (RefreshableView) findViewById(R.id.refreshTask_view);
        dbHelper = new CourseDBHelper(TaskListActivity.this);
        taskListData = dbHelper.findAll(TableTaskInfo.class);
        spinnerSelectTerm = (Spinner) findViewById(R.id.spinner_select_term);
        mProgress = new ProgressDialog(TaskListActivity.this);

        try {
            getServerData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                Loger.i("TAG111", "开始onRefresh()");
                try {
                    getServerData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinnerData();
        initUserInformation();

    }

    // 初始化当前用户的数据
    private void initUserInformation() {
        String selfName = "";
        SharedPreferences sharedPre =
                getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE);

        workNumber = sharedPre.getString(ConstantStr.USER_WORK_NUMBER, "");
        identity = sharedPre.getString(ConstantStr.USER_IDENTITY, "");

        switch (identity) {
            case ConstantStr.ID_TEACHER:
                TableUserTeacher teacher = dbHelper
                        .findById(workNumber, TableUserTeacher.class);
                selfName = teacher == null ? "" : teacher.getName();
                break;
            case ConstantStr.ID_TEACHING_OFFICE:
                TableUserTeachingOffice office = dbHelper
                        .findById(workNumber, TableUserTeachingOffice.class);
                selfName = office == null ? "" : office.getName();
                break;
            case ConstantStr.ID_DEPARTMENT_HEAD:
                TableUserDepartmentHead departmentHead = dbHelper
                        .findById(workNumber, TableUserDepartmentHead.class);
                selfName = departmentHead == null ? "" : departmentHead.getName();
                break;
            default:
                break;
        }
        tvOwnName.setText(selfName);
        selfInforEditor = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE).edit();
        selfInforEditor.putString(ConstantStr.USER_NAME, selfName);
        selfInforEditor.apply();
    }

    // 从服务器获取数据
    private void getServerData() throws IOException {
        String tableName = ConstantStr.TABLE_TASK_INFO;
        NetworkManager.getJsonString(tableName, new NetworkManager.ResponseCallback() {
            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                try {
                    List list = JsonTools.getJsonList(responseStr, TableTaskInfo.class);
                    Loger.w(TAG, "jsonList" + list.toString());
                    dbHelper.deleteAll(TableTaskInfo.class);
                    dbHelper.insertAll(list);

                    if (mTaskAdapter != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshSpinner();
                                mTaskAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshSpinner();
                                displayTaskList();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {

            }
        });
    }

    /**
     * 获取对应学期的任务列表数据
     *
     * @param selectedTerm
     */
    private void getDataByTerm(String selectedTerm) {
        if (taskListData != null) {
            taskListData.clear();
        }
        String year = selectedTerm.substring(0, 4);
        String semester = selectedTerm.substring(4, 6);
        List<TableTaskInfo> list = dbHelper.findAllByWhere(TableTaskInfo.class,
                "year=\"" + year + "\" and semester=\"" + semester + "\"");
        taskListData.addAll(list);
    }

    // 显示任务列表数据
    private void displayTaskList() {
        mTaskAdapter = new TaskAdapter(this, R.layout.list_item_task, taskListData);
        mListView.setAdapter(mTaskAdapter);
        mListView.setOnItemClickListener(this);
        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            mListView.setOnItemLongClickListener(this);
        }
        Loger.i("TAG", "显示数据");
    }

    // 点击任务列表项跳转操作
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
        intent.putExtra("relativeTable",
                String.valueOf(taskListData.get(position).getRelativeTable()));
        startActivity(intent);
    }

    //长按任务列表项
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Loger.d("longclick", String.valueOf(refreshableView.getCurrentStatus()));
        if (refreshableView.getCurrentStatus() == RefreshableView.STATUS_REFRESH_FINISHED) {
            String taskName = taskListData.get(position).getRelativeTable();
            Loger.d("longclick", taskName);

            View v = this.getLayoutInflater().inflate(R.layout.popup_dialog_delete, null);
            popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            tvDelete = (TextView) v.findViewById(R.id.tv_popup_delete);
            tvDelete.setOnClickListener(this);
            tvDelete.setTag(taskName);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(view, ((getWindowManager()
                    .getDefaultDisplay().getWidth() / 3)), -(3 * view.getHeight() / 2));
        }
        return true;
    }

    // 任务名映射
    public static String transferTableNameToChinese(String string) {
        StringBuilder strTaskName = new StringBuilder();
        Pattern pattern = Pattern.compile("[a-zA-Z_]*");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            strTaskName.append(matcher.group());
        }
//        Loger.d("TAG", strTaskName.toString());
        switch (strTaskName.toString()) {
            case "tc_com_exc":
                return "计算机（卓越班）";
            case "tc_com_nor":
                return "计算机专业";
            case "tc_com_ope":
                return "计算机（实验班）";
            case "tc_inf_sec":
                return "信息安全专业";
            case "tc_math_nor":
                return "数学类";
            case "tc_math_ope":
                return "数学类（实验班）";
            case "tc_net_pro":
                return "网络工程专业";
            case "tc_soft_pro":
                return "软件工程专业";
            default:
                return null;
        }
    }

    // 任务状态映射
    public static String taskStateMap(String string) {
        if (string == null) {
            return null;
        }
        switch (string) {
            case "0":
                return "进行中";
            case "1":
                return "审核中";
            case "2":
                return "已结束";
            default:
                return null;
        }
    }

    // 下拉选项框初始化
    private void setSpinnerData() {
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refreshSpinner();
        spinnerSelectTerm.setAdapter(spinnerAdapter);
        spinnerSelectTerm.setSelection(0);
        spinnerSelectTerm.setOnItemSelectedListener(onItemSelectedListener);
    }

    // 下拉选项框数据刷新
    public void refreshSpinner() {
        List<String> semesterList = dbHelper.getSemesterList();
        spinnerAdapter.clear();
        spinnerAdapter.addAll(semesterList);
        if (spinnerAdapter != null) {
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private android.widget.AdapterView.OnItemSelectedListener
            onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectedTerm = (String) parent.getItemAtPosition(position);
            getDataByTerm(selectedTerm);

            if (mTaskAdapter != null) {
                mTaskAdapter.notifyDataSetChanged();
            } else {
                displayTaskList();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_popup_delete:
                popupWindow.dismiss();
                mProgress.setMessage("删除任务中...");
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setCancelable(false);
                mProgress.show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            String taskName = tvDelete.getTag().toString();
                            Loger.d("taskName", "tvDelete" + taskName);
                            NetworkManager.postToServerSync(taskName, "", NetworkManager.DELETE_TASK);
                            dbHelper.deleteByID(TableTaskInfo.class, taskName);
                            dbHelper.dropTable(taskName);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshSpinner();
                                    setSpinnerData();
                                    if (mProgress.isShowing()) {
                                        mProgress.cancel();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Loger.d("delete", "程序崩溃");
                            clossProcess();
                        }
                    }
                }.start();
                break;
            default:
                break;
        }
    }

    private void clossProcess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgress.isShowing()) {
                    mProgress.cancel();
                }
            }
        });
    }

    /**
     * 任务列表的适配器
     */
    class TaskAdapter extends ArrayAdapter<TableTaskInfo> {
        private int resourceId;

        public TaskAdapter(Context context, int resource, List<TableTaskInfo> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableTaskInfo task = getItem(position);
            View view;
            viewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new viewHolder();
                viewHolder.taskState = (TextView) view.findViewById(R.id.tv_task_state);
                viewHolder.taskName = (TextView) view.findViewById(R.id.tv_task_name);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (TaskAdapter.viewHolder) view.getTag();
            }

            viewHolder.taskState.setText(taskStateMap(task.getTaskState()));
            viewHolder.taskName.setText(transferTableNameToChinese(task.getRelativeTable()));
            return view;
        }

        class viewHolder {
            TextView taskState;
            TextView taskName;
        }
    }

    // 左侧菜单的初始设置
    private void setNavViewConfig() {
        identity = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_IDENTITY, null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_base);

        if (identity.equals(ConstantStr.ID_TEACHER)) {
            navigationView.getMenu().removeItem(R.id.nav_teacher_list);
        }
        tvOwnName = (TextView) navigationView.inflateHeaderView(R.layout.nav_header_base)
                .findViewById(R.id.nav_own_name);
        navigationView.getMenu().findItem(R.id.nav_task_list).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // 左菜单点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_task_list:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_teacher_list:
                startActivity(new Intent(TaskListActivity.this, TeacherListActivity.class));
                item.setChecked(true);
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(TaskListActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_own_information:
                startActivity(new Intent(TaskListActivity.this, TeacherDetailActivity.class));

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        closeNavigationView();
                    }
                }, 200);
                break;
            default:
                break;
        }
        return false;
    }

    private void closeNavigationView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        });
    }

    // 添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        identity = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_IDENTITY, null);
        getMenuInflater().inflate(R.menu.task_list_activity_actions, menu);
        if (!identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            menu.removeItem(R.id.action_add_task);
        }
        return true;
    }

    // 标题栏上的按钮图标点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_task:
                Loger.i(TAG, "click add icon");
                startActivity(new Intent(TaskListActivity.this, TaskCreationActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // 双击退出
    public void setSupportDoubleBackExit(boolean isDoubleBackExit) {
        this.isSupportDoubleBackExit = isDoubleBackExit;
    }

    // 系统返回键事件
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isSupportDoubleBackExit) {
            if ((System.currentTimeMillis() - betweenDoubleBackTime) > 2000) {
                Toast.makeText(TaskListActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                betweenDoubleBackTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
