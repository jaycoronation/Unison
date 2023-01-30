package com.unisonpharmaceuticals.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListView;

public class BottomSheetListView extends ListView {

    public BottomSheetListView(Context context, AttributeSet p_attrs)
    {
        super(context, p_attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (canScrollVertically(this))
        {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (canScrollVertically(this))
        {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }

    public boolean canScrollVertically(AbsListView view)
    {
        boolean canScroll = false;

        if (view != null && view.getChildCount() > 0)
        {
            boolean isOnTop = view.getFirstVisiblePosition() != 0 || view.getChildAt(0).getTop() != 0;
            boolean isAllItemsVisible = isOnTop && view.getLastVisiblePosition() == view.getChildCount();

            if (isOnTop || isAllItemsVisible)
            {
                canScroll = true;
            }
        }

        return canScroll;
    }


   /* public BottomSheetListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        View view = (View) getChildAt(getChildCount() - 1);

        int diffBottom = (view.getBottom() - (getHeight() + getScrollY()));
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            if (diffBottom == 0) {
                return false;
            }
        }

        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (canScrollVertically(this)) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean canScrollVertically(AbsListView absListView) {

        boolean canScroll = false;

        if (absListView != null && absListView.getChildCount() > 0) {

            boolean isOnTop = absListView.getFirstVisiblePosition() != 0 || absListView.getChildAt(0).getTop() != 0;
            boolean isAllItemsVisible = isOnTop && getLastVisiblePosition() == absListView.getChildCount();

            if (isOnTop || isAllItemsVisible)
                canScroll = true;
        }

        return canScroll;
    }*/
}