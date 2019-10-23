package com.sds.xr.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class StateLinearLayout extends LinearLayout implements IDrawableStateObservable {
    private OnDrawableStateChanged mOnDrawableStateChanged;

    public StateLinearLayout(Context context) {
        super(context);
    }

    public StateLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public StateLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OnDrawableStateChanged getOnDrawableStateChanged() {
        return mOnDrawableStateChanged;
    }

    public void setOnDrawableStateChanged(OnDrawableStateChanged onDrawableStateChanged) {
        this.mOnDrawableStateChanged = onDrawableStateChanged;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mOnDrawableStateChanged != null) {
            mOnDrawableStateChanged.onDrawableStateChanged(this, getDrawableState());
        }
    }
}