package com.lyh.viewtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class TestAnimView extends View implements View.OnTouchListener {
    private static final String TAG = "TestAnimView";
    private int padding;    //间隔值
    private int r;          //半径
    private int mover;      //移动圆的半径
    private int moveindex;      //移动圆形的序号
    private int centerx;        //中心点
    private int centery;        //中心点
    private int color;        //颜色
    private AnimatorSet animatorSet;
    private Paint paint;        //画笔
    private MoveCircle moveCircle;
    private Circle[] circles;
    public TestAnimView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        //初始化属性
        init();
        setAnim();
    }

    private void init(){
        padding=10;
        r=40;
        mover=40;
        moveindex=0;
        centerx=300;
        centery=700;
        Log.d(TAG, "init: "+centerx+"  "+centery);
        paint=new Paint();
        moveCircle=new MoveCircle();
        circles=new Circle[9];
        color=Color.BLUE;
        paint.setColor(color);
        initCircle();
        setMoveCircleData();
    }
    private void initCircle(){
        for(int i=0;i<9;i++)
            circles[i]=new Circle(i);
        //连接Circle中的next
        connectCircle();
        for(int i=0;i<9;i++){
            Circle next=circles[i].next;
            if(next!=null)
                Log.d(TAG, "initCircle: "+i+"  "+next.getIndex());
            else
                Log.d(TAG, "initCircle: "+i+"  为空");
        }
    }

    private void connectCircle(){
        //第一行
        for(int i=0;i<3;i++){
            if(circles[i].next==null){
                //判断最左，最右还是中间
                switch (i){
                    case 0:
                        circles[i].next=circles[i+3];   break;
                    case 2:
                        circles[i].next=circles[i-1];   break;
                        default:
                            circles[i].next=circles[i-1];   break;
                }
            }
        }
        //最后一行
        for(int i=6;i<9;i++){
            if(circles[i].next==null){
                //判断最左，最右还是中间
                switch (i%3){
                    case 0:
                        circles[i].next=circles[i+1];   break;
                    case 2:
                        circles[i].next=circles[i-3];   break;
                    default:
                        circles[i].next=circles[i+1];   break;
                }
            }
        }
        //第一列
        for(int i=0;i<2*3+1;i+=3){
            if(circles[i].next==null){
                //判断最上，最下还是中间
                switch (i/3){
                    case 0:
                        circles[i].next=circles[i+3];   break;
                    case 2:
                        circles[i].next=circles[i+1];   break;
                    default:
                        circles[i].next=circles[i+3];   break;
                }
            }
        }
        //最后一列
        for(int i=3-1;i<9;i+=3){
            if(circles[i].next==null){
                //判断最上，最下还是中间
                switch (i/3){
                    case 0:
                        circles[i].next=circles[i-1];   break;
                    case 2:
                        circles[i].next=circles[i-3];   break;
                    default:
                        circles[i].next=circles[i-3];   break;
                }
            }
        }
    }
    //内部类  圆形
    class Circle{
        private Circle next;
        private boolean show;
        private int index;
        private int x;
        private int y;
        public Circle(int index) {
            this.index = index;
            next=null;
            x=getXByIndex(index);
            y=getYByIndex(index);
        }

        public Circle getNext() {
            return next;
        }

        public void setNext(Circle next) {
            this.next = next;
        }
        public boolean isShow() {
            return show;
        }
        public void setShow(boolean show) {
            this.show = show;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    //内部类  移动的圆形
    class MoveCircle{
        private int index;
        private int currentx;
        private int currenty;
        private int tox;
        private int toy;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getCurrentx() {
            return currentx;
        }

        public void setCurrentx(int currentx) {
            this.currentx = currentx;
            Log.d(TAG, "setCurrentx: "+currentx);
            invalidate();
        }

        public int getCurrenty() {
            return currenty;
        }

        public void setCurrenty(int currenty) {
            this.currenty = currenty;
            Log.d(TAG, "setCurrenty: "+currenty);
        }

        public int getTox() {
            return tox;
        }

        public void setTox(int tox) {
            this.tox = tox;
        }

        public int getToy() {
            return toy;
        }

        public void setToy(int toy) {
            this.toy = toy;
        }
    }
    //根据index得到xy坐标
    private int getXByIndex(int i){
        switch (i%3){
            case 0:
                return centerx-padding-2*r;
            case 1:
                return centerx;
            case 2:
                return centerx+padding+2*r;
                default:
                    return centerx;
        }
    }
    private int getYByIndex(int i){
        switch (i/3){
            case 0:
                return centery-padding-2*r;
            case 1:
                return centery;
            case 2:
                return centery+padding+2*r;
            default:
                return centery;
        }
    }
    //为moveCircle设值
    private void setMoveCircleData(){
        Circle owncircle=circles[moveindex];
        Circle nextcircle=circles[moveindex].next;
        moveCircle.setCurrentx(owncircle.x);
        moveCircle.setCurrenty(owncircle.y);
        moveCircle.setTox(nextcircle.x);
        moveCircle.setToy(nextcircle.y);

    }
    //设置组合动画
    private void setAnim(){
        ValueAnimator translateAnima=createTranslateAnim();
        ValueAnimator changeColorAnima=createChangeColorAnim();
        animatorSet=new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "onAnimationEnd: 动画结束回调");
                updateMoveindex();
                //更新移动方块
                setMoveCircleData();
                setAnim();
            }

            @Override
            public void onAnimationStart(Animator animation) {

                Log.d(TAG, "onAnimationStart: 动画开始回调");
                setMoveCircleData();
                //隐藏对应的circle
                int next=circles[moveindex].next.getIndex();
                for(int i=0;i<9;i++){
                    if(i==moveindex||i==next)   circles[i].setShow(false);
                    else
                        circles[i].setShow(true);
                }
            }
        });
        animatorSet.playTogether(translateAnima,changeColorAnima);
        animatorSet.start();
    }
    //设置平移动画
    private ValueAnimator createTranslateAnim(){
        //x的动画
        PropertyValuesHolder xHolder=PropertyValuesHolder.ofInt("x",moveCircle.getCurrentx(),moveCircle.getTox());
        //y的动画
        PropertyValuesHolder yHolder=PropertyValuesHolder.ofInt("y",moveCircle.getCurrenty(),moveCircle.getToy());
        ValueAnimator animator=ValueAnimator.ofPropertyValuesHolder(xHolder,yHolder);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int x=(int)animation.getAnimatedValue("x");
                int y=(int)animation.getAnimatedValue("y");
                moveCircle.setCurrentx(x);
                moveCircle.setCurrenty(y);
                invalidate();
            }
        });
        animator.setDuration(2000);
        return animator;
    }
    //设置缩放动画
    private ValueAnimator createChangeColorAnim(){
        ObjectAnimator valueAnimator=new ObjectAnimator();
        PropertyValuesHolder propertyValuesHolder=PropertyValuesHolder.ofInt("mover",40,20,40);
        valueAnimator.setValues(propertyValuesHolder);
        valueAnimator.setTarget(this);
        valueAnimator.setDuration(2000);
        /*valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int newr=(int)animation.getAnimatedValue("r");
                mover=newr;
                invalidate();
            }
        });*/
        return valueAnimator;
    }
    //更新moveindex
    private void updateMoveindex(){
        Log.d(TAG, "updateMoveindex: "+moveindex);
        switch (moveindex){
            case 1:
            case 0:
                moveindex=moveindex+1; break;
            case 2:
            case 5:
                moveindex=moveindex+3;  break;
            case 7:
            case 8:
                moveindex=moveindex-1;  break;
            case 3:
            case 6:
                moveindex=moveindex-3;  break;
        }
        Log.d(TAG, "updateMoveindex: "+moveindex);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawColor(Color.WHITE);
        //绘制圆形  根据属性值show

        //绘制固定圆形
        for(int i=0;i<9;i++){
            if(circles[i].isShow()) canvas.drawCircle(circles[i].getX(),circles[i].getY(),r,paint);
        }
        //绘制移动圆形
        canvas.drawCircle(moveCircle.getCurrentx(),moveCircle.getCurrenty(),mover,paint);
    }

    public int getMover() {
        return mover;
    }

    public void setMover(int mover) {
        this.mover = mover;
        invalidate();

    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

    }
    private int getMeasure(int MeasureSpec){

    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
