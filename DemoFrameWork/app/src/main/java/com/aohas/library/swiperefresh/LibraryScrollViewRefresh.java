package com.aohas.library.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ScrollView;
import com.aohas.library.ui.adapter.LibraryBaseAdapter;

/**
 *
 */
public class LibraryScrollViewRefresh extends LibraryBaseRefresh<ScrollView> {

    public LibraryScrollViewRefresh(Context context) {
        super(context);
    }

    public LibraryScrollViewRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LibraryScrollViewRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(t + getHeight() + lastNum >=  computeVerticalScrollRange()){
            //ScrollView滑动到底部了
            if (libraryOnRefreshListener != null) {
                libraryOnRefreshListener.onLastItemVisible();
            }
        }
    }

    @Override
    protected ScrollView createRefreshableView(Context context) {
        ScrollView scrollView = new ScrollView(context);
        return scrollView;
    }

    @Override
    public void onRefresh() {
        if (libraryOnRefreshListener != null) {
            libraryOnRefreshListener.onRefresh();
        }
    }
}
