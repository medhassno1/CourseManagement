package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.ftd.schaepher.coursemanagement.pojo.TableTeacher;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sxq on 2015/11/2.
 * 教师列表界面
 */
public class TeacherListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AdapterView.OnItemClickListener, MenuItem.OnMenuItemClickListener {
    private Toolbar mToolbar;
    private boolean isSupportDoubleBackExit;
    private long betweenDoubleBackTime;
    private EditText eSearch;
    private ImageView ivDeleteText;

    private Handler myhandler = new Handler();
    private static final String TAG = "TeacherListActivity";
    private String workNumber;

    private List<TableTeacher> teacherListData;//
    private List<TableTeacher> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_teacher_list);
        mToolbar.setTitle("教师列表");
        setSupportActionBar(mToolbar);

        setNavViewConfig();
        setSupportDoubleBackExit(true);

        updateTeacherDataList();

        Intent intent = getIntent();
        workNumber = intent.getStringExtra("teacherID");

        setSearchTextChanged();//设置eSearch搜索框的文本改变时监听器
        setIvDeleteTextOnClick();//设置叉叉的监听器
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTeacherDataList();
    }

    public void updateTeacherDataList(){
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
        teacherListData = new ArrayList<>();
        CourseDBHelper dbHelper = new CourseDBHelper();
        dbHelper.creatDataBase(this);

        list = dbHelper.findall(TableTeacher.class);
        Log.i("string", list.size() + "");

        for(int i=0;i<list.size();i++){
            teacherListData.add(list.get(i));
            Log.i("string",list.get(i).getName());
        }

    }

    private void initTeacherListView() {
        TeacherAdapter mTeacherAdapter = new TeacherAdapter(this, R.layout.list_item_teacher, teacherListData);
        ListView mListView = (ListView) findViewById(R.id.lv_teacher_list);
        mListView.setAdapter(mTeacherAdapter);
        mListView.setOnItemClickListener(this);
    }

    //左菜单点击事件
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_teacher_list:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_teacher_list);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_task_list:
                startActivity(new Intent(TeacherListActivity.this, TaskListActivity.class));
                finish();
                break;
            case R.id.nav_logout:
                startActivity(new Intent(TeacherListActivity.this,LoginActivity.class));
                finish();
                break;
            case R.id.nav_own_information:
                Intent intend = new Intent();
                intend.setClass(TeacherListActivity.this, TeacherDetailActivity.class);
                intend.putExtra("teacherID",workNumber);
                startActivity(intend);
                onBackPressed();
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
        MenuItem addTeacherItem = menu.findItem(R.id.action_add_teacher);
        addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_input).setOnMenuItemClickListener(this);
        addTeacherItem.getSubMenu().findItem(R.id.add_teacher_from_file).setOnMenuItemClickListener(this);
        return true;
    }

    //点击查看教师信息跳转逻辑
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intend = new Intent();
        intend.setClass(TeacherListActivity.this, TeacherDetailActivity.class);
        intend.putExtra("teacherID", list.get(position).getWorkNumber());
        startActivity(intend);

        Log.i("str", position + "    " + id);
        Log.i("str",list.get(position).getWorkNumber()+list.get(position).getName());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_teacher_from_input:
                startActivity(new Intent(TeacherListActivity.this, TeacherCreationActivity.class));
                break;
            case R.id.add_teacher_from_file:
                startActivity(new Intent(TeacherListActivity.this, FileSelectActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 任务列表的适配器
     */
    class TeacherAdapter extends ArrayAdapter<TableTeacher> {
        private int resourceId;

        public TeacherAdapter(Context context, int resource, List<TableTeacher> objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           // Teacher teacher = getItem(position);
            TableTeacher teacher = getItem(position);
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

            @Override
            public void afterTextChanged(Editable s) {
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                }

                myhandler.post(eChanged);
            }
        });

    }

    Runnable eChanged = new Runnable() {
        @Override
        public void run() {
            String data = eSearch.getText().toString();

        }
    };

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
}
