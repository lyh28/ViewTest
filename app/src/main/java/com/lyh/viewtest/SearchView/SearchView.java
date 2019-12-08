package com.lyh.viewtest.SearchView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;

import com.lyh.viewtest.R;


public class SearchView extends ViewGroup {
    private static final String TAG = "ServiceView";
    //基本属性
    private int r;              //小的圆的半径
    private int padding;        //间隔
    private int viewH;
    private int viewW;
    private int centerX;
    private int centerY;


    private EditText childView;     //子view
    //图片
    private Bitmap searchImg;       //查找图片
    //图片rect
    private Rect imgSrcRect;
    private RectF imgdesRect;
    //外矩形
    private RectF desRect;
    private Paint paint;
    //画笔
    private int paintStroke;        //画笔粗度
    private int yellowColor;
    //下划线
    private float lineY;            //下划线Y
    private float lineCurrX;        //当前值
    private float lineStartX;       //下划线X开始值
    private float lineEndX;         //下划线X终止值

    //动画
    private AnimatorSet animatorSet;
    private float animValue;

    private int state;
    private final int OPENING = 1;
    private final int CLOSE = 2;
    private final int DRAWLINE = 3;       //绘制下划线
    private final int OPEN = 5;

    //触摸
    private SearchTouch searchTouch;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if( !(context instanceof SearchTouch)){
            throw new RuntimeException("需要实现SearchTouch接口");
        }
        searchTouch=(SearchTouch)context;
        checkChild();
        initChild(context);
        init();
    }

    private void checkChild() {
        if (getChildCount() != 0)
            throw new RuntimeException("SearchView不能拥有子view");
    }



    private void initChild(Context context) {
        childView = new EditText(context);
        childView.setTextSize(12);
        childView.setBackground(null);
        childView.setVisibility(GONE);
        addView(childView);
    }

    private void init() {
        //属性设置
        viewH = 130;        // 高:宽= 1:6
        viewW = 800;
        padding = 20;
        paintStroke = 7;
        r = viewH / 2 - padding - paintStroke;
        yellowColor = Color.rgb(255, 212, 108);

        state = CLOSE;


        scaleImg();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(paintStroke);
    }

    //缩小图片比例
    private void scaleImg() {
        searchImg = BitmapFactory.decodeResource(getResources(), R.drawable.search);
        imgSrcRect=new Rect(0,0,searchImg.getWidth(),searchImg.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);

        childView.measure(getMeasuredWidth()/2+viewH/2- 2 * r - padding * 2,viewH-4*padding-2*paintStroke);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top=centerY-viewH/2+paintStroke;
        Log.d(TAG, "onLayout: "+top+"  "+centerX+"  ");
        childView.layout((int)lineStartX,top,(int)lineEndX,top+childView.getMeasuredHeight());
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //基本属性设置
        centerX = w / 2;
        centerY = h / 2;
        //外矩形设置
        desRect = new RectF(centerX - r - padding-paintStroke, centerY - viewH / 2, centerX + r + padding+paintStroke, centerY + viewH / 2);
        Log.d(TAG, "onSizeChanged: "+desRect.top);
        //图片
        imgdesRect=new RectF(centerX-r,desRect.bottom - padding - paintStroke - 2 * r,centerX+r,desRect.bottom - padding - paintStroke);
        //下划线
        lineY = desRect.bottom - padding;
        lineStartX = centerX-viewW/2 + padding;
        lineCurrX = lineStartX;
        lineEndX = centerX+viewW/2- 2 * r - padding * 2;

        setAnimSet();
    }

    //组合动画
    private void setAnimSet() {
        animatorSet = new AnimatorSet();
        animatorSet.playSequentially(setOpenAnim(), setLineAnim());
    }

    private void startOpenAnimSet() {
        animatorSet.start();
    }

    //展开动画
    private ValueAnimator setOpenAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate: "+(float) animation.getAnimatedValue());
                animValue = (float) animation.getAnimatedValue();
                //更新图片位置
                UpdateSearchPosByState();
                //更新外矩形位置
                UpdateRectF();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                state = DRAWLINE;
            }
        });
        return valueAnimator;
    }

    //绘制下划线动画
    private ValueAnimator setLineAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(lineStartX, lineEndX);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.d(TAG, "onAnimationUpdate: 更新下划线  "+(float) animation.getAnimatedValue());
                lineCurrX = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                state = OPEN;
                childView.setVisibility(VISIBLE);
            }
        });
        return valueAnimator;
    }

    public void startOpen() {
        state = OPENING;
        startOpenAnimSet();
    }

    public void startClose() {
        state = CLOSE;
        //startOpenAnim();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawOwn(canvas);
        super.dispatchDraw(canvas);
    }

    private void drawOwn(Canvas canvas) {
        //更新图片位置
        UpdateSearchPosByState();
        //更新外矩形位置
        UpdateRectF();
        paint.setColor(Color.WHITE);
        float RXY= getViewRXY();

        canvas.drawRoundRect(desRect, RXY, RXY, paint);
        //绘制图片
        canvas.drawBitmap(searchImg, imgSrcRect, imgdesRect, paint);
        if (state == DRAWLINE || state == OPEN) {
            //根据动画值绘制下划线
            paint.setColor(yellowColor);
            Log.d(TAG, "drawOwn: "+lineStartX+"  "+lineCurrX);
            canvas.drawLine(lineStartX, lineY, lineCurrX, lineY, paint);
        }
    }

    //得到rxy
    private float getViewRXY() {
        return -(r + padding+paintStroke) * animValue + r + padding+paintStroke;
    }

    //计算矩形
    private void UpdateRectF() {
        float offset = (viewW / 2 - r - padding-paintStroke) * animValue;
        desRect.left = centerX - r - offset - padding-paintStroke;
        desRect.right = centerX + r + offset + padding+paintStroke;
    }

    //根据anim返回search的位置
    private void UpdateSearchPosByState() {
        imgdesRect.left=centerX - r + animValue * (viewW / 2 - padding - r);
        imgdesRect.right=centerX + r + animValue * (viewW / 2 - padding - r);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!animatorSet.isRunning() && isTouchBystate(event)) {
                    startTouchByState();
                }
                break;
        }
        return true;
    }

    //根据state判断有无点击
    private boolean isTouchBystate(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= imgdesRect.left && x <= imgdesRect.right * r && y >= imgdesRect.top && y <= imgdesRect.bottom)
            return true;
        return false;
    }


    //根据state启动响应事件
    private void startTouchByState() {
        if (searchTouch == null) return;
        if (state == OPEN) {
            Log.d(TAG, "startAnimByState: 搜索点击");
            searchTouch.SearchTouch();
        } else {
            Log.d(TAG, "startAnimByState: 打开点击");
            searchTouch.openTouch();
        }
    }

    public interface SearchTouch {
        void openTouch();

        void SearchTouch();
    }
}
