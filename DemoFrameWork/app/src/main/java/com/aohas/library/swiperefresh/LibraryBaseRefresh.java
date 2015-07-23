package com.aohas.library.swiperefresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 *
 */
public abstract class LibraryBaseRefresh<T extends View> extends LinearLayout implements SwipeRefreshLayout.OnRefreshListener {

    T mRefreshableView;
    LibraryOnRefreshListener libraryOnRefreshListener;
    int lastNum = 0;

    public LibraryBaseRefresh(Context context) {
        super(context);
        init(context);
    }

    public LibraryBaseRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LibraryBaseRefresh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context);
        this.addView(swipeRefreshLayout);
        swipeRefreshLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRefreshableView = createRefreshableView(context);
        swipeRefreshLayout.addView(mRefreshableView);
        mRefreshableView.setLayoutParams(new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    public final T getRefreshableView() {
        return mRefreshableView;
    }

    public void setOnRefreshListener(int lastVisibleNum, LibraryOnRefreshListener libraryOnRefreshListener){
        this.lastNum = lastVisibleNum;
        this.libraryOnRefreshListener = libraryOnRefreshListener;
    }

    protected abstract T createRefreshableView(Context context);

    public interface LibraryOnRefreshListener{
        public void onRefresh();
        public void onLastItemVisible();
    }
}
