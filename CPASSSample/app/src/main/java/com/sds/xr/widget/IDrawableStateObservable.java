package com.sds.xr.widget;

import android.view.View;

public interface IDrawableStateObservable {
    public OnDrawableStateChanged getOnDrawableStateChanged();

    public void setOnDrawableStateChanged(OnDrawableStateChanged onDrawableStateChanged);

    public static interface OnDrawableStateChanged {
        public void onDrawableStateChanged(View view, int[] state);
    }
}
