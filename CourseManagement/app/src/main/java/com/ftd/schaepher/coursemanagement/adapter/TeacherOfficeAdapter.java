package com.ftd.schaepher.coursemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.pojo.TableTaskInfo;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeacher;
import com.ftd.schaepher.coursemanagement.pojo.TableUserTeachingOffice;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sxq on 2015/11/29.
 */
public class TeacherOfficeAdapter extends ArrayAdapter<TableUserTeachingOffice> {
    private int resourceId;

    public TeacherOfficeAdapter(Context context, int resource, List<TableUserTeachingOffice> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TableUserTeachingOffice teacher = getItem(position);
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
            viewHolder = (TeacherOfficeAdapter.viewHolder) view.getTag();
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
