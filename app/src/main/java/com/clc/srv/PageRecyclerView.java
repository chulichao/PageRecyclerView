package com.clc.srv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PageRecyclerView extends RecyclerView {
    private static final String TAG = "PageRecyclerView";
    public static final int PAGE_WIDTH = 1000;
    public static final int DISTANCE_LIMIT = 300;
    public static final float SCROLL_CRITICAL_SPEED = 1000f;
    private static final int TO_SECOND = 1000;
    private Scroller mScroller;
    private int mDownX;
    private long mDownTime;
    private int mScrollX;
    private PageChangedListener mPageChangedListener;

    public int gainScrollX() {
        return mScrollX;
    }

    public void setPageChangedListener(PageChangedListener pageChangedListener) {
        mPageChangedListener = pageChangedListener;
    }
    public PageRecyclerView(@NonNull Context context) {
        super(context);
        mScroller = new Scroller(context);
    }

    public PageRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        return super.fling(0, 0);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        mScrollX = mScrollX + dx;
        Log.d(TAG, "onScrolled: mScrollX = " + mScrollX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**
         * 记录mDownX，以判断滑动方向
         */
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDownX = gainScrollX();
            mDownTime = System.currentTimeMillis();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int currX = gainScrollX();
            int finalX = findFinalX(currX);
            Log.d(TAG, "currX = " + currX + ", finalX = " + finalX);
/*            mScroller.startScroll(currX, 0, finalX - currX, 0);
            invalidate();*/
            smoothScrollTo(finalX);
            if (mPageChangedListener != null) {
                mPageChangedListener.onPageChanged(finalX / PAGE_WIDTH + 1);
            }
        }
        return super.onTouchEvent(event);
    }

    private void smoothScrollTo(int finalX) {
        if (gainScrollX() < finalX) {
            scrollBy(1, 0);
        } else if (gainScrollX() > finalX) {
            scrollBy(-1, 0);
        } else {
            //递归出口
            return;
        }
        invalidate();
        smoothScrollTo(finalX);
    }

    /**
     * 获取scroll终点（较为通用）
     */
    private int findFinalX(int currX) {
        int remainder = currX % PAGE_WIDTH;
        int multiple = currX / PAGE_WIDTH;
        float speed = (currX - mDownX) * TO_SECOND / (System.currentTimeMillis() - mDownTime);
        Log.d(TAG, "scroll speed = " + speed);
        if (speed < SCROLL_CRITICAL_SPEED && speed > -SCROLL_CRITICAL_SPEED) {
            //滑动速度慢，才会判断距离
            if (remainder < DISTANCE_LIMIT) {
                return PAGE_WIDTH * multiple;
            }
            if (remainder > PAGE_WIDTH - DISTANCE_LIMIT) {
                return PAGE_WIDTH * (multiple + 1);
            }
        }

        /**
         * 滑动速度快，直接走以下步骤
         */
        if (currX > mDownX) {
            return PAGE_WIDTH * (multiple + 1);
        } else {
            return PAGE_WIDTH * (multiple);
        }
    }

/*    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll: ");
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "mScroller.getCurrX() = " + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }*/

    public interface PageChangedListener {
        void onPageChanged(int pageNum);
    }
}
