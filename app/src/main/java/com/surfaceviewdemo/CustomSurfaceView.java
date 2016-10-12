package com.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Sunmeng on 10/12/2016.
 * E-Mail:Sunmeng1995@outlook.com
 * Description:
 */

public class CustomSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

    private final static String DEFAULT_DBNAME = "CustomSurfaceView";

    private SurfaceHolder mSurfaceHolder;//控制SurfaceView
    private Canvas mCanvas;
    private boolean isDrawing;

    private Paint mPaint;// 画笔
    private Path mPath;// 路径
    private float mLastX, mLastY;//上次的坐标

    public CustomSurfaceView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();//得到SurfaceHolder对象
        mSurfaceHolder.addCallback(this);//注册SurfaceHolder
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);//保持屏幕长亮
        //画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(10f);
        mPaint.setColor(Color.parseColor("#FF4081"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);//画笔接洽点类型 如影响矩形但角的外轮廓
        mPaint.setStrokeCap(Paint.Cap.ROUND);//画笔笔刷类型 如影响画笔但始末端
        //路径
        mPath = new Path();

    }

    /**
     * 创建
     * */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isDrawing=true;
        new Thread(this).start();
    }

    /**
     * 改变
     * */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * 销毁
     * */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isDrawing=false;
    }

    @Override
    public void run() {
        while (isDrawing){
            drawing();
        }
    }

    /**
     * 绘制
     * */
    private void drawing() {
        try {
            mCanvas=mSurfaceHolder.lockCanvas();
            if(null==mCanvas)
                return;
            mCanvas.drawColor(getContext().getResources().getColor(R.color.colorPrimary));
            mCanvas.drawPath(mPath,mPaint);
        } finally {
            if(mCanvas!=null){
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);//提交画布
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX=x;
                mLastY=y;
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x - mLastX);
                float dy = Math.abs(y - mLastY);
                if (dx >= 3 || dy >= 3) {
                    mPath.quadTo(mLastX, mLastY, (mLastX + x) / 2, (mLastY + y) / 2);
                }
                mLastX = x;
                mLastY = y;
                break;
        }
        return true;
    }

    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, hSpecSize);
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize, 300);
        }
    }

}
