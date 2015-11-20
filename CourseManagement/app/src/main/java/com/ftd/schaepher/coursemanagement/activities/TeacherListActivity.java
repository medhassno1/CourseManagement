package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sxq on 2015/11/2.
 * 教师列表界面
 */
public class TeacherListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {

    private static final String TAG = "TeacherListActivity";
    private static final int CLOSE_NAV = 1;

    private Toolbar mToolbar;
    private EditText eSearch;
    Runnable eChanged = new Runnable() {
        @Override
        public void run() {
            String data = eSearch.getText().toString();

        }
    };
    private ImageView ivDeleteText;
    private TextView tvOwnName;
    private boolean isSupportDoubleBackExit;
    private long betweenDoubleBackTime;
    private Handler myHandler = new Handler();
    private List<TableUserTeacher> teacherListData;
    private List<TableUserTeacher> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("教师列表");
        setSupportActionBar(mToolbar);

        // 侧滑菜单
        setNavViewConfig();
        setSupportDoubleBackExit(true);

        setSearchTextChanged(); // 设置eSearch搜索框的文本改变时监听器
        setIvDeleteTextOnClick(); // 设置叉叉的监听器
    }

    // 左滑菜单初始配置
    private void setNavViewConfig() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teacher_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_teacher_list);
        tvOwnName = (TextView) navigationView.inflateHeaderView(R.layout.nav_header_base)
                .findViewById(R.id.nav_own_name);
        navigationView.getMenu().findItem(R.id.nav_teacher_list).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setSupportDoubleBackExit(boolean isDoubleBackExit) {
        this.isSupportDoubleBackExit = isDoubleBackExit;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTeacherDataList();
        initUserInformation();

    }

    public void updateTeacherDataList() {
        initTeacherListData();
        initTeacherListView();
    }

    // 初始化教师列表数据
    private void initTeacherListData() {
        teacherListData = new ArrayList<>();
        CourseDBHelper dbHelper = new CourseDBHelper();
        dbHelper.createDataBase(this);

        list = dbHelper.findAll(TableUserTeacher.class);
        Log.i("string", list.size() + "");

        for (int i = 0; i < list.size(); i++) {
            teacherListData.add(list.get(i));
            Log.i("string", list.get(i).getName());
        }

    }

    // 初始化教师列表界面的控件
    private void initTeacherListView() {
        TeacherAdapter mTeacherAdapter = new TeacherAdapter(this, R.layout.list_item_teacher, teacherListData);
        ListView mListView = (ListView) findViewById(R.id.lv_teacher_list);
        mListView.setAdapter(mTeacherAdapter);
        mListView.setOnItemClickListener(this);
    }

    private void initUserInformation() {
        String ownName = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_NAME, "");
        tvOwnName.setText(ownName);
    }

    //添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_list_activity_actions, menu);
        MenuItem addTeacherItem = menu.findItem(R.id.action_add_teacher);
        addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_input).setOnMenuItemClickListener(this);
        addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_file).setOnMenuItemClickListener(this);
        return true;
    }

    // 左菜单点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_teacher_list:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teacher_list);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_task_list:
                startActivity(new Intent(TeacherListActivity.this, TaskListActivity.class));
                item.setChecked(true);
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(TeacherListActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_own_information:
                startActivity(new Intent(TeacherListActivity.this, TeacherDetailActivity.class));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = CLOSE_NAV;
                        mHandler.sendMessage(msg);
                    }
                }, 200);
                break;
            default:
                break;
        }
        return false;
    }

    // 点击查看教师信息跳转逻辑
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intend = new Intent();
        intend.setClass(TeacherListActivity.this, TeacherDetailActivity.class);
        intend.putExtra("teacherID", list.get(position).getWorkNumber());
        intend.putExtra("isQueryOwnInfomation", false);
        startActivity(intend);

        Log.i("str", position + "    " + id);
        Log.i("str", list.get(position).getWorkNumber() + list.get(position).getName());
    }

    // 点击标题栏的子菜单事件
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_teacher_from_input:
                startActivity(new Intent(TeacherListActivity.this, TeacherCreationActivity.class));
                break;
            case R.id.add_teacher_from_file:
                Intent intent = new Intent(TeacherListActivity.this, FileSelectActivity.class);
                intent.putExtra("isRequireImportTeacherFile", true);
                startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void setSearchTextChanged() {
        eSearch = (EditText) findViewById(R.id.etSearch);

        eSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                //这是文本框改变之前会执行的动作
            }

            /**这是文本框改变之后 会执行的动作
             * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，
             * 并且如一的显示在界面上。所以这里我们就需要加上数据的修改的动作了。
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE); // 当文本框为空时，则叉叉消失
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE); // 当文本框不为空时，出现叉叉
                }

                myHandler.post(eChanged);
            }
        });

    }

    /**
     * 设置叉叉的点击事件，即清空功能
     */
    private void setIvDeleteTextOnClick() {
        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                eSearch.setText("");
            }
        });
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CLOSE_NAV:
                    onBackPressed();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    /**
     * 任务列表的适配器
     */
    class TeacherAdapter extends ArrayAdapter<TableUserTeacher> {
        private int resourceId;

        public TeacherAdapter(Context context, int resource, List<TableUserTeacher> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TableUserTeacher teacher = getItem(position);
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

            viewHolder.teacherImageId.setImageResource(R.drawable.ic_people);
            viewHolder.teacherName.setText(teacher.getName());
            return view;
        }

        class viewHolder {
            CircleImageView teacherImageId;
            TextView teacherName;
        }
    }

}