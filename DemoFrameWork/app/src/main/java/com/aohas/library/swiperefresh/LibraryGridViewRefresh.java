package com.aohas.library.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 *
 */
public class LibraryGridViewRefresh extends LibraryBaseRefresh<GridView> {

    GridView gridView;

    public LibraryGridViewRefresh(Context context) {
        super(context);
    }

    public LibraryGridViewRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LibraryGridViewRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private GridView createGridView(Context context) {
        GridView gridView = new GridView(context);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        return gridView;
    }


    public void setAdapter(ListAdapter adapter) {
        gridView.setAdapter(adapter);
    }

    @Override
    protected GridView createRefreshableView(Context context) {
        GridView gridView = createGridView(context);
        return gridView;
    }


    @Override
    public void onRefresh() {
        if (libraryOnRefreshListener != null) {
            libraryOnRefreshListener.onRefresh();
        }
    }
}
