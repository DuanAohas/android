package com.aohas.library.ui.util.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;


/**
 * XListView
 */
public class XListView extends ListView implements OnScrollListener {
//    private static final String TAG = "XListView";

    private static final int INVALID_POINTER = -1;
    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;

    // when pull up >= 50px
    private final static int PULL_LOAD_MORE_DELTA = 50;

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;
    private float mInitialMotionY = -1;
    private int mActivePointerId = INVALID_POINTER;

    // used for scroll back
    private Scroller mScroller;
    // user's scroll listener
    private OnScrollListener mScrollListener;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    // the interface to trigger refresh and load more.
//    private IXListViewListener mListener;
    private OnRefreshListener mRefreshListener;
    private OnLoadMoreListener mLoadMoreListener;

    private XHeaderView mHeader;
    // header view content, use it to calculate the Header's height. And hide it when disable pull refresh.
    private LinearLayout mHeaderLayout;
    private int mHeaderHeight;

    private LinearLayout mFooterLayout;
    private XFooterView mFooterView;
    private boolean mIsFooterReady = false;

//    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

//    private boolean mEnablePullLoad = true;
    private boolean mEnableAutoLoad = false;
    private boolean mPullLoading = false;

    // total list items, used to detect is at the bottom of ListView
    private int mTotalItemCount;
    private Handler mHandler = new Handler();

    public XListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        // init header view
        mHeader = new XHeaderView(context);
        mHeaderLayout = new LinearLayout(context);
        mHeaderLayout.addView(mHeader, params);
        addHeaderView(mHeaderLayout, null, false);

        // init footer view
        mFooterView = new XFooterView(context);
        mFooterLayout = new LinearLayout(context);
        mFooterLayout.addView(mFooterView, params);

