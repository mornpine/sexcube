package com.pashkov.sexcubefull.view_pager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.pashkov.sexcubefull.R;

public class ForegroundRelativeLayout extends RelativeLayout {
    protected Drawable mForegroundDrawable;

    public ForegroundRelativeLayout(Context context) {
        super(context);
    }

    public ForegroundRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ForegroundRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ForegroundRelativeLayout,
                defStyle, 0);
        try {
            if (a != null) {
                mForegroundDrawable = a.getDrawable(R.styleable
                        .ForegroundRelativeLayout_android_foreground);
            }
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForegroundDrawable != null) {
            mForegroundDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldwidth, int oldheight) {
        super.onSizeChanged(width, height, oldwidth, oldheight);
        if (mForegroundDrawable != null) {
            mForegroundDrawable.setBounds(0, 0, width, height);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mForegroundDrawable != null) {
            mForegroundDrawable.draw(canvas);
        }
    }
}
