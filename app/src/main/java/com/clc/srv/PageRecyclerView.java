package com.clc.srv;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PageRecyclerView extends RecyclerView {
    private static final String TAG = "PageRecyclerView";
    public static final int PAGE_WIDTH = 1000;
    public static final int DISTANCE_LIMIT = 200;
    public static final float SCROLL_CRITICAL_SPEED = 1000f;
    private static final int TO_SECOND = 1000;
    private Scroller mScroller;
    private int mDownX;
    private long mDownTime;
    private int mScrollX;
    private PageChangedListener mPageChangedListener;
    private int mOldX;

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
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = gainScrollX();
                mDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                int currX = gainScrollX();
                if (currX == mDownX) {
                    return super.dispatchTouchEvent(event);
                }
                int finalX = findFinalX(currX);
                Log.d(TAG, "currX = " + currX + ", finalX = " + finalX);
                mScroller.startScroll(currX, 0, finalX - currX, 0);
                invalidate();
                if (mPageChangedListener != null) {
                    mPageChangedListener.onPageChanged(finalX / PAGE_WIDTH + 1);
                }
                break;
            default:
                // Do nothing.
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 获取scroll终点（如果滑动临界是页宽的一半）
     */
    /*private int findFinalX(int currX) {
        int multiple = currX / DISTANCE_LIMIT;
        if ((multiple & 1) == 1) {
            return DISTANCE_LIMIT * (multiple + 1);
        } else {
            return DISTANCE_LIMIT * multiple;
        }
    }*/

    /**
     * 获取scroll终点（较为通用）
     */
    private int findFinalX(int currX) {
        int remainder = currX % PAGE_WIDTH;
        int multiple = currX / PAGE_WIDTH;
        float speed = (currX - mDownX) * TO_SECOND / (System.currentTimeMillis() - mDownTime);
        Log.d(TAG, "scroll speed = " + speed);
        if (currX > mDownX) {
            //往正方向滑
            if (speed > SCROLL_CRITICAL_SPEED) {
                //滑动速度快，直接翻页
                Log.d(TAG, "findFinalX: 1");
                return PAGE_WIDTH * (multiple + 1);
            } else {
                //滑动速度慢，则判断距离够不够
                if (remainder < DISTANCE_LIMIT) {
                    //距离不够，不翻页
                    return PAGE_WIDTH * multiple;
                } else {
                    //距离够，翻页
                    return PAGE_WIDTH * (multiple + 1);
                }
            }
        } else {
            //往负方向滑
            if (speed < -SCROLL_CRITICAL_SPEED) {
                //滑动速度快，直接翻页
                Log.d(TAG, "findFinalX: 4");
                return PAGE_WIDTH * (multiple);
            } else {
                //滑动速度慢，则判断距离够不够
                if (PAGE_WIDTH - remainder < DISTANCE_LIMIT) {
                    //距离不够，不翻页
                    return PAGE_WIDTH * (multiple + 1);
                } else {
                    //距离够，翻页
                    return PAGE_WIDTH * (multiple);
                }
            }
        }
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll: ");
        mOldX = gainScrollX();
        if (mScroller.computeScrollOffset()) {
            Log.d(TAG, "mScroller.getCurrX() = " + mScroller.getCurrX());
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * RecyclerView自己的scrollTo()是空方法
     */
    @Override
    public void scrollTo(int x, int y) {
        scrollBy(x - mOldX, 0);
    }

    public interface PageChangedListener {
        void onPageChanged(int pageNum);
    }
}
