package com.ftd.schaepher.coursemanagement.activities;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.adapter.DepartmentHeadAdapter;
import com.ftd.schaepher.coursemanagement.adapter.TeacherAdapter;
import com.ftd.schaepher.coursemanagement.adapter.TeacherOfficeAdapter;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantTools;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.widget.MoreListView;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private List<TableUserTeachingOffice> officeListData;
    private List<TableUserDepartmentHead> departmentListData;
    private String identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("教师列表");
        setSupportActionBar(mToolbar);

        identity = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantTools.USER_IDENTITY, null);

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
        initTeacherListData();
        initUserInformation();
    }

    // 初始化教师列表数据
    private void initTeacherListData() {
        try {
            NetworkManager.getJsonString(ConstantTools.TABLE_USER_TEACHER,
                    new NetworkManager.ResponseCallback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            //从服务器获取教师数据，并更新到本地数据库
                            CourseDBHelper dbHelper = new CourseDBHelper(TeacherListActivity.this);
                            JsonTools jsonTools = new JsonTools();
                            List list = jsonTools.getJsonList(response.body().string(), TableUserTeacher.class);
                            Log.w("jsonList", list.toString());

                            dbHelper.deleteAll(TableUserTeacher.class);
                            dbHelper.insertAll(list);
                            //从本地数据库获取教师数据
                            teacherListData = dbHelper.findAll(TableUserTeacher.class);
                            officeListData = dbHelper.findAll(TableUserTeachingOffice.class);
                            departmentListData = dbHelper.findAll(TableUserDepartmentHead.class);

                            TeacherListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initTeacherListView();
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


    // 初始化教师列表界面的控件
    private void initTeacherListView() {
        if (teacherListData != null) {
            TeacherAdapter mTeacherAdapter = new TeacherAdapter(this, R.layout.list_item_teacher, teacherListData);
            ListView mListView = (MoreListView) findViewById(R.id.lv_teacher_list);
            mListView.setAdapter(mTeacherAdapter);
            mListView.setOnItemClickListener(this);
        }
        if (departmentListData != null) {
            DepartmentHeadAdapter departmentAdapter = new DepartmentHeadAdapter(this, R.layout.list_item_teacher, departmentListData);
            ListView departmentListView = (MoreListView) findViewById(R.id.lv_department_list);
            departmentListView.setAdapter(departmentAdapter);
            departmentListView.setOnItemClickListener(this);
        }
        if (officeListData != null) {
            TeacherOfficeAdapter officeAdapter = new TeacherOfficeAdapter(this, R.layout.list_item_teacher, officeListData);
            ListView officeListView = (MoreListView) findViewById(R.id.lv_office_list);
            officeListView.setAdapter(officeAdapter);
            officeListView.setOnItemClickListener(this);
        }
    }

    private void initUserInformation() {
        String ownName = getSharedPreferences(ConstantTools.USER_INFORMATION, MODE_PRIVATE).getString(ConstantTools.USER_NAME, "");
        tvOwnName.setText(ownName);
    }

    //添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (identity.equals(ConstantTools.ID_TEACHING_OFFICE)) {
            getMenuInflater().inflate(R.menu.teacher_list_activity_actions, menu);
            MenuItem addTeacherItem = menu.findItem(R.id.action_add_teacher);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_input).setOnMenuItemClickListener(this);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_file).setOnMenuItemClickListener(this);
        }
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
        String queryWorkNumber;
        String queryIdentity;
        switch (parent.getId()) {
            case R.id.lv_office_list:
                Log.i("parent", "教学办");
                queryWorkNumber = officeListData.get(position).getWorkNumber();
                queryIdentity = ConstantTools.ID_TEACHING_OFFICE;
                break;
            case R.id.lv_department_list:
                Log.i("parent", "系负责人");
                queryWorkNumber = departmentListData.get(position).getWorkNumber();
                queryIdentity = ConstantTools.ID_DEPARTMENT_HEAD;
                break;
            case R.id.lv_teacher_list:
                queryWorkNumber = teacherListData.get(position).getWorkNumber();
                queryIdentity = ConstantTools.ID_TEACHER;
                Log.i("parent", "教师");
                break;
            default:
                Log.i("parent", "无" + parent.getCount());
                queryWorkNumber = "";
                queryIdentity = "";
                break;
        }
        intend.putExtra("teacherID", queryWorkNumber);
        intend.putExtra("teacherIdentity", queryIdentity);
        intend.putExtra("isQueryOwnInfomation", false);
        startActivity(intend);

        Log.i("str", String.valueOf(parent));
        Log.i("str", position + "    " + id);
        Log.i("str", queryWorkNumber + "  " + queryIdentity);
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
}