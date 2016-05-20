package com.went_gone.mobilesafe.ui;

import com.went_gone.mobilesafe.R;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 弧形进度条
 * Created by Went_Gone on 2015/9/16.
 */
public class ProgressBarArc extends View{
    private int width;
    private int height;
    private int radius;
    private RectF rectF;
    private Paint mPaintCirecle;//画圆的画笔
    private Paint mPaintArc;//画弧线的画笔
    private Paint mPaintText;
    private long maxProgress;
    private long currentProgress;

	public long getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(long maxProgress) {
		this.maxProgress = maxProgress;
	}

	public long getCurrentProgress() {
		return currentProgress;
	}

	public void setCurrentProgress(long currentProgress) {
		this.currentProgress = currentProgress;
		invalidate();
	}

    public ProgressBarArc(Context context) {
        super(context);
    }

    public ProgressBarArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintCirecle = new Paint();
        mPaintCirecle.setAntiAlias(true);
        mPaintCirecle.setStrokeWidth(20);
        mPaintCirecle.setStyle(Paint.Style.STROKE);
        mPaintCirecle.setColor(getResources().getColor(R.color.progress_arc_background));

        mPaintArc = new Paint();
        mPaintArc.setStrokeWidth(20);
        mPaintArc.setAntiAlias(true);
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setColor(getResources().getColor(R.color.progress_arc_pro));

        mPaintText = new Paint();
        mPaintText.setTextSize(80);
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.STROKE);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(Color.BLACK);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        /*width = 400;
        height = 400;*/
        setMeasuredDimension(width, height);
        rectF = new RectF();
//        int m = 150;
        radius = 150;
        rectF.left = width/2-radius;
        rectF.top = height/2-radius;
        rectF.right = width/2+radius;
        rectF.bottom = height/2+radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(width/2,height/2,300,mPaintCirecle);
        canvas.drawCircle(width/2,height/2,radius,mPaintCirecle);
        canvas.drawArc(rectF,-90,currentProgress*360/maxProgress,false,mPaintArc);
        canvas.drawText(currentProgress*100/maxProgress+"%",width/2,height/2,mPaintText);
    }
    
}
