package com.went_gone.mobilesafe.ui;
import java.util.ArrayList;
import java.util.List;

import com.nineoldandroids.view.ViewHelper;
import com.went_gone.mobilesafe.R;
import com.went_gone.mobilesafe.adapter.CommonAdapter;
import com.went_gone.mobilesafe.adapter.ViewHolder;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


/**
 * Created by Went_Gone on 2016/1/26.
 */
public class SlidingMenu extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    private int mMenuWidth;
    private Context context;
    private GridView mGridView;


    //dp
    private int mMenuRightPadding = 50;

    private boolean once;//判断执行一次

    private boolean isOpean;//是不是打开状态


    public SlidingMenu(Context context) {
        this(context, null);
    }

    /**
     * 未使用自定义属性时，调用
     *
     * @author Went_Gone
     * @mail imwent_gone@yeah.net
     * created at 2016/1/26 10:26
     **/
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当使用了自定义属性时，会调用次构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        //获得我们定义的属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenu, defStyleAttr, 0);
        int n = a.getIndexCount();//获取属性数量
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.SlidingMenu_rightpadding:
                    mMenuRightPadding = a.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(
                                    TypedValue.COMPLEX_UNIT_DIP, 50, context
                                            .getResources().getDisplayMetrics()));//设置默认值，不写的时候的默认值
                    break;
                default:
                    break;
            }
        }
        a.recycle();//使用完TypeArray需要释放一下


        //获得屏幕的宽   首先获得windowManager，创建一个DisplayMetrics对象，然后通过getDefaultDisplay给DisplayMetrics对象赋值
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        //将dp转换成px
//        mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());
    }


    /**
     * 设置子View的宽和高
     * 设置自己的宽和高
     *
     * @author Went_Gone
     * @mail imwent_gone@yeah.net
     * created at 2016/1/26 10:36
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 通过设置偏移量，jiangMenu隐藏
     *
     * @author Went_Gone
     * @mail imwent_gone@yeah.net
     * created at 2016/1/26 10:51
     **/
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

//        this.scrollTo(int x,int y);//x为正值时，滚动条向右移动，内容区域向左移动
        if (changed) {
            this.scrollTo(mMenuWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                //隐藏在左边的宽度
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);//缓慢的隐藏
                    isOpean = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpean = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }
    /**
     *打开菜单
     *@author Went_Gone
     *@mail imwent_gone@yeah.net
     *created at 2016/1/26 11:45
     **/
    public void opeanMenu(){
        if (isOpean)
            return;
        this.smoothScrollTo(0,0);
        isOpean = true;
    }
    /**
     *关闭菜单
     *@author Went_Gone
     *@mail imwent_gone@yeah.net
     *created at 2016/1/26 11:47
     **/
    public void closeMenu(){
        if (!isOpean)
            return;
        this.smoothScrollTo(mMenuWidth,0);
        isOpean = false;
    }

    /**
     *切换菜单的方法
     *@author Went_Gone
     *@mail imwent_gone@yeah.net
     *created at 2016/1/26 11:48
     **/
    public void toggle(){
        if (isOpean){
            closeMenu();
        }else {
            opeanMenu();
        }
    }

    /**
     * 滚动发生时
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scall = l * 1.0f / mMenuWidth;//梯度0~1

        //调用属性动画，设置TranslationX
        ViewHelper.setTranslationX(mMenu,mMenuWidth*scall);
    }
}
