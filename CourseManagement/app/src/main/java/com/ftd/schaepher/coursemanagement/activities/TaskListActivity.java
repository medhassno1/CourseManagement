package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sxq on 2015/10/31.
 * 教学办登录默认主界面---任务主界面
 */
public class TaskListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "TaskListActivity";
    private static final int CLOSE_NAV = 1;
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
    private SharedPreferences.Editor ownInformationSaveEditor;
    private TaskAdapter mTaskAdapter;


    // 任务名映射
    public static String transferTableNameToChinese(String string) {
        StringBuffer strTaskName = new StringBuffer();
        Pattern pattern = Pattern.compile("[a-zA-Z_]*");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            strTaskName.append(matcher.group());
        }
//        Log.d("TAG", strTaskName.toString());
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

        dbHelper = new CourseDBHelper(TaskListActivity.this);
        //dbHelper.deleteAll(TableTaskInfo.class);

        initSpinner();
        initTaskListData();

        initUserInformation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initSpinner();
    }

    private void initSpinner() {
        spinnerSelectTerm = (Spinner) findViewById(R.id.spinner_select_term);
        final ArrayAdapter<Set> mAdapter = new ArrayAdapter<Set>(this,R.layout.spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Set termData = new HashSet();
        final List<TableTaskInfo> taskInfo ;
        taskInfo =  dbHelper.findAll(TableTaskInfo.class);

        for (TableTaskInfo list:taskInfo){
            termData.add(list.getYear()+list.getSemester());
        }
        TreeSet soredTermData = new TreeSet(termData);
        soredTermData.comparator();
        mAdapter.addAll(soredTermData);
        spinnerSelectTerm.setAdapter(mAdapter);
        spinnerSelectTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTerm = (String) parent.getItemAtPosition(position);
                try {
                    taskListData.clear();
                    for (TableTaskInfo task : taskInfo) {
                        String term = task.getYear() + task.getSemester();
                        if (term.equals(selectedTerm)) {
                            Log.d("TAG", task.toString());
                            taskListData.add(task);
                        }
                    }

                    mTaskAdapter.notifyDataSetChanged();
                }catch(Exception e){
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // 左侧菜单的初始设置
    private void setNavViewConfig() {
        identity = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_IDENTITY, null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_base);

        if (identity.equals(ConstantTools.ID_TEACHER)) {
            navigationView.getMenu().removeItem(R.id.nav_teacher_list);
        }
        tvOwnName = (TextView) navigationView.inflateHeaderView(R.layout.nav_header_base)
                .findViewById(R.id.nav_own_name);
        navigationView.getMenu().findItem(R.id.nav_task_list).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setSupportDoubleBackExit(boolean isDoubleBackExit) {
        this.isSupportDoubleBackExit = isDoubleBackExit;
    }

    private void initUserInformation() {
        String ownName = "";
        workNumber = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_WORKNUMBER, "");
        identity = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_IDENTITY, "");
        switch (identity) {
            case ConstantTools.ID_TEACHER:
                TableUserTeacher teacher =
                        (TableUserTeacher) dbHelper.findById(workNumber, TableUserTeacher.class);
                ownName = teacher == null ? "" : teacher.getName();
                break;
            case ConstantTools.ID_TEACHING_OFFICE:
                TableUserTeachingOffice office =
                        (TableUserTeachingOffice) dbHelper.findById(workNumber, TableUserTeachingOffice.class);
                ownName = office == null ? "" : office.getName();
                break;
            case ConstantTools.ID_DEPARTMENT_HEAD:
                TableUserDepartmentHead departmentHead =
                        (TableUserDepartmentHead) dbHelper.findById(workNumber, TableUserDepartmentHead.class);
                ownName = departmentHead == null ? "" : departmentHead.getName();
                break;
            default:
                break;
        }
        tvOwnName.setText(ownName);
        ownInformationSaveEditor = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).edit();
        ownInformationSaveEditor.putString(ConstantTools.USER_NAME, ownName);
        ownInformationSaveEditor.apply();
    }

    // 初始化数据，从数据库中获取当前页面所需的数据
    private void initTaskListData() {
        try {
            NetworkManager.getJsonString(ConstantTools.TABLE_TASK_INFO,
                    new NetworkManager.ResponseCallback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            //从服务器获取报课任务数据，并更新到本地数据库
                            JsonTools jsonTools = new JsonTools();
                            List list = jsonTools.getJsonList(response.body().string(), TableTaskInfo.class);
                            Log.w("jsonList", list.toString());

                            dbHelper.deleteAll(TableTaskInfo.class);
                            Log.i("TAG1", "getJsonString中taskListData是否为空" + (taskListData == null));
                            dbHelper.insertAll(list);
                            //从本地数据库获取报课任务数据
                            taskListData = dbHelper.findAll(TableTaskInfo.class);
                            Log.i("TAG1", "getJsonString中taskListData是否为空" + (taskListData == null));
                            TaskListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initTaskListView();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 显示数据，控件与数据绑定
    private void initTaskListView() {
        mTaskAdapter = new TaskAdapter(this, R.layout.list_item_task, taskListData);
        ListView mListView = (ListView) findViewById(R.id.lv_task_list);
        mListView.setAdapter(mTaskAdapter);
        mListView.setOnItemClickListener(this);
        Log.i("TAG","显示数据");
    }

    // 点击任务列表项跳转操作
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.d(TAG, String.valueOf(taskListData.get(position).getId()));
        Intent intent = new Intent(TaskListActivity.this, TaskDetailActivity.class);
        intent.putExtra("relativeTable", String.valueOf(taskListData.get(position).getRelativeTable()));
        startActivity(intent);
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
        identity = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_IDENTITY, null);
        getMenuInflater().inflate(R.menu.task_list_activity_actions, menu);
        if (!identity.equals(ConstantTools.ID_TEACHING_OFFICE)) {
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
                Log.i(TAG, "click add icon");
                startActivity(new Intent(TaskListActivity.this, TaskCreationActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
}
