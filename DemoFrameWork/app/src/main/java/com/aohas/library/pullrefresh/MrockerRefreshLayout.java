/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aohas.library.pullrefresh;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;


/**
 * 下拉刷新主View
 */
public class MrockerRefreshLayout extends ViewGroup {
    private static final String LOG_TAG = MrockerRefreshLayout.class.getSimpleName();

    private static final long RETURN_TO_ORIGINAL_POSITION_TIMEOUT = 0;
    private static final long RETURN_TO_END_POSITION_TIMEOUT = 500;
    private static final float ACCELERATE_INTERPOLATION_FACTOR = 1.5f;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final float PROGRESS_BAR_HEIGHT = 4;
    private static final float MAX_SWIPE_DISTANCE_FACTOR = .6f;
    private static final int REFRESH_TRIGGER_DISTANCE = 120;
    private static final int INVALID_POINTER = -1;
    private static final int PULL_CHILD_INDEX = 2;

    private final AttributeSet attrs;

    private MrockerProgressBar mProgressBar; //the thing that shows progress is going
    private View mTarget; //the content that gets pulled down
    private int mOriginalOffsetTop;
    private int mOriginalOffsetBottom;
    private OnRefreshListener mRefreshUpListener;
    private OnEndRefreshListener mRefreshEndListener;
    private OnLastItemVisibleListener mLastItemVisibleListener;
    private int mFrom;
    private boolean mRefreshing = false;
    private int mTouchSlop;
    private float mDistanceToTriggerSync = -1;
    private int mMediumAnimationDuration;
    private float mFromPercentage = 0;
    private float mCurrPercentage = 0;
    private int mProgressBarHeight;
    private int mCurrentTargetOffsetTop;

