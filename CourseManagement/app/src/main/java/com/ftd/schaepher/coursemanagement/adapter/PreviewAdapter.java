package com.ftd.schaepher.coursemanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ftd.schaepher.coursemanagement.R;
import com.ftd.schaepher.coursemanagement.pojo.TableCourseMultiline;

import java.util.List;

public class PreviewAdapter extends BaseAdapter{

    private List<TableCourseMultiline> mListData;
    private Context mContext;

    public PreviewAdapter(Context context, List<TableCourseMultiline> previewMajorList) {
        mListData = previewMajorList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_iten_preview_commit, parent, false);
            holder = new ViewHolder();
            holder.majorName = (TextView) view.findViewById(R.id.tv_major_name);
            holder.majorGrade = (TextView) view.findViewById(R.id.tv_major_grade);
            holder.majorType = (TextView) view.findViewById(R.id.tv_major_type);
            holder.majorNote = (TextView) view.findViewById(R.id.tv_major_note);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        TableCourseMultiline major = mListData.get(position);
        holder.majorName.setText(major.getCourseName());
        holder.majorGrade.setText(major.getGrade());
        holder.majorType.setText(major.getCourseType());
        holder.majorNote.setText(major.getRemark());

        return view;
    }

    class ViewHolder{
         TextView majorName;
         TextView majorGrade;
         TextView majorType;
         TextView majorNote;
    }
}
