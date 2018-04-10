package example.com.kuxuan.mycanander;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import example.com.kuxuan.mycanander.widget.TouchPullView;

public class TouchActivity extends AppCompatActivity {
    private static final float TOUCH_MOVE_MAX_Y = 600;
    private float mTouchMoveStartY = 0;
    private TouchPullView mTouchPullView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);


        mTouchPullView = findViewById(R.id.touchPull);


        findViewById(R.id.main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mTouchMoveStartY = event.getY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float y = event.getY();
                        if (y >= mTouchMoveStartY) {
                            float moveSize = y - mTouchMoveStartY;
                            float progress = moveSize >= TOUCH_MOVE_MAX_Y
                                    ? 1 : moveSize / TOUCH_MOVE_MAX_Y;
                            mTouchPullView.setProgress(progress);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        mTouchPullView.release();
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

    }
}
