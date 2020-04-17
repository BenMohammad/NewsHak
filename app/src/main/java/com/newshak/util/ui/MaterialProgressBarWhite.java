package com.newshak.util.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import com.newshak.R;

public class MaterialProgressBarWhite extends View {

    private CircularProgressDrawable mCircleDrawable;

    public MaterialProgressBarWhite(Context context) {
        this(context, null);
    }

    public MaterialProgressBarWhite(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialProgressBarWhite(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int color = context.getResources().getColor(R.color.white);
        mCircleDrawable = new CircularProgressDrawable(color, 0);
        mCircleDrawable.setCallback(this);
        if(getVisibility() == VISIBLE) {
            mCircleDrawable.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mCircleDrawable != null) {
            mCircleDrawable.draw(canvas);
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(mCircleDrawable != null) {
            if(visibility == VISIBLE) {
                mCircleDrawable.start();
            } else {
                mCircleDrawable.stop();
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mCircleDrawable != null) {
            mCircleDrawable.setBounds(0, 0, w, h);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mCircleDrawable || super.verifyDrawable(who);
    }
}
