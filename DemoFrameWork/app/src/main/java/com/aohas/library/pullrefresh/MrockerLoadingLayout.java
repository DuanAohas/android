package com.aohas.library.pullrefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aohas.demoframework.R;

/**
 * headerView
 */
public final class MrockerLoadingLayout extends LinearLayout {
    private final String LOG_TAG = MrockerLoadingLayout.class.getSimpleName();
    private final int PullRefreshDefault1 = 0;
    private final int PullRefreshDefault2 = 1;

    private DisplayMetrics displayMetrics;
    private int layoutStyle;
    private RelativeLayout relativeLayout;
    protected ImageView mHeaderImage;
    protected MrockerRoundProgressBar mHeaderProgress;
    private TextView mHeaderText;

    public MrockerLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        initDefault(context, attrs);
    }

    public MrockerLoadingLayout(Context context, AttributeSet attrs, View header) {
        super(context, attrs);
        getAttrs(context, attrs);
        initHeader(header);
    }

    private void getAttrs(Context context, AttributeSet attrs){
        displayMetrics = getResources().getDisplayMetrics();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.MrockerPullRefresh);
//        //获取自定义属性和默认值
        layoutStyle = mTypedArray.getInt(R.styleable.MrockerPullRefresh_pullLayoutStyle, 0);

        relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(layoutParams);
        this.addView(relativeLayout);
    }

    private void initDefault(Context context, AttributeSet attrs) {
        if (layoutStyle == PullRefreshDefault1) {
            mHeaderProgress = new MrockerRoundProgressBar(context, attrs);
            mHeaderProgress.setId(R.id.pull_progress);
            RelativeLayout.LayoutParams layoutParams = new  RelativeLayout.LayoutParams(getPxForDp(50), getPxForDp(50));
            layoutParams.setMargins(0, getPxForDp(10), 0, getPxForDp(10));
            layoutParams.addRule( RelativeLayout.ALIGN_PARENT_TOP |  RelativeLayout.CENTER_HORIZONTAL);
            mHeaderProgress.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams textLayout = new  RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mHeaderText = new TextView(context);
            mHeaderText.setId(R.id.pull_text);
            mHeaderText.setText("下拉刷新...");
            textLayout.addRule(RelativeLayout.BELOW, mHeaderProgress.getId());
            textLayout.addRule(RelativeLayout.CENTER_HORIZONTAL);
            mHeaderText.setLayoutParams(textLayout);

            relativeLayout.addView(mHeaderProgress);
            relativeLayout.addView(mHeaderText);
        } else if (layoutStyle == PullRefreshDefault2) {
            mHeaderProgress = new MrockerRoundProgressBar(context);
            mHeaderProgress.setId(R.id.pull_progress);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getPxForDp(50), getPxForDp(50));
            layoutParams.setMargins(getPxForDp(20), getPxForDp(10), 0, getPxForDp(10));
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT | RelativeLayout.CENTER_VERTICAL);
            mHeaderProgress.setLayoutParams(layoutParams);

            LayoutParams textLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPxForDp(70));
            mHeaderText = new TextView(context);
            mHeaderText.setId(R.id.pull_text);
            mHeaderText.setGravity(Gravity.CENTER);
            mHeaderText.setText("下拉刷新...");
            mHeaderText.setLayoutParams(textLayoutParams);

            relativeLayout.addView(mHeaderProgress);
            relativeLayout.addView(mHeaderText);
        }
    }

    private void initHeader(View headerView) {
        View mHeaderPrs = headerView.findViewById(R.id.pull_progress);
        View mHeaderImg = headerView.findViewById(R.id.pull_image);
        View mHeaderTxt = headerView.findViewById(R.id.pull_text);

        if (mHeaderTxt != null && mHeaderTxt instanceof TextView) {
            mHeaderText = (TextView) mHeaderTxt;
        } else if (mHeaderTxt != null && !(mHeaderTxt instanceof TextView)) {
            Log.e(LOG_TAG, "this must be TextView by id pull_text");
        }

        if (mHeaderPrs != null && mHeaderPrs instanceof MrockerRoundProgressBar) {
            mHeaderProgress = (MrockerRoundProgressBar) mHeaderPrs;
        } else if (mHeaderPrs != null && !(mHeaderPrs instanceof MrockerRoundProgressBar)) {
            Log.e(LOG_TAG, "this must be RoundProgressBar by id pull_progress");
        }

        if (mHeaderImg != null && mHeaderImg instanceof ImageView) {
            mHeaderImage = (ImageView) mHeaderImg;
        } else if (mHeaderImg != null && !(mHeaderImg instanceof MrockerRoundProgressBar)) {
            Log.e(LOG_TAG, "this must be ImageView by id pull_image");
        }
        relativeLayout.addView(headerView);
    }

    public void setText(String str) {
        if (mHeaderText != null)
            mHeaderText.setText(str);
    }

    public void setTextSize(float size) {
        if (mHeaderText != null)
            mHeaderText.setTextSize(size);
    }

    public void setTextColor(int color) {
        if (mHeaderText != null)
            mHeaderText.setTextColor(color);
    }

    public void setCricleColor(int color) {
        if (mHeaderProgress != null)
            mHeaderProgress.setCricleColor(color);
    }

    public void setCricleProgressColor(int color) {
        if (mHeaderProgress != null)
            mHeaderProgress.setCricleProgressColor(color);
    }

    public void setProgressIsFill(boolean isFill) {
        if (mHeaderProgress != null)
            mHeaderProgress.setStyle(isFill ? MrockerRoundProgressBar.FILL : MrockerRoundProgressBar.STROKE);
    }

     public void setProgressRoundWidth(float roundWidth) {
        if (mHeaderProgress != null)
            mHeaderProgress.setRoundWidth(roundWidth);
    }

    protected boolean isHaveProgress() {
        return mHeaderProgress != null;
    }

    protected void setTriggerPercentage(float percent) {
        if (mHeaderImage != null)
            mHeaderImage.setRotation(percent * 300);
        if (mHeaderProgress != null)
            mHeaderProgress.setProgress((int) (percent * 100));
    }

    private int getPxForDp(int dp) {
        return (int) ((dp - 0.5) * displayMetrics.density);
    }
}
