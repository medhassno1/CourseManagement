package com.ftd.schaepher.coursemanagement.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.pojo.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxq on 2015/10/31.
 * 教学办登录默认主界面---任务主界面
 */
public class TaskJxbActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private List<Task> taskListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_jxb);
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setTitle("报课任务列表");

        initTaskListData();
        initTaskListView();
    }

    private void initTaskListData() {
        taskListData = new ArrayList<Task>();
        Task task = new Task("进行中","计算机专业");
        taskListData.add(task);
    }

    private void initTaskListView() {
        TaskAdapter mTaskAdapter = new TaskAdapter(this,R.layout.list_item_task,taskListData);
        ListView mListView = (ListView) findViewById(R.id.lv_task_list);
        mListView.setAdapter(mTaskAdapter);
        mListView.setOnItemClickListener(this);
    }

    //点击任务列表项跳转操作
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
