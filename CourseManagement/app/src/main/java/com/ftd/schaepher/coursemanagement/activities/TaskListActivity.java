package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.pojo.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/31.
 * 教学办登录默认主界面---任务主界面
 */
public class TaskListActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {
    private List<Task> taskListData;
    private Toolbar mToolbar;

    private String identity;
    private boolean isSupportDoubleBackExit;
    private long betweenDoubleBackTime;
    private static final String TAG = "TaskListActivity";

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

        initTaskListData();
        initTaskListView();
    }

    private void setNavViewConfig() {
        identity = getSharedPreferences("userInformation",MODE_PRIVATE).getString("identity", null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_base);

        if (identity.equals("teacher")){
            navigationView.getMenu().removeItem(R.id.nav_teacher_list);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    //初始化数据，从数据库中获取当前页面所需的数据
    private void initTaskListData() {
        taskListData = new ArrayList<Task>();
        Task task = new Task("进行中", "计算机专业");
        taskListData.add(task);
    }

    //显示数据，控件与数据绑定
    private void initTaskListView() {
        TaskAdapter mTaskAdapter = new TaskAdapter(this, R.layout.list_item_task, taskListData);
        ListView mListView = (ListView) findViewById(R.id.lv_task_list);
        mListView.setAdapter(mTaskAdapter);
        mListView.setOnItemClickListener(this);
    }

    //点击任务列表项跳转操作
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(TaskListActivity.this, TaskDetailActivity.class));
    }

    //左菜单点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_task_list:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_base);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_teacher_list:
                startActivity(new Intent(TaskListActivity.this, TeacherListActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(TaskListActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.nav_own_information:
                startActivity(new Intent(TaskListActivity.this,TeacherDetailActivity.class));
                onBackPressed();
                break;
            default:
                break;
        }
        return false;
    }

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

    public void setSupportDoubleBackExit(boolean isDoubleBackExit) {
        this.isSupportDoubleBackExit = isDoubleBackExit;
    }

    //添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        identity = getSharedPreferences("userInformation",MODE_PRIVATE).getString("identity", null);
        getMenuInflater().inflate(R.menu.task_list_activity_actions, menu);
        if (identity.equals("teacher")){
            menu.removeItem(R.id.action_add_task);
        }
        return true;
    }

    //标题栏上的按钮图标点击事件
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

    /**
     * 任务列表的适配器
     */
    class TaskAdapter extends ArrayAdapter<Task> {
        private int resourceId;

        public TaskAdapter(Context context, int resource, List<Task> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Task task = getItem(position);
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

            viewHolder.taskState.setText(task.getTaskState());
            viewHolder.taskName.setText(task.getTaskName());
            return view;
        }

        class viewHolder {
            TextView taskState;
            TextView taskName;
        }
    }
}
