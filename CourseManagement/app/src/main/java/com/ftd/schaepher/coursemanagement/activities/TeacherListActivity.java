package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.GlobalMap;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.widget.MoreListView;
import com.ftd.schaepher.coursemanagement.widget.RefreshableView;
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
        AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener,
        View.OnClickListener, AdapterView.OnItemLongClickListener {

    private static final String TAG = "TeacherListActivity";
    private static final int CLOSE_NAV = 1;

    private Toolbar mToolbar;
    private EditText eSearch;
    private Runnable eChanged = new Runnable() {
        @Override
        public void run() {
            // 搜索栏
            String data = eSearch.getText().toString();
        }
    };
    private RefreshableView refreshableView;
    private ImageView ivDeleteText;
    private TextView tvOwnName;
    private TextView tvDelete;
    private ListView teacherListView;
    private ListView departmentListView;
    private ListView officeListView;
    private PopupWindow popupWindow;
    private ProgressDialog mProgress;
    private boolean isSupportDoubleBackExit;
    private long doubleBackTime;
    private Handler myHandler = new Handler();
    private List<TableUserTeacher> teacherListData;
    private List<TableUserTeachingOffice> officeListData;
    private List<TableUserDepartmentHead> departmentListData;
    private TeacherAdapter teacherAdapter;
    private DepartmentHeadAdapter departmentAdapter;
    private TeacherOfficeAdapter officeAdapter;
    private String identity;
    private CourseDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        refreshableView = (RefreshableView) findViewById(R.id.refreshTeacher_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("教师列表");
        setSupportActionBar(mToolbar);
        dbHelper = new CourseDBHelper(TeacherListActivity.this);

        identity = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_IDENTITY, null);

        // 侧滑菜单
        setNavViewConfig();
        isSupportDoubleBackExit = true;
        mProgress = new ProgressDialog(TeacherListActivity.this);

        setSearchTextChanged(); // 设置eSearch搜索框的文本改变时监听器
        setIvDeleteTextOnClick(); // 设置叉叉的监听器
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserInformation();
        refreshTeacherListData();
        initTeacherListView();
        getServerTeacherData();
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

    // 初始化教师列表数据
    private void refreshTeacherListData() {
        //从本地数据库获取教师数据
        teacherListData = dbHelper.findAll(TableUserTeacher.class);
        officeListData = dbHelper.findAll(TableUserTeachingOffice.class);
        departmentListData = dbHelper.findAll(TableUserDepartmentHead.class);
    }


    // 初始化教师列表界面的控件
    private void initTeacherListView() {
        teacherListView = (MoreListView) findViewById(R.id.lv_teacher_list);
        teacherListView.setEmptyView(findViewById(R.id.tv_empty_teacher));
        departmentListView = (MoreListView) findViewById(R.id.lv_department_list);
        departmentListView.setEmptyView(findViewById(R.id.tv_empty_department));
        officeListView = (MoreListView) findViewById(R.id.lv_office_list);
        officeListView.setEmptyView(findViewById(R.id.tv_empty_office));

        if (teacherListData != null) {
            teacherAdapter = new TeacherAdapter(this, R.layout.list_item_teacher, teacherListData);
            teacherListView.setAdapter(teacherAdapter);
            teacherListView.setOnItemClickListener(this);
        }
        if (departmentListData != null) {
            departmentAdapter =
                    new DepartmentHeadAdapter(this, R.layout.list_item_teacher, departmentListData);
            departmentListView.setAdapter(departmentAdapter);
            departmentListView.setOnItemClickListener(this);
        }
        if (officeListData != null) {
            officeAdapter =
                    new TeacherOfficeAdapter(this, R.layout.list_item_teacher, officeListData);
            officeListView.setAdapter(officeAdapter);
            officeListView.setOnItemClickListener(this);
        }
        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            teacherListView.setOnItemLongClickListener(this);
            departmentListView.setOnItemLongClickListener(this);
            officeListView.setOnItemLongClickListener(this);
        }
    }

    private void getServerTeacherData() {
        try {
            NetworkManager.getJsonString(ConstantStr.TABLE_USER_TEACHER,
                    new NetworkManager.ResponseCallback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            //从服务器获取教师数据，并更新到本地数据库
                            List list = JsonTools.getJsonList(response.body().string(), TableUserTeacher.class);
                            Loger.w("jsonList", list.toString());
                            dbHelper.insertAll(list);

                            TeacherListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //从本地数据库获取教师数据
                                    teacherListData = dbHelper.findAll(TableUserTeacher.class);
                                    teacherAdapter.clear();
                                    teacherAdapter.addAll(teacherListData);
                                    teacherAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }
                    });

            NetworkManager.getJsonString(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                    new NetworkManager.ResponseCallback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            //从服务器获取教师数据，并更新到本地数据库
                            List list = JsonTools.getJsonList(response.body().string(), TableUserDepartmentHead.class);
                            Loger.w("jsonList", list.toString());
                            dbHelper.insertAll(list);

                            TeacherListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    departmentListData = dbHelper.findAll(TableUserDepartmentHead.class);
                                    departmentAdapter.clear();
                                    departmentAdapter.addAll(departmentListData);
                                    departmentAdapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, IOException e) {

                        }
                    });

            NetworkManager.getJsonString(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                    new NetworkManager.ResponseCallback() {
                        @Override
                        public void onResponse(Response response) throws IOException {
                            //从服务器获取教师数据，并更新到本地数据库
                            List list = JsonTools.getJsonList(response.body().string(), TableUserTeachingOffice.class);
                            Loger.w("jsonList", list.toString());
                            dbHelper.insertAll(list);

                            TeacherListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    officeListData = dbHelper.findAll(TableUserTeachingOffice.class);
                                    officeAdapter.clear();
                                    officeAdapter.addAll(officeListData);
                                    officeAdapter.notifyDataSetChanged();
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

    private void initUserInformation() {
        String ownName = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_NAME, "");
        tvOwnName.setText(ownName);
    }

    //添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            getMenuInflater().inflate(R.menu.teacher_list_activity_actions, menu);
            MenuItem addTeacherItem = menu.findItem(R.id.action_add_teacher);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_input)
                    .setOnMenuItemClickListener(this);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_file)
                    .setOnMenuItemClickListener(this);
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
        String queryWorkNumber;
        String queryIdentity;
        switch (parent.getId()) {
            case R.id.lv_office_list:
                Loger.i("parent", "教学办");
                queryWorkNumber = officeListData.get(position).getWorkNumber();
                queryIdentity = ConstantStr.ID_TEACHING_OFFICE;
                break;
            case R.id.lv_department_list:
                Loger.i("parent", "系负责人");
                queryWorkNumber = departmentListData.get(position).getWorkNumber();
                queryIdentity = ConstantStr.ID_DEPARTMENT_HEAD;
                break;
            case R.id.lv_teacher_list:
                queryWorkNumber = teacherListData.get(position).getWorkNumber();
                queryIdentity = ConstantStr.ID_TEACHER;
                Loger.i("parent", "教师");
                break;
            default:
                Loger.i("parent", "无" + parent.getCount());
                queryWorkNumber = "";
                queryIdentity = "";
                break;
        }
        Intent intend = new Intent();
        intend.setClass(TeacherListActivity.this, TeacherDetailActivity.class);
        intend.putExtra("teacherID", queryWorkNumber);
        intend.putExtra("teacherIdentity", queryIdentity);
        intend.putExtra("isQueryingSelf", false);
        startActivity(intend);

        Loger.i("str", String.valueOf(parent));
        Loger.i("str", position + "    " + id);
        Loger.i("str", queryWorkNumber + "  " + queryIdentity);
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
            if ((System.currentTimeMillis() - doubleBackTime) > 2000) {
                Toast.makeText(TeacherListActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
                doubleBackTime = System.currentTimeMillis();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_popup_delete:
                popupWindow.dismiss();
                mProgress.setMessage("删除用户中...");
                mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgress.setCancelable(false);
                mProgress.show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Bundle bundle = (Bundle) tvDelete.getTag();
                            String tableName = bundle.getString("tableName");
                            String workNumber = bundle.getString("workNumber");
                            NetworkManager.deleteServerUser(tableName, workNumber,
                                    new MyRespons(tableName, workNumber));
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

    class MyRespons implements NetworkManager.ResponseCallback {
        private String tableName;
        private String workNumber;

        public MyRespons(String tableName, String workNumber) {
            super();
            this.tableName = tableName;
            this.workNumber = workNumber;
        }

        @Override
        public void onResponse(Response response) throws IOException {
            String result = response.body().string();
            if (result.equals("没有权限")) {
                sendToast("没有权限");

            } else {
                clossProcess();
                sendToast("删除成功！");
                try {
                    Loger.d("delete", "tableName:" + tableName + "|||workNumber:" + workNumber);
                    dbHelper.deleteByID(Class.forName(GlobalMap.get(tableName)), workNumber);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                refreshListView();
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {

        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (refreshableView.getCurrentStatus() == RefreshableView.STATUS_REFRESH_FINISHED) {
            View v = this.getLayoutInflater().inflate(R.layout.popup_dialog_delete, null);
            popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.showAsDropDown(view, ((getWindowManager()
                    .getDefaultDisplay().getWidth() / 3)), -(3 * view.getHeight() / 2));

            Bundle bundle = new Bundle();
            switch (parent.getId()) {
                case R.id.lv_office_list:
                    bundle.putString("tableName", ConstantStr.ID_TEACHING_OFFICE);
                    bundle.putString("workNumber", officeListData.get(position).getWorkNumber());
                    break;
                case R.id.lv_department_list:
                    bundle.putString("tableName", ConstantStr.ID_DEPARTMENT_HEAD);
                    bundle.putString("workNumber", departmentListData.get(position).getWorkNumber());
                    break;
                case R.id.lv_teacher_list:
                    bundle.putString("tableName", ConstantStr.ID_TEACHER);
                    bundle.putString("workNumber", teacherListData.get(position).getWorkNumber());
                    break;
                default:
                    break;
            }
            tvDelete = (TextView) v.findViewById(R.id.tv_popup_delete);
            tvDelete.setOnClickListener(this);
            tvDelete.setTag(bundle);
        }
        return true;
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

    public void sendToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TeacherListActivity.this, message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void refreshListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                officeAdapter.notifyDataSetChanged();
                departmentAdapter.notifyDataSetChanged();
                teacherAdapter.notifyDataSetChanged();
            }
        });
    }
}