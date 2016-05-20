package com.went_gone.mobilesafe.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 圆形进度条
 * Created by Went_Gone on 2015/9/16.
 */
public class ProgressBarCircle extends View{
    private int width;
    private int height;
    private Paint mPaintCircleBackGround;
    private Paint mPaintCircleCurrent;
    private Paint mPaintText;
    private int maxProgress = 100;
    private int currentProgress;

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }

    public ProgressBarCircle(Context context) {
        super(context);
    }

    public ProgressBarCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaintCircleBackGround = new Paint();
        mPaintCircleBackGround.setColor(Color.GRAY);
        mPaintCircleBackGround.setStrokeWidth(300);
        mPaintCircleBackGround.setAntiAlias(true);

        mPaintCircleCurrent = new Paint();
        mPaintCircleCurrent.setColor(Color.GREEN);
        mPaintCircleCurrent.setStrokeWidth(0);
        mPaintCircleCurrent.setAntiAlias(true);

        mPaintText = new Paint();
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(100);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(width/2,height/2,300,mPaintCircleBackGround);
        canvas.drawCircle(width/2,height/2,currentProgress*300/maxProgress,mPaintCircleCurrent);
        canvas.drawText(currentProgress*100f/maxProgress+"%",width/2,height/2,mPaintText);
    }
}