        // init header height
        ViewTreeObserver observer = mHeader.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeader.getHeaderHeight();
                    ViewTreeObserver observer = getViewTreeObserver();

                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
    }

    public void setAdapter(ListAdapter adapter) {
        if(!this.mIsFooterReady) {
            this.mIsFooterReady = true;
            this.addFooterView(this.mFooterLayout, (Object) null, false);
        }

        super.setAdapter(adapter);
    }

    public void stop() {
        this.stopRefresh();
        this.stopLoadMore();
    }

    public void stopRefresh() {
        if(this.mPullRefreshing) {
            this.mPullRefreshing = false;
            this.postDelayed(new Runnable() {
                public void run() {
                    XListView.this.resetHeaderHeight();
                }
            }, 150L);
        }

    }

    public void stopLoadMore() {
        if(this.mPullLoading) {
            this.mPullLoading = false;
            this.postDelayed(new Runnable() {
                public void run() {
                    XListView.this.mFooterView.setState(0);
                }
            }, 150L);
        }

    }

    public void setRefreshTime(String time) {
        this.mHeader.setRefreshTime(time);
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
        this.mHeaderLayout.setVisibility(null != this.mRefreshListener?View.VISIBLE:View.INVISIBLE);
    }

    public void setOnLoadMoreListener(boolean autoLoadEnable, OnLoadMoreListener listener) {
        this.mEnableAutoLoad = autoLoadEnable;
        this.mLoadMoreListener = listener;
        this.showOrHideFooter(listener != null);
    }

    public void showOrHideFooter(boolean isShow) {
        if(!isShow) {
            this.mFooterView.setBottomMargin(0);
            this.mFooterView.hide();
            this.mFooterView.setPadding(0, 0, 0, this.mFooterView.getHeight() * -1);
            this.mFooterView.setOnClickListener((OnClickListener)null);
        } else {
            this.mPullLoading = false;
            this.mFooterView.setPadding(0, 0, 0, 0);
            this.mFooterView.show();
            this.mFooterView.setState(0);
            this.mFooterView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    XListView.this.startLoadMore();
                }
            });
        }

    }

    public void autoRefresh() {
        this.mHeader.setVisibleHeight(this.mHeaderHeight);
        if(null != this.mRefreshListener && !this.mPullRefreshing) {
            if(this.mHeader.getVisibleHeight() > this.mHeaderHeight) {
                this.mHeader.setState(1);
            } else {
                this.mHeader.setState(0);
            }
        }

        this.mPullRefreshing = true;
        this.mHeader.setState(2);
        this.refresh();
    }

    private void invokeOnScrolling() {
        if(this.mScrollListener instanceof OnXScrollListener) {
            OnXScrollListener listener = (OnXScrollListener)this.mScrollListener;
            listener.onXScrolling(this);
        }

    }

    private void updateHeaderHeight(float delta) {
        this.mHeader.setVisibleHeight((int)delta + this.mHeader.getVisibleHeight());
        if(null != this.mRefreshListener && !this.mPullRefreshing) {
            if(this.mHeader.getVisibleHeight() > this.mHeaderHeight) {
                this.mHeader.setState(1);
            } else {
                this.mHeader.setState(0);
            }
        }

        this.setSelection(0);
    }

    private void resetHeaderHeight() {
        int height = this.mHeader.getVisibleHeight();
        if(height != 0 && (!this.mPullRefreshing || height > this.mHeaderHeight)) {
            int finalHeight = 0;
            if(this.mPullRefreshing && height > this.mHeaderHeight) {
                finalHeight = this.mHeaderHeight;
            }

            this.mScrollBack = 0;
            this.mScroller.startScroll(0, height, 0, finalHeight - height, 400);
            this.invalidate();
        }

    }

    private void updateFooterHeight(float delta) {
        int height = this.mFooterView.getBottomMargin() + (int)delta;
        if(null != this.mLoadMoreListener && !this.mPullLoading) {
            if(height > 50) {
                this.mFooterView.setState(1);
            } else {
                this.mFooterView.setState(0);
            }
        }

        this.mFooterView.setBottomMargin(height);
    }

    private void resetFooterHeight() {
        int bottomMargin = this.mFooterView.getBottomMargin();
        if(bottomMargin > 0) {
            this.mScrollBack = 1;
            this.mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, 400);
            this.invalidate();
        }

    }

    private void startLoadMore() {
        this.mPullLoading = true;
        this.mFooterView.setState(2);
        this.loadMore();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch(ev.getAction()) {
            case 0:
                this.mLastY = this.mInitialMotionY = ev.getRawY();
                this.mActivePointerId = ev.getPointerId(0);
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if(this.mLastY == -1.0F) {
            this.mLastY = ev.getRawY();
        }

        switch(ev.getAction()) {
            case 0:
                this.mLastY = this.mInitialMotionY = ev.getRawY();
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
            case 3:
            case 4:
            default:
                this.mLastY = -1.0F;
                if(this.getFirstVisiblePosition() == 0) {
                    if(null != this.mRefreshListener && this.mHeader.getVisibleHeight() > this.mHeaderHeight) {
                        this.mPullRefreshing = true;
                        this.mHeader.setState(2);
                        this.refresh();
                    }

                    this.resetHeaderHeight();
                } else if(this.getLastVisiblePosition() == this.mTotalItemCount - 1) {
                    if(null != this.mLoadMoreListener && this.mFooterView.getBottomMargin() > 50) {
                        this.startLoadMore();
                    }

                    this.resetFooterHeight();
                }
                break;
            case 2:
                if(this.mActivePointerId == -1) {
                    Log.e("XLIST", "Got ACTION_MOVE event but don\'t have an active pointer id.");
                    return false;
                }

                int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                if(pointerIndex < 0) {
                    Log.e("XLIST", "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                float deltaY = ev.getY(pointerIndex) - this.mLastY;
                if(this.getFirstVisiblePosition() == 0 && (this.mHeader.getVisibleHeight() > 0 || deltaY > 0.0F) && null != this.mRefreshListener) {
                    this.updateHeaderHeight(deltaY / 1.8F);
                    this.invokeOnScrolling();
                }

                this.mLastY = ev.getY(pointerIndex);
                break;
            case 5:
                int index = ev.getActionIndex();
                this.mInitialMotionY = ev.getY(index) - (this.mLastY - this.mInitialMotionY);
                this.mLastY = ev.getY(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                this.onSecondaryPointerUp(ev);
        }

        return super.onTouchEvent(ev);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        if(pointerId == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0?1:0;
            this.mLastY = ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }

    }

    public void computeScroll() {
        if(this.mScroller.computeScrollOffset()) {
            if(this.mScrollBack == 0) {
                this.mHeader.setVisibleHeight(this.mScroller.getCurrY());
            } else {
                this.mFooterView.setBottomMargin(this.mScroller.getCurrY());
            }

            this.postInvalidate();
            this.invokeOnScrolling();
        }

        super.computeScroll();
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mScrollListener = l;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(view, scrollState);
        }

        if(scrollState == 0 && this.mEnableAutoLoad && this.getLastVisiblePosition() == this.getCount() - 1) {
            this.startLoadMore();
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.mTotalItemCount = totalItemCount;
        if(this.mScrollListener != null) {
            this.mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

    }

    private void refresh() {
        if(null != this.mRefreshListener) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    XListView.this.mRefreshListener.onRefresh();
                }
            }, 400L);
        }

    }

    private void loadMore() {
        if(null != this.mLoadMoreListener) {
            this.mLoadMoreListener.onLoadMore();
        }

    }

    public interface OnXScrollListener extends OnScrollListener {
        void onXScrolling(View var1);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