    private float mInitialMotionY;
    private float mLastMotionY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;

    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mReturningToStart;
    private final DecelerateInterpolator mDecelerateInterpolator;
    private final AccelerateInterpolator mAccelerateInterpolator;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };


    private MrockerLoadingLayout mrockerLoadingLayout;
    private int loadingLayoutheihgt = 0;
    private int loadingLayoutheihgtFrom = 0;

    private MrockerLoadingLayout mrockerFooterLayout;
    private int loadingFooterheihgt = 0;
    private int loadingFooterheihgtFrom = 0;


    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetTop = 0;
            if (mFrom != mOriginalOffsetTop) {
                targetTop = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
            }
            int offset = targetTop - mTarget.getTop();
            final int currentTop = mTarget.getTop();
            if (offset + currentTop < 0) {
                offset = 0 - currentTop;
            }
            setTargetOffsetTopAndBottom(offset);
            //动画清除进度条
            setProgressTriggerPercentage(targetTop / mDistanceToTriggerSync);
        }
    };

    private final Animation mAnimateToEndPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int targetBottom = ((getBottom() + mFrom) - (int) ((mFrom) * interpolatedTime));
            int offset = targetBottom - mTarget.getBottom();
            final int currentBottom = mTarget.getBottom();
            if (offset + currentBottom > getBottom()) {
                offset = 0 + currentBottom;
            }
            setTargetOffsetTopAndBottom(offset);
            //动画清除进度条
            setFooterProgressTriggerPercentage((getBottom() - targetBottom) / mDistanceToTriggerSync);
        }
    };

    private final Animation mAnimateHeaderToHide = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = loadingLayoutheihgtFrom + (int) ((0 - loadingLayoutheihgtFrom) * interpolatedTime);
            loadingLayoutheihgt = newHeight;
            mrockerLoadingLayout.getLayoutParams().height = newHeight;
            mrockerLoadingLayout.requestLayout();
        }
    };

    private final Animation mAnimateFooterToHide = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = loadingFooterheihgtFrom + (int) ((0 - loadingFooterheihgtFrom) * interpolatedTime);
            loadingFooterheihgt = newHeight;
            mrockerFooterLayout.getLayoutParams().height = newHeight;
            mrockerFooterLayout.requestLayout();
        }
    };

    private Animation mShrinkTrigger = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            float percent = mFromPercentage + ((0 - mFromPercentage) * interpolatedTime);
            mProgressBar.setTriggerPercentage(percent);
        }
    };

    private final AnimationListener mReturnToStartPositionListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // Once the target content has returned to its start position, reset
            // the target offset to 0
            mCurrentTargetOffsetTop = 0;
        }
    };


    private final AnimationListener mReturnToHeaderHideListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // Once the target content has returned to its start position, reset
            // the target offset to 0
            loadingLayoutheihgt = 0;
        }
    };

    private final AnimationListener mReturnToFooterHideListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            // Once the target content has returned to its start position, reset
            // the target offset to 0
            loadingFooterheihgt = 0;
        }
    };

    private final AnimationListener mShrinkAnimationListener = new BaseAnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            mCurrPercentage = 0;
        }
    };

    private final Runnable mReturnToStartPosition = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            if (mCurrentTargetOffsetTop > 0){
                animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(),
                        mReturnToStartPositionListener);
                animateHeaderHide(mReturnToHeaderHideListener);
            }else{
                animateOffsetToEndPosition(mCurrentTargetOffsetTop - getPaddingBottom(),
                        mReturnToStartPositionListener);
                animateFooterHide(mReturnToFooterHideListener);
            }
        }
    };

    // Cancel the refresh gesture and animate everything back to its original state.
    private final Runnable mCancel = new Runnable() {

        @Override
        public void run() {
            mReturningToStart = true;
            // Timeout fired since the user last moved their finger; animate the
            // trigger to 0 and put the target back at its original position
            if (mProgressBar != null) {
                mFromPercentage = mCurrPercentage;
                mShrinkTrigger.setDuration(mMediumAnimationDuration);
                mShrinkTrigger.setAnimationListener(mShrinkAnimationListener);
                mShrinkTrigger.reset();
                mShrinkTrigger.setInterpolator(mDecelerateInterpolator);
                startAnimation(mShrinkTrigger);
            }

            if (mCurrentTargetOffsetTop > 0){
                animateOffsetToStartPosition(mCurrentTargetOffsetTop + getPaddingTop(),
                        mReturnToStartPositionListener);
                animateHeaderHide(mReturnToHeaderHideListener);
            }else{
                animateOffsetToEndPosition(mCurrentTargetOffsetTop - getPaddingBottom(),
                        mReturnToStartPositionListener);
                animateFooterHide(mReturnToFooterHideListener);
            }
        }
    };

    private final Runnable mReturnToEndPosition = new Runnable() {

        @Override
        public void run() {
            if (isChildScrollEnd() && mLastItemVisibleListener != null) {
                mLastItemVisibleListener.onLastItemVisible();
            }
        }
    };

    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     *
     * @param context
     */
    public MrockerRefreshLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     *
     * @param context
     * @param attrs
     */
    public MrockerRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.attrs = attrs;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMediumAnimationDuration = getResources().getInteger(
                android.R.integer.config_mediumAnimTime);

        setWillNotDraw(false);
        mProgressBar = new MrockerProgressBar(this);
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mProgressBarHeight = (int) (metrics.density * PROGRESS_BAR_HEIGHT);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
        mAccelerateInterpolator = new AccelerateInterpolator(ACCELERATE_INTERPOLATION_FACTOR);


        mrockerLoadingLayout = new MrockerLoadingLayout(context, attrs);
        addView(mrockerLoadingLayout, 0);

        mrockerFooterLayout = new MrockerLoadingLayout(context, attrs);
        addView(mrockerFooterLayout, 1);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeCallbacks(mCancel);
        removeCallbacks(mReturnToStartPosition);
        removeCallbacks(mReturnToEndPosition);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mReturnToStartPosition);
        removeCallbacks(mCancel);
        removeCallbacks(mReturnToEndPosition);

    }

    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(mMediumAnimationDuration);
        mAnimateToStartPosition.setAnimationListener(listener);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mTarget.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToEndPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToEndPosition.reset();
        mAnimateToEndPosition.setDuration(mMediumAnimationDuration);
        mAnimateToEndPosition.setAnimationListener(listener);
        mAnimateToEndPosition.setInterpolator(mDecelerateInterpolator);
        mTarget.startAnimation(mAnimateToEndPosition);
    }

    private void animateHeaderHide(AnimationListener listener) {
        loadingLayoutheihgtFrom = loadingLayoutheihgt;
        mAnimateHeaderToHide.reset();
        mAnimateHeaderToHide.setDuration(mMediumAnimationDuration);
        mAnimateHeaderToHide.setAnimationListener(listener);
        mAnimateHeaderToHide.setInterpolator(mDecelerateInterpolator);
        mrockerLoadingLayout.startAnimation(mAnimateHeaderToHide);
    }

    private void animateFooterHide(AnimationListener listener) {
        loadingFooterheihgtFrom = loadingFooterheihgt;
        mAnimateFooterToHide.reset();
        mAnimateFooterToHide.setDuration(mMediumAnimationDuration);
        mAnimateFooterToHide.setAnimationListener(listener);
        mAnimateFooterToHide.setInterpolator(mDecelerateInterpolator);
        mrockerFooterLayout.startAnimation(mAnimateFooterToHide);
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshUpListener = listener;
    }

    public void setOnEndRefreshListener(OnEndRefreshListener listener) {
        mRefreshEndListener = listener;
    }

    /**
     * Set the listener to be notified when view is end
     * gesture.
     */
    public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
        mLastItemVisibleListener = listener;
    }

    /**
     * Set the four colors used in the progress animation from color resources.
     * The first color will also be the color of the bar that grows in response
     * to a user swipe gesture.
     */
    public void setColorSchemeResources(int colorRes1, int colorRes2, int colorRes3,
                                        int colorRes4) {
        final Resources res = getResources();
        setColorSchemeColors(res.getColor(colorRes1), res.getColor(colorRes2),
                res.getColor(colorRes3), res.getColor(colorRes4));
    }

    /**
     * Set the four colors used in the progress animation. The first color will
     * also be the color of the bar that grows in response to a user swipe
     * gesture.
     */
    public void setColorSchemeColors(int color1, int color2, int color3, int color4) {
        ensureTarget();
        mProgressBar.setColorScheme(color1, color2, color3, color4);
    }

    public void setHeaderView(int layoutId) {
        View headerView = View.inflate(getContext(), layoutId, null);
        setHeaderView(headerView);
    }

    public void setHeaderView(View headerView) {
        mrockerLoadingLayout = new MrockerLoadingLayout(getContext(), attrs, headerView);
        removeViewAt(0);
        addView(mrockerLoadingLayout, 0);
    }

    public void setFooterView(int layoutId) {
        View headerView = View.inflate(getContext(), layoutId, null);
        setFooterView(headerView);
    }

    public void setFooterView(View footerView) {
        mrockerFooterLayout = new MrockerLoadingLayout(getContext(), attrs, footerView);
        removeViewAt(1);
        addView(mrockerFooterLayout, 1);
    }

    private void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid out yet.
        if (mTarget == null) {
            if (getChildCount() > PULL_CHILD_INDEX + 1 && !isInEditMode()) {
                throw new IllegalStateException(
                        "SwipeRefreshLayout can host only one direct child");
            }
            mTarget = getChildAt(PULL_CHILD_INDEX);
            mOriginalOffsetTop = mTarget.getTop() + getPaddingTop();
            mOriginalOffsetBottom = mTarget.getBottom() + getPaddingBottom();
            if (mTarget instanceof ViewGroup) {
                ((ViewGroup) mTarget).setClipToPadding(false);
            }
        }
        if (mDistanceToTriggerSync == -1) {
            if (getParent() != null && ((View) getParent()).getHeight() > 0) {
                final DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDistanceToTriggerSync = (int) Math.min(
                        ((View) getParent()).getHeight() * MAX_SWIPE_DISTANCE_FACTOR,
                        REFRESH_TRIGGER_DISTANCE * metrics.density);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mProgressBar.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() == 0) {
            return;
        }
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();

        final int childProgressLeft = paddingLeft;
        final int childProgressTop =  paddingTop;
        final int childProgressWidth = width - paddingLeft - paddingRight;
        final int childProgressHeight = mProgressBarHeight;
        mProgressBar.setBounds(childProgressLeft, childProgressTop, childProgressLeft + childProgressWidth, childProgressTop + childProgressHeight);

        final View childHeader = getChildAt(0);
        final int childHeaderLeft = paddingLeft;
        final int childHeaderTop = paddingTop;
        final int childHeaderWidth = width - paddingLeft - paddingRight;
        final int childHeaderHeight = loadingLayoutheihgt;
        childHeader.layout(childHeaderLeft, childHeaderTop, childHeaderLeft + childHeaderWidth, childHeaderTop + childHeaderHeight);

        final View childFooter = getChildAt(1);
        final int childFooterLeft = paddingLeft;
        final int childFooterTop = height - loadingFooterheihgt - paddingBottom;
        final int childFooterWidth = width - paddingLeft - paddingRight;
        final int childFooterHeight = loadingFooterheihgt;
        childFooter.layout(childFooterLeft, childFooterTop, childHeaderLeft + childFooterWidth, childFooterTop + childFooterHeight);


        final View child = getChildAt(PULL_CHILD_INDEX);
        final int childLeft = paddingLeft;
        final int childTop = mCurrentTargetOffsetTop + paddingTop;
        final int childWidth = width - paddingLeft - paddingRight;
        final int childHeight = height - paddingTop - paddingBottom;
        child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > PULL_CHILD_INDEX + 1 && !isInEditMode()) {
            throw new IllegalStateException("SwipeRefreshLayout can host only one direct child");
        }
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).measure(
                        MeasureSpec.makeMeasureSpec(
                                getMeasuredWidth() - getPaddingLeft() - getPaddingRight(),
                                MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(
                                getMeasuredHeight() - getPaddingTop() - getPaddingBottom(),
                                MeasureSpec.EXACTLY));
            }
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    private boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return mTarget.getScrollY() > 0;
            }
        } else {
            return mTarget.canScrollVertically(-1);
        }
    }

    /**
     * 判断是否滑到底部
     */
    private boolean isChildScrollEnd() {
        if (mTarget instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) mTarget;

//            if (absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1){
//            Log.d("========pull======", "bottom: " + absListView.getChildAt(absListView.getChildCount() - 1).getBottom() + " bottom: " + absListView.getBottom());

//            }


            //todo  底部判断有问题
            return absListView.getLastVisiblePosition() == absListView.getAdapter().getCount() - 1;
        } else if (mTarget instanceof ScrollView) {
            return mTarget.getScrollY() >= (((ScrollView) mTarget).getChildAt(0).getHeight() - getHeight());
        } else if (mTarget instanceof WebView) {
            float exactContentHeight = FloatMath.floor(((WebView) mTarget).getContentHeight() * ((WebView) mTarget).getScale());
            return mTarget.getScrollY() >= (exactContentHeight - mTarget.getHeight());
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ensureTarget();

        final int action = ev.getActionMasked();

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }
        updateEndPositionTimeout();

        if (!isEnabled() || mReturningToStart || mRefreshing || (canChildScrollUp() && !isChildScrollEnd())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                final float yDiff = y - mInitialMotionY;
                if (yDiff > mTouchSlop && mRefreshUpListener != null && !canChildScrollUp()) {
                    mLastMotionY = y;
                    mIsBeingDragged = true;
                } else if (-yDiff > mTouchSlop && mRefreshEndListener != null && isChildScrollEnd()) {
                    mLastMotionY = y;
                    mIsBeingDragged = true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        return mIsBeingDragged;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // Nope.
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();

        if (mReturningToStart && action == MotionEvent.ACTION_DOWN) {
            mReturningToStart = false;
        }


        if (!isEnabled() || mReturningToStart || mRefreshing || (canChildScrollUp() && !isChildScrollEnd())) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = ev.getY(pointerIndex);
                final float yDiff = y - mInitialMotionY;

                if (!mIsBeingDragged && yDiff > mTouchSlop && (mRefreshUpListener != null || mRefreshEndListener != null)) {
                    mIsBeingDragged = true;
                }

                if (mIsBeingDragged) {
                    // User velocity passed min velocity; trigger a refresh
                    if (!canChildScrollUp()) {
                        if (yDiff > mDistanceToTriggerSync) {
                            // User movement passed distance; trigger a refresh
                            startRefresh();
                        } else {
                            // Just track the user's movement
                            updateContentOffsetTop((int) (yDiff));
                            setTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
                            setProgressTriggerPercentage(mAccelerateInterpolator.getInterpolation(yDiff / mDistanceToTriggerSync));
                            if (mLastMotionY > y && mTarget.getTop() == getPaddingTop()) {
                                // If the user puts the view back at the top, we
                                // don't need to. This shouldn't be considered
                                // cancelling the gesture as the user can restart from the top.
                                removeCallbacks(mCancel);
                            } else {
//                            updatePositionTimeout();
                            }
                        }
                    } else if (isChildScrollEnd()) {
                        if (yDiff < -mDistanceToTriggerSync) {
                            // User movement passed distance; trigger a refresh
                            startEndRefresh();
                        } else {
                            endContentOffsetTop((int) (yDiff));
                            setFooterProgressTriggerPercentage(mAccelerateInterpolator.getInterpolation(-yDiff / mDistanceToTriggerSync));
                        }
                    }
                    mLastMotionY = y;
                }
                break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionY = ev.getY(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                updatePositionTimeout();
                mIsBeingDragged = false;
                mCurrPercentage = 0;
                mActivePointerId = INVALID_POINTER;
                return false;
        }

        return true;
    }

    /**
     * @return Whether the SwipeRefreshWidget is actively showing refresh
     * progress.
     */
    public boolean isRefreshing() {
        return mRefreshing;
    }

    public MrockerLoadingLayout getMrockerLoadingLayout() {
        return mrockerLoadingLayout;
    }

    public MrockerLoadingLayout getMrockerFooterLoadingLayout() {
        return mrockerFooterLayout;
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (mRefreshing != refreshing) {
            ensureTarget();
            mCurrPercentage = 0;
            mRefreshing = refreshing;
            if (mRefreshing) {
                mProgressBar.start();
            } else {
                mProgressBar.stop();
            }
        }
    }

    private void startRefresh() {
        removeCallbacks(mCancel);
        mReturnToStartPosition.run();
        setRefreshing(true);
        mRefreshUpListener.onRefresh();
    }

    private void startEndRefresh() {
        removeCallbacks(mCancel);
        mReturnToStartPosition.run();
//        setRefreshing(true);
        mRefreshEndListener.onRefresh();
    }

    private void setTriggerPercentage(float percent) {
        if (!mrockerLoadingLayout.isHaveProgress()) {
            if (percent == 0f) {
                // No-op. A null trigger means it's uninitialized, and setting it to zero-percent
                // means we're trying to reset state, so there's nothing to reset in this case.
                mCurrPercentage = 0;
                return;
            }
            mCurrPercentage = percent;
            mProgressBar.setTriggerPercentage(percent);
        }
    }

    private void setProgressTriggerPercentage(float percent) {
        if (percent == 0f) {
            // No-op. A null trigger means it's uninitialized, and setting it to zero-percent
            // means we're trying to reset state, so there's nothing to reset in this case.
            mCurrPercentage = 0;
            return;
        }
        mrockerLoadingLayout.setTriggerPercentage(percent);
    }


    private void setFooterProgressTriggerPercentage(float percent) {
        if (percent == 0f) {
            // No-op. A null trigger means it's uninitialized, and setting it to zero-percent
            // means we're trying to reset state, so there's nothing to reset in this case.
            mCurrPercentage = 0;
            return;
        }
        mrockerFooterLayout.setTriggerPercentage(percent);
    }

    private void updateContentOffsetTop(int targetTop) {
        final int currentTop = mTarget.getTop();
        if (targetTop > mDistanceToTriggerSync) {
            targetTop = (int) mDistanceToTriggerSync;
        } else if (targetTop < 0) {
            targetTop = 0;
        }
        setTargetOffsetTopAndBottom(targetTop - currentTop);
        mTarget.post(new Runnable() {
            @Override
            public void run() {
                setHeaderHeight(mTarget.getTop());
            }
        });
    }

    private void endContentOffsetTop(int targetEnd) {
        targetEnd = -targetEnd;
        final int currentTop = mTarget.getTop();
        if (targetEnd > mDistanceToTriggerSync) {
            targetEnd = (int) mDistanceToTriggerSync;
        } else if (targetEnd < 0) {
            targetEnd = 0;
        }
        setTargetOffsetTopAndBottom((-targetEnd) - currentTop);
        mTarget.post(new Runnable() {
            @Override
            public void run() {
                setFooterHeight(getHeight() - mTarget.getBottom());
            }
        });
    }

    private void setHeaderHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        loadingLayoutheihgt = height;
        mrockerLoadingLayout.getLayoutParams().height = loadingLayoutheihgt;
        mrockerLoadingLayout.requestLayout();
    }

    private void setFooterHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        loadingFooterheihgt = height;
        mrockerFooterLayout.getLayoutParams().height = loadingFooterheihgt;
        mrockerFooterLayout.requestLayout();
    }

    private void setTargetOffsetTopAndBottom(int offset) {
        mTarget.offsetTopAndBottom(offset);
        mCurrentTargetOffsetTop = mTarget.getTop();
        this.invalidate();
    }

    private void updatePositionTimeout() {
        removeCallbacks(mCancel);
        postDelayed(mCancel, RETURN_TO_ORIGINAL_POSITION_TIMEOUT);
    }

    private void updateEndPositionTimeout() {
        removeCallbacks(mReturnToEndPosition);
        postDelayed(mReturnToEndPosition, RETURN_TO_END_POSITION_TIMEOUT);
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionY = ev.getY(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    /**
     * Classes that wish to be notified when the swipe gesture correctly
     * triggers a refresh should implement this interface.
     */
    public interface OnRefreshListener {
        public void onRefresh();
    }

    public interface OnEndRefreshListener {
        public void onRefresh();
    }

    public interface OnLastItemVisibleListener {
        public void onLastItemVisible();
    }

    /**
     * Simple AnimationListener to avoid having to implement unneeded methods in
     * AnimationListeners.
     */
    private class BaseAnimationListener implements AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
