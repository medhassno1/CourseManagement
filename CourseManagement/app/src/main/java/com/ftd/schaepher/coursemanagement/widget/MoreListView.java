package com.ftd.schaepher.coursemanagement.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by sxq on 2015/11/29.
 */
public class MoreListView extends ListView {


    public MoreListView(Context context) {
        super(context);
    }

    public MoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
