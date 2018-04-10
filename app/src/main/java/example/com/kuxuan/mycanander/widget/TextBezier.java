package example.com.kuxuan.mycanander.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * @author huangfuruixin
 * @date 2018/4/10
 */

public class TextBezier extends View{
    public TextBezier(Context context) {
        this(context,null);

    }

    public TextBezier(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);

    }

    public TextBezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //画笔
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private void init(){


        Paint paint = mPaint;

        //抗锯齿
        paint.setAntiAlias(true);
        //抗抖动
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);

        //一阶贝塞尔曲线
        Path path = mPath;
        path.moveTo(100,100);
        path.lineTo(400,400);

        //二阶贝塞尔曲线
        path.quadTo(600,100,800,400);
        // 相对的实现
        //path.rQuadTo(200,-300,400,0);

         path.moveTo(400,800);
        //三阶贝塞尔曲线
        //path.cubicTo(500,600,700,1200,800,800);
        //相对的三阶贝塞尔
        path.rCubicTo(100,-200,300,400,400,0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
        canvas.drawPoint(600,100,mPaint);
        canvas.drawPoint(500,600,mPaint);
        canvas.drawPoint(700,1200,mPaint);
    }
}
