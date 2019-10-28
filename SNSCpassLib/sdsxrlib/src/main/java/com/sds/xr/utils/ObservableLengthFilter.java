package com.sds.xr.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class ObservableLengthFilter extends InputFilter.LengthFilter {
    private int mMaxLength;
    private OnMaxLengthReachListener mListener;

    public ObservableLengthFilter(int maxLength) {
        this(maxLength, null);
    }

    public ObservableLengthFilter(int maxLength, OnMaxLengthReachListener listener) {
        super(maxLength);

        mMaxLength = maxLength;
        mListener = listener;
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setOnMaxLengthReachListener(OnMaxLengthReachListener listener) {
        mListener = listener;
    }

    public OnMaxLengthReachListener getOnMaxLengthReachListener(OnMaxLengthReachListener listener) {
        return mListener;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence result = super.filter(source, start, end, dest, dstart, dend);

        if (result == null) {	//NOPMD
        }
        else {
            // MAX LEGNTH

            if (mListener != null) {
                mListener.onMaxLengthReachListener(mMaxLength);
            }
        }

        return result;
    }

    public static interface OnMaxLengthReachListener {
        public void onMaxLengthReachListener(int maxLength);
    }
}
