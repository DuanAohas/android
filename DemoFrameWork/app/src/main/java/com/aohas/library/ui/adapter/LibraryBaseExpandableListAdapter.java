package com.aohas.library.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;

/**
 * Created by liuyu on 14-7-11.
 */
public abstract class LibraryBaseExpandableListAdapter extends BaseExpandableListAdapter {

    public abstract View getGroupView();
    public abstract View getChildView();
    public abstract void initChildData(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent);
    public abstract void initGroupData(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent);

    @Override
    public final View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = getGroupView();
            RelayoutViewTool.relayoutViewWithScale(convertView, Library.screenWidthScale);
        }
        initGroupData(groupPosition, isExpanded, convertView, parent);
        return convertView;
    }

    @Override
    public final View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = getChildView();
            RelayoutViewTool.relayoutViewWithScale(convertView, Library.screenWidthScale);
        }
        initChildData(groupPosition, childPosition, isLastChild, convertView, parent);
        return convertView;
    }
}
