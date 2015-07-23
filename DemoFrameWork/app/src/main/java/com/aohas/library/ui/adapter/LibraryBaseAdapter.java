package com.aohas.library.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.aohas.library.Library;
import com.aohas.library.ui.util.RelayoutViewTool;

/**
 * Created by liuyu on 14-7-11.
 */
public abstract class LibraryBaseAdapter extends BaseAdapter{

    public abstract View getView();

    public abstract void initData(int position, View convertView, ViewGroup parent);

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = getView();
            RelayoutViewTool.relayoutViewWithScale(convertView, Library.screenWidthScale);
        }
        initData(position, convertView, parent);
        return convertView;
    }
}
