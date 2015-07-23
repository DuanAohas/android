package com.aohas.library.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aohas.library.ui.adapter.LibraryBaseAdapter;

/**
 *
 */
public class LibraryListViewRefresh extends LibraryBaseRefresh<ListView> {

    ListView listView;

    public LibraryListViewRefresh(Context context) {
        super(context);
    }

    public LibraryListViewRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LibraryListViewRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private ListView createListView(Context context) {
        ListView listView = new ListView(context);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount + lastNum == totalItemCount && totalItemCount > 0) {
                    if (libraryOnRefreshListener != null) {
                        libraryOnRefreshListener.onLastItemVisible();
                    }
                }
            }
        });
        return listView;
    }

    public void addHeaderView(View view){
        listView.addHeaderView(view);
    }


    public void addFooterView(View view){
        listView.addFooterView(view);
    }

    public void setAdapter(LibraryBaseAdapter adapter){
        listView.setAdapter(adapter);
    }

    @Override
    protected ListView createRefreshableView(Context context) {
        ListView listView = createListView(context);
        return listView;
    }


    @Override
    public void onRefresh() {
        if (libraryOnRefreshListener != null) {
            libraryOnRefreshListener.onRefresh();
        }
    }
}
