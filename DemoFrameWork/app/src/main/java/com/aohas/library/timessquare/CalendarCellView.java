// Copyright 2013 Square, Inc.

package com.aohas.library.timessquare;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.aohas.library.Library;
//import com.aohas.library.R;
import com.aohas.library.timessquare.MonthCellDescriptor.RangeState;
import com.aohas.library.util.ParcelUtil;

public class CalendarCellView extends TextView {

    private static final int[] STATE_SELECTABLE = {
            ParcelUtil.getAttrId("slibrary_tate_selectable")
    };
    private static final int[] STATE_CURRENT_MONTH = {
            ParcelUtil.getAttrId("library_state_current_month")
    };
    private static final int[] STATE_TODAY = {
            ParcelUtil.getAttrId("library_state_today")
    };
    private static final int[] STATE_RANGE_FIRST = {
            ParcelUtil.getAttrId("library_state_range_first")
    };
    private static final int[] STATE_RANGE_MIDDLE = {
            ParcelUtil.getAttrId("library_state_range_middle")
    };
    private static final int[] STATE_RANGE_LAST = {
            ParcelUtil.getAttrId("library_state_range_last")
    };

    private boolean isSelectable = false;
    private boolean isCurrentMonth = false;
    private boolean isToday = false;
    private RangeState rangeState = RangeState.NONE;

    public CalendarCellView(Context context) {
        super(context);
    }

    public CalendarCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CalendarCellView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSelectable(boolean isSelectable) {
        this.isSelectable = isSelectable;
        refreshDrawableState();
    }

    public void setCurrentMonth(boolean isCurrentMonth) {
        this.isCurrentMonth = isCurrentMonth;
        refreshDrawableState();
    }

    public void setToday(boolean isToday) {
        this.isToday = isToday;
        refreshDrawableState();
    }

    public void setRangeState(MonthCellDescriptor.RangeState rangeState) {
        this.rangeState = rangeState;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 4);

        if (isSelectable) {
            mergeDrawableStates(drawableState, STATE_SELECTABLE);
        }

        if (isCurrentMonth) {
            mergeDrawableStates(drawableState, STATE_CURRENT_MONTH);
        }

        if (isToday) {
            mergeDrawableStates(drawableState, STATE_TODAY);
        }

        if (rangeState == MonthCellDescriptor.RangeState.FIRST) {
            mergeDrawableStates(drawableState, STATE_RANGE_FIRST);
        } else if (rangeState == MonthCellDescriptor.RangeState.MIDDLE) {
            mergeDrawableStates(drawableState, STATE_RANGE_MIDDLE);
        } else if (rangeState == RangeState.LAST) {
            mergeDrawableStates(drawableState, STATE_RANGE_LAST);
        }

        return drawableState;
    }
}
