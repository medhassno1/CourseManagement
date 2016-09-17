package com.ftd.schaepher.coursemanagement.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.adapter.DepartmentHeadAdapter;
import com.ftd.schaepher.coursemanagement.adapter.TeacherAdapter;
import com.ftd.schaepher.coursemanagement.adapter.TeacherOfficeAdapter;
import com.ftd.schaepher.coursemanagement.db.CourseDBHelper;
import com.ftd.schaepher.coursemanagement.pojo.TableManageMajor;
import com.ftd.schaepher.coursemanagement.pojo.TableUserDepartmentHead;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;
import com.ftd.schaepher.coursemanagement.tools.ConstantStr;
import com.ftd.schaepher.coursemanagement.tools.GlobalMap;
import com.ftd.schaepher.coursemanagement.tools.JsonTools;
import com.ftd.schaepher.coursemanagement.tools.Loger;
import com.ftd.schaepher.coursemanagement.tools.NetworkManager;
import com.ftd.schaepher.coursemanagement.widget.MoreListView;
import com.j256.ormlite.dao.Dao;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
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

    private TextView tvOwnName;
    private TextView tvDelete;
    private ListView teacherListView;
    private ListView departmentListView;
    private ListView officeListView;
    private PopupWindow popupWindow;
    private ProgressDialog mProgress;
    private boolean isSupportDoubleBackExit;
    private long doubleBackTime;
    private List<TableUserTeacher> teacherListData;
    private List<TableUserTeachingOffice> officeListData;
    private List<TableUserDepartmentHead> departmentListData;
    private TeacherAdapter teacherAdapter;
    private DepartmentHeadAdapter departmentAdapter;
    private TeacherOfficeAdapter officeAdapter;
    private String identity;
    private CourseDBHelper dbHelper;
    private Dao<TableUserTeacher, String> teacherDao = null;
    private Dao<TableUserDepartmentHead, String> departmentHeadDao = null;
    private Dao<TableUserTeachingOffice, String> officeDao = null;
    private Dao<TableManageMajor, Integer> manageMajorDao = null;

    private TextView tvListEmptyTeacher;
    private TextView tvListEmptyDepartment;
    private TextView tvListEmptyOffice;

    private int finishFromServer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("用户列表");
        setSupportActionBar(mToolbar);
        dbHelper = CourseDBHelper.getInstance(this);

        identity = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_IDENTITY, null);

        // 侧滑菜单
        setNavViewConfig();
        isSupportDoubleBackExit = true;
        mProgress = new ProgressDialog(TeacherListActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(true);

//        setSearchTextChanged();
        try {
            teacherDao = dbHelper.getTeacherDao();
            departmentHeadDao = dbHelper.getDepartmentHeadDao();
            officeDao = dbHelper.getOfficeDao();
            manageMajorDao = dbHelper.getManageMajorDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initTeacherListView();

        Loger.i("jsonList1", "开始执行onCreate()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (teacherListData == null && departmentListData == null && officeListData == null) {
            Loger.d("onResume", "true");
            refreshAllData();
        } else {
            SharedPreferences preferences = getSharedPreferences("TableUpdateTime", MODE_PRIVATE);
            long time = preferences.getLong("UserTable", 0);
            long fiveHours = 5 * 3600 * 1000;
            if (time == 0 || Calendar.getInstance().getTimeInMillis() - time > fiveHours) {
                refreshAllData();
            }
        }

        initThisUserInformation();
    }

    private void refreshTeacherData() {

    }

    private void refreshDepartmentData() {

    }

    private void refreshOfficeData() {

    }

    private void refreshAllData() {
        try {
            departmentListData = departmentHeadDao.queryForAll();
            officeListData = officeDao.queryForAll();
            teacherListData = teacherDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (teacherAdapter != null) {
            teacherAdapter.clear();
            teacherAdapter.addAll(teacherListData);
            teacherAdapter.notifyDataSetChanged();
        } else if (teacherListData != null) {
            teacherAdapter = new TeacherAdapter(this, R.layout.list_item_teacher, teacherListData);
            teacherListView.setAdapter(teacherAdapter);
            teacherListView.setOnItemClickListener(this);
        }

        if (departmentAdapter != null) {
            departmentAdapter.clear();
            departmentAdapter.addAll(departmentListData);
            departmentAdapter.notifyDataSetChanged();
        } else if (departmentListData != null) {
            departmentAdapter =
                    new DepartmentHeadAdapter(this, R.layout.list_item_teacher, departmentListData);
            departmentListView.setAdapter(departmentAdapter);
            departmentListView.setOnItemClickListener(this);
        }

        if (officeAdapter != null) {
            officeAdapter.clear();
            officeAdapter.addAll(officeListData);
            officeAdapter.notifyDataSetChanged();
        } else if (officeListData != null) {
            officeAdapter =
                    new TeacherOfficeAdapter(this, R.layout.list_item_teacher, officeListData);
            officeListView.setAdapter(officeAdapter);
            officeListView.setOnItemClickListener(this);
        }

    }

    private void initThisUserInformation() {
        String ownName = getSharedPreferences(ConstantStr.USER_INFORMATION, MODE_PRIVATE)
                .getString(ConstantStr.USER_NAME, "");
        tvOwnName.setText(ownName);
    }

    // 初始化教师列表界面的控件
    private void initTeacherListView() {
        tvListEmptyTeacher = (TextView) findViewById(R.id.tv_empty_teacher);
        teacherListView = (MoreListView) findViewById(R.id.lv_teacher_list);
        teacherListView.setEmptyView(tvListEmptyTeacher);

        tvListEmptyDepartment = (TextView) findViewById(R.id.tv_empty_department);
        departmentListView = (MoreListView) findViewById(R.id.lv_department_list);
        departmentListView.setEmptyView(tvListEmptyDepartment);

        tvListEmptyOffice = (TextView) findViewById(R.id.tv_empty_office);
        officeListView = (MoreListView) findViewById(R.id.lv_office_list);
        officeListView.setEmptyView(tvListEmptyOffice);

        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE)) {
            teacherListView.setOnItemLongClickListener(this);
            departmentListView.setOnItemLongClickListener(this);
            officeListView.setOnItemLongClickListener(this);
        }

    }

    private void getServerUserData() throws IOException {
        mProgress.setMessage("更新用户列表中...");
        mProgress.show();

        Loger.i("jsonList1", "开始执行getServerTeacherData()");
        getServerTeacherData();
        getServerDepartmentData();
        getServerOfficeData();

        SharedPreferences.Editor editor = getSharedPreferences("TableUpdateTime", MODE_PRIVATE).edit();
        editor.putLong("UserTable", Calendar.getInstance().getTimeInMillis());
        editor.apply();

    }

    private void getServerTeacherData() throws IOException {
        NetworkManager.getJsonString(ConstantStr.TABLE_USER_TEACHER,
                new NetworkManager.ResponseCallback() {
                    @Override
                    public void onResponse(Response response) throws IOException {
                        //从服务器获取教师数据，并更新到本地数据库
                        List list = null;
                        try {
                            list = JsonTools.getJsonList(response.body().string(), TableUserTeacher.class);
                            Loger.w("jsonList1", list.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            teacherDao.deleteBuilder().delete();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (list != null) {
                            try {
                                teacherDao.create(list);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvListEmptyTeacher.setText("该用户列表为空");
                                }
                            });
                        }

                        TeacherListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //从本地数据库获取教师数据
                                refreshAllData();
                                closeProgressIfAllFinished();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                });
    }

    private void getServerDepartmentData() throws IOException {
        NetworkManager.getJsonString(ConstantStr.TABLE_USER_DEPARTMENT_HEAD,
                new NetworkManager.ResponseCallback() {
                    @Override
                    public void onResponse(Response response) throws IOException {
                        //从服务器获取系负责人数据，并更新到本地数据库
                        List list = null;
                        try {
                            list = JsonTools.getJsonList(response.body().string(), TableUserDepartmentHead.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            departmentHeadDao.deleteBuilder().delete();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        if (list != null) {
                            Loger.w("jsonList1", list.toString());
                            try {
                                departmentHeadDao.create(list);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvListEmptyDepartment.setText("该用户列表为空");
                                }
                            });
                        }


                        TeacherListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshAllData();
                                closeProgressIfAllFinished();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                });

        NetworkManager.getJsonString(ConstantStr.TABLE_MANAGE_MAJOR,
                new NetworkManager.ResponseCallback() {
                    @Override
                    public void onResponse(Response response) throws IOException {
                        //从服务器获取系负责人专业数据，并更新到本地数据库
                        List list = null;
                        try {
                            list = JsonTools.getJsonList(response.body().string(), TableManageMajor.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            manageMajorDao.deleteBuilder().delete();

                            if (list != null) {
                                Loger.i("jsonList1", list.toString());
                                manageMajorDao.create(list);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                });
    }

    private void getServerOfficeData() throws IOException {
        NetworkManager.getJsonString(ConstantStr.TABLE_USER_TEACHING_OFFICE,
                new NetworkManager.ResponseCallback() {
                    @Override
                    public void onResponse(Response response) throws IOException {
                        //从服务器获取教学办数据，并更新到本地数据库
                        List list = null;
                        try {
                            list = JsonTools.getJsonList(response.body().string(), TableUserTeachingOffice.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            officeDao.deleteBuilder().delete();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if (list != null) {
                            Loger.w("jsonList1", list.toString());
                            try {
                                officeDao.create(list);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvListEmptyOffice.setText("该用户列表为空");
                                }
                            });
                        }

                        TeacherListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshAllData();
                                closeProgressIfAllFinished();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Request request, IOException e) {

                    }
                });
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

    //添加标题栏上的按钮图标
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (identity.equals(ConstantStr.ID_TEACHING_OFFICE) || identity.equals(ConstantStr.ID_DEPARTMENT_HEAD)) {
            getMenuInflater().inflate(R.menu.teacher_list_activity_actions, menu);
            MenuItem addTeacherItem = menu.findItem(R.id.action_add_teacher);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_input)
                    .setOnMenuItemClickListener(this);
            addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_file)
                    .setOnMenuItemClickListener(this);

            menu.findItem(R.id.action_refresh).setOnMenuItemClickListener(this);
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
                Loger.i("parent", "位置是：" + position);
                // Loger.i("parent", "教学办"+dbHelper.findAll(TableUserTeachingOffice.class));
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
        startActivityForResult(intend, 0);

        Loger.i("parent", String.valueOf(parent));
        Loger.i("parent", position + "    " + id);
        Loger.i("parent", queryWorkNumber + "  " + queryIdentity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == 1) {
                refreshAllData();
            }
        }
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
            case R.id.action_refresh:
                try {
                    getServerUserData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        return true;
    }

    /**
     * 设置搜索框的文本更改时的监听器
     */
    private void setSearchTextChanged() {
        EditText eSearch = (EditText) findViewById(R.id.etSearch);

        eSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                mProgress.show();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Bundle bundle = (Bundle) tvDelete.getTag();
                            String tableName = bundle.getString("tableName");
                            String workNumber = bundle.getString("workNumber");
                            int position = bundle.getInt("position");
                            NetworkManager.deleteServerUser(tableName, workNumber,
                                    new MyResponse(tableName, workNumber, position));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Loger.d("delete", "程序崩溃");
                            closeProgress();
                        }
                    }
                }.start();
                break;
            default:
                break;
        }
    }

    class MyResponse implements NetworkManager.ResponseCallback {
        private String tableName;
        private String workNumber;
        private int position;

        public MyResponse(String tableName, String workNumber, int position) {
            super();
            this.tableName = tableName;
            this.workNumber = workNumber;
            this.position = position;
        }

        @Override
        public void onResponse(Response response) throws IOException {
            String result = response.body().string();
//            Loger.w("re", result);
            if (result.equals("true")) {
                sendToast("删除成功！");
                try {
                    Loger.d("delete", "tableName:" + tableName + "|||workNumber:" + workNumber);
                    Class clazz = Class.forName(GlobalMap.get(tableName));

                    switch (tableName) {
                        case ConstantStr.ID_TEACHER:
                            teacherDao.deleteById(workNumber);
                            break;
                        case ConstantStr.ID_DEPARTMENT_HEAD:
                            departmentHeadDao.deleteById(workNumber);
                            manageMajorDao.deleteBuilder().where().eq("workNumber", workNumber);
                            break;
                        case ConstantStr.ID_TEACHING_OFFICE:
                            officeDao.deleteById(workNumber);
                            break;
                        default:
                            break;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (tableName) {
                                case ConstantStr.TABLE_USER_TEACHER:
                                    teacherAdapter.remove(teacherListData.get(position));
                                    teacherAdapter.notifyDataSetChanged();
                                    break;
                                case ConstantStr.TABLE_USER_DEPARTMENT_HEAD:
                                    departmentAdapter.remove(departmentListData.get(position));
                                    departmentAdapter.notifyDataSetChanged();
                                    break;
                                case ConstantStr.TABLE_USER_TEACHING_OFFICE:
                                    officeAdapter.remove(officeListData.get(position));
                                    officeAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    });

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                sendToast("删除失败");
            }
            closeProgress();
        }

        @Override
        public void onFailure(Request request, IOException e) {

        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
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
                bundle.putInt("position", position);
                break;
            case R.id.lv_department_list:
                bundle.putString("tableName", ConstantStr.ID_DEPARTMENT_HEAD);
                bundle.putString("workNumber", departmentListData.get(position).getWorkNumber());
                bundle.putInt("position", position);
                break;
            case R.id.lv_teacher_list:
                bundle.putString("tableName", ConstantStr.ID_TEACHER);
                bundle.putString("workNumber", teacherListData.get(position).getWorkNumber());
                bundle.putInt("position", position);
                break;
            default:
                break;
        }
        tvDelete = (TextView) v.findViewById(R.id.tv_popup_delete);
        tvDelete.setOnClickListener(this);
        tvDelete.setTag(bundle);
        return true;
    }

    private void closeProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgress.isShowing()) {
                    mProgress.cancel();
                }
            }
        });
    }

    public void closeProgressIfAllFinished() {
        finishFromServer++;
        if (finishFromServer == 3) {
            closeProgress();
        }
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
}