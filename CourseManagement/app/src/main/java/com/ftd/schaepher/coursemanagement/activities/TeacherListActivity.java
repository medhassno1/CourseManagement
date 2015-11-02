package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import com.ftd.schaepher.coursemanagement.pojo.Teacher;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sxq on 2015/11/2.
 * 教师列表界面
 */
public class TeacherListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {
    private List<Teacher> teacherListData;
    private Toolbar mToolbar;
    private boolean isSupportDoubleBackExit;
    private long betweenDoubleBackTime;
    private static final String TAG = "TeacherListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("教师列表");
        setSupportActionBar(mToolbar);


        setNavViewConfig();
        setSupportDoubleBackExit(true);

        initTeacherListData();
        initTeacherListView();
    }

    private void setNavViewConfig() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teacher_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_teacher_list);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initTeacherListData() {
        teacherListData = new ArrayList<Teacher>();
        Teacher teacher = new Teacher(R.drawable.ic_people,"张三");
        teacherListData.add(teacher);
    }

    private void initTeacherListView() {
        TeacherAdapter mTeacherAdapter = new TeacherAdapter(this,R.layout.list_item_teacher, teacherListData);
        ListView mListView = (ListView) findViewById(R.id.lv_teacher_list);
        mListView.setAdapter(mTeacherAdapter);
        mListView.setOnItemClickListener(this);
    }

    //左菜单点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_teacher_list:
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout_teacher_list);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_task_list:
                finish();
                startActivity( new Intent(TeacherListActivity.this,TaskListActivity.class));
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teacher_list);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isSupportDoubleBackExit) {
            if ((System.currentTimeMillis() - betweenDoubleBackTime) > 2000) {
                Toast.makeText(TeacherListActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.teacher_list_activity_actions, menu);

        return true;
    }
    //标题栏上的按钮图标点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_teacher:
                Log.i(TAG, "click add icon");
                startActivity(new Intent(TeacherListActivity.this, TeacherCreationActivity.class));
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //点击查看教师信息跳转逻辑
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(new Intent(TeacherListActivity.this,TeacherDetailActivity.class));
    }

    /**
     * 任务列表的适配器
     */
    class TeacherAdapter extends ArrayAdapter<Teacher> {
        private int resourceId;

        public TeacherAdapter(Context context, int resource, List<Teacher> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Teacher teacher = getItem(position);
            View view;
            viewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new viewHolder();
                viewHolder.teacherImageId = (CircleImageView) view.findViewById(R.id.circle_img_teacher);
                viewHolder.teacherName = (TextView) view.findViewById(R.id.tv_teacher_name);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (TeacherAdapter.viewHolder) view.getTag();
            }

            viewHolder.teacherImageId.setImageResource(teacher.getTeacherImageId());
            viewHolder.teacherName.setText(teacher.getTeacherName());
            return view;
        }

        class viewHolder {
            CircleImageView teacherImageId;
            TextView teacherName;
        }
    }
}
