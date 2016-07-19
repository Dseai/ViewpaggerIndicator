package com.syn.viewpaggerindicator;

import android.content.Context;
import android.content.pm.ProviderInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 孙亚楠 on 2016/7/11.
 */
public class ViewpagerIndicator extends LinearLayout {
      private Paint mPaint;
    private Path mPath;
    private  int mTriangleWidth;
    private  int mTriangleHeight;
    private static final  float RADIO_TRIANGLE_WIDTH=1/6F;
    //三角形底边最大宽度
    private  final int DIMENSION_TRIANGLE_WIDTH_MAX=(int)(getScreenWidth()/3*RADIO_TRIANGLE_WIDTH);
    private int mInitTranslationX;
    private int mTranslationX;
    private int mTabVisibleCount;
    private  static final int COUNT_DEFAULT=4;
    public List<String> mTitles;
    private static  final  int  COLOR_TEXT_NOEMAL=0x77ffff;
    private static  final  int  COLOR_TEXT_HIGHLIGHT=0xFFffff;

    /**
     * 设置可见title数量
     * @param count
     */
   public  void setVisibleTabCount(int count){
       mTabVisibleCount=count;
   }

    public ViewpagerIndicator(Context context) {
        this(context,null);
    }

    public ViewpagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取可见tab的数量
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.ViewPagerIndator);
       mTabVisibleCount=a.getInt(R.styleable.ViewPagerIndator_visible_tab_count,COUNT_DEFAULT);
        if(mTabVisibleCount<0){
            mTabVisibleCount=COUNT_DEFAULT;
        }
        a.recycle();
        //初始化画笔
        mPaint=new Paint();
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));//圆角效果不至于太尖锐

    }
    protected void dispatchDraw(Canvas canvas){
        canvas.save();
        canvas.translate(mTranslationX+mInitTranslationX,getHeight()+2);
        canvas.restore();
        super.dispatchDraw(canvas);
    }
    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mTriangleWidth=(int)(w/mTabVisibleCount*RADIO_TRIANGLE_WIDTH);
        mTriangleWidth=Math.min(mTriangleWidth,DIMENSION_TRIANGLE_WIDTH_MAX);
        mInitTranslationX=(w/mTabVisibleCount/2-mTriangleWidth/2);
        initTriangle();

    }
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        int cCount=getChildCount();
        if(cCount==0) return;
        for(int i=0;i<cCount;i++){
            View view=getChildAt(i);
            LinearLayout.LayoutParams Ip=(LayoutParams)view.getLayoutParams();
            Ip.weight=0;
            Ip.width=getScreenWidth()/mTabVisibleCount;
            view.setLayoutParams(Ip);
        }
        setItemClickEvent();
    }

    private void initTriangle() {
        mTriangleHeight=mTriangleWidth/2;
        mPath=new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }
 //指示器跟随手指滚动
    public void scroll(int position, float positionOffset) {
        int tabWidth=getWidth()/mTabVisibleCount;
        mTranslationX=(int)(tabWidth*(positionOffset+position));
        //容器移动，在tab处于移动至最后一个时
        if(position>=(mTabVisibleCount-2)&&positionOffset>0&&getChildCount()>mTabVisibleCount){
            if(mTabVisibleCount!=1){
                this.scrollTo((position-(mTabVisibleCount-2))*tabWidth+(int)(tabWidth*positionOffset),0);

            }
            else {
                this.scrollTo(position*tabWidth+(int)(tabWidth*positionOffset),0);

            }

        }
        invalidate();//重绘

    }

    public  int getScreenWidth() {
        WindowManager wm= (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
    public  void setTabItemTitles(List<String> titles){
        if(titles!=null&&titles.size()>0){
            this.removeAllViews();
            mTitles=titles;
            for(String title:mTitles){
                addView(generateTextView(title));
            }
            setItemClickEvent();
        }
    }

    /**
     * 根据title创建tab
     * @param title
     * @return
     */
    private View generateTextView(String title){
        TextView tv=new TextView(getContext());
        LinearLayout.LayoutParams Ip=new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                Ip.width=getScreenWidth()/mTabVisibleCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        tv.setTextColor(COLOR_TEXT_NOEMAL);
        tv.setLayoutParams(Ip);
        return tv;

    }
    private ViewPager mViewPager;
    public interface PagerOnchangeListner{
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) ;
        public void onPageSelected(int position) ;
        public void onPageScrollStateChanged(int state);

    }
    public PagerOnchangeListner mListener;
    public  void setOnPageChangeListener(PagerOnchangeListner listener){
        this.mListener=listener;

    }

    /**
     * 设置关联的viewPager
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager,int pos){
        mViewPager=viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset
                scroll(position,positionOffset);
                if (mListener!=null){
                    mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
   if (mListener!=null){
       mListener.onPageSelected(position);
   }
                highlightTexteView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListener!=null){
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highlightTexteView(pos);
    }
    /**
     * 重置tab文本颜色
     */
    private void resetTextViewColor(){
        for(int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            if(view instanceof TextView){
                ((TextView)view).setTextColor(COLOR_TEXT_NOEMAL);
            }
        }

    }
    /**
     * 高亮某个tab的文本
     *
     * @param pos
     */
    private void highlightTexteView(int pos){
        resetTextViewColor();
        View view=getChildAt(pos);
        if(view instanceof TextView){
            ((TextView)view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /**
     * 设置tab点击事件
     */
    private void setItemClickEvent(){
        int cCount=getChildCount();
        for(int i=0;i<cCount;i++){
            final  int j=i;
            View view=getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                  mViewPager.setCurrentItem(j);
                }
            });
        }
    }





    }

