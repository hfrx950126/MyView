package example.com.kuxuan.mycanander.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import example.com.kuxuan.mycanander.R;


/**
 * @author huangfuruixin
 * @date 2018/4/9
 */

public class TouchPullView extends View {
    private static final String TAG = "TouchPullView";
    //圆的画笔
    private Paint mCirclePaint;
    //圆的半径
    private float mCircleRadius = 50;
    private float mCirclePointX, mCirclePointY;

    //可拖动的高度
    private int mDragHeight = 300;

    //进度值
    private float mProgress;
    //目标宽度
    private int mTargetWidth = 400;
    //贝塞尔曲线的路径以及画笔
    private Path mPath = new Path();
    private Paint mPathPaint;
    //重心点最终高度，决定控制点的Y坐标
    private int mTargetGravityHeight = 10;
    //角度变换 0~135度
    private int mTangentAngle = 105;
    private Interpolator mProgressInterpolator = new DecelerateInterpolator();

    private Interpolator mTanentAngleInterpolator;
    private ValueAnimator valueAnimator;

    private Drawable mContent = null;

    private int mContentMargin = 0;

    public TouchPullView(Context context) {
        this(context, null);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchPullView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化方法
     *
     * @param attrs
     */
    private void init(AttributeSet attrs) {

        final Context context = getContext();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TouchPullView, 0, 0);
        int color = array.getColor(R.styleable.TouchPullView_pColor, 0x20000000);
        mCircleRadius = array.getDimension(R.styleable.TouchPullView_pRadius, mCircleRadius);
        mDragHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pDragHeight, mDragHeight);
        mTangentAngle = array.getInteger(R.styleable.TouchPullView_pTangentAngle, mTangentAngle);
        mTargetWidth = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetWidth, mTargetWidth);
        mTargetGravityHeight = array.getDimensionPixelOffset(R.styleable.TouchPullView_pTargetGravityHeight, mTargetGravityHeight);
        mContent = array.getDrawable(R.styleable.TouchPullView_pContentDrawable);
        mContentMargin = array.getDimensionPixelOffset(R.styleable.TouchPullView_pContentDrawableMargin, 0);

        array.recycle();

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        //设置抗锯齿
        p.setAntiAlias(true);
        //设置填充方式
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xFFFF4081);
        //设置防抖动
        p.setDither(true);
        mCirclePaint = p;

        //初始化路径部分画笔
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setDither(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xFFFF4081);

        mPathPaint = p;


        //切角路径差值器
        mTanentAngleInterpolator = PathInterpolatorCompat.create((mCircleRadius * 2.0f) / mDragHeight, 90.0f / mTangentAngle);
    }

    /**
     * 当大小改变时触发
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //当高度变化是进行路径更新
        updatePathLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //进行基础坐标参数系改变
        int count = canvas.save();
        float tranX = (getWidth() - getValueByLine(getWidth(), mTargetWidth, mProgress)) / 2;
        canvas.translate(tranX, 0);

        //画贝塞尔曲线
        canvas.drawPath(mPath, mPathPaint);


        //画圆
        canvas.drawCircle(mCirclePointX, mCirclePointY, mCircleRadius, mCirclePaint);

        Drawable drawable = mContent;
        if (drawable != null) {
            canvas.save();
            //剪切矩形区域
            canvas.clipRect(drawable.getBounds());
            //绘制Drawable
            drawable.draw(canvas);
            canvas.restore();
        }
        canvas.restoreToCount(count);
    }

    /**
     * 当进行测量时触发
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //宽度的类型
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //高度的类型
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int iHeight = (int) ((mDragHeight * mProgress + 0.5f) + getPaddingTop() + getPaddingBottom());

        int iWidth = (int) (2 * mCircleRadius + getPaddingRight() + getPaddingLeft());

        int measureWidth, measureHeight;

        if (widthMode == MeasureSpec.EXACTLY) {
            //确切的
            measureWidth = width;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //最多
            measureWidth = Math.min(iWidth, width);
        } else {
            measureWidth = iWidth;
        }


        if (height == MeasureSpec.EXACTLY) {
            //确切的
            measureHeight = height;
        } else if (height == MeasureSpec.AT_MOST) {
            //最多
            measureHeight = Math.min(iHeight, height);
        } else {
            measureHeight = iHeight;
        }
        //设置测量的高度宽度
        setMeasuredDimension(measureWidth, measureHeight);

    }

    /**
     * 更新我们的路径等相关操作
     */
    private void updatePathLayout() {
        //获取进度
        final float progress = mProgressInterpolator.getInterpolation(mProgress);

        //重置
        //获取可绘制区域高度宽度
        final float w = getValueByLine(getWidth(), mTargetWidth, mProgress);
        final float h = getValueByLine(0, mDragHeight, mProgress);
        //X对称轴的参数，圆的圆心X
        final float cPointx = w / 2;
        //圆的半径
        final float cRadius = mCircleRadius;
        //圆的圆心Y坐标
        final float cPointy = h - cRadius;
        //控制点结束Y的值
        final float endControlY = mTargetGravityHeight;

        //更新圆的坐标
        mCirclePointX = cPointx;
        mCirclePointY = cPointy;

        //路径
        final Path path = mPath;
        //复位
        path.reset();
        path.moveTo(0, 0);

        //左边部分的结束点和控制点
        float lEndPointX, lEndPointY;
        float lControlPointx, lControlPointY;

        //获取当前切线的弧度
        float angle = mTangentAngle * mTanentAngleInterpolator.getInterpolation(progress);
        double radian = Math.toRadians(angle);
        float x = (float) (Math.sin(radian) * cRadius);
        float y = (float) (Math.cos(radian) * cRadius);

        lEndPointX = cPointx - x;
        lEndPointY = cPointy + y;

        // 控制点的Y坐标变化
        lControlPointY = getValueByLine(0, endControlY, progress);
        //控制点之间与结束点之间的高度
        float tHeight = lEndPointY - lControlPointY;
        float tWidth = (float) (tHeight / Math.tan(radian));
        lControlPointx = lEndPointX - tWidth;


        //贝塞尔曲线
        path.quadTo(lControlPointx, lControlPointY, lEndPointX, lEndPointY);
        //链接到右边
        path.lineTo(cPointx + (cPointx - lEndPointX), lEndPointY);
        //画右边的贝塞尔曲线
        path.quadTo(cPointx + cPointx - lControlPointx, lControlPointY, w, 0);
        //更新内容部分
        updateContetLayout(cPointx, cPointy, cRadius);
    }

    /**
     * 获取当前值
     *
     * @param start    起始值
     * @param end      结束值
     * @param progress 进度
     * @return 当前进度值
     */
    private float getValueByLine(float start, float end, float progress) {
        return start + (end - start) * progress;
    }

    private void updateContetLayout(float cx, float cy, float radius) {
        Drawable drawable = mContent;
        if(drawable !=null){
            int margin = mContentMargin;
            int l = (int) (cx-radius+margin);
            int r = (int) (cx+radius-margin);
            int t = (int) (cy-radius+margin);
            int b = (int) (cy+radius-margin);
            drawable.setBounds(l,t,r,b);
        }
    }

    /**
     * 添加释放操作
     */
    public void release() {
        if (valueAnimator == null) {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, 0f);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(400);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object val = animation.getAnimatedValue();
                    if (val instanceof Float) {
                        setProgress((Float) val);
                    }
                }
            });
            valueAnimator = animator;
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(mProgress, 0f);
        }
        valueAnimator.start();
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(float progress) {
        Log.e(TAG, "P:" + progress);
        mProgress = progress;
        //请求重新就行测量
        requestLayout();
    }
}
