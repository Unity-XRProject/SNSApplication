package com.sds.xr.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.widget.EditText;

import com.sds.xr.utils.ObservableLengthFilter;
import com.sds.xr.widget.AdaptableTextView.Helper;

public class AdaptableEditText extends EditText {
    public AdaptableEditText(Context context) {
        super(context);

        Helper.adapt(this);
    }

    public AdaptableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        Helper.adapt(this);
    }

    public AdaptableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Helper.adapt(this);
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        super.setFilters(filters);

        for (InputFilter filter : filters) {
            if (filter instanceof ObservableLengthFilter) {
                Bundle bundle = getInputExtras(true);
                bundle.putInt("maxLength", ((ObservableLengthFilter) filter).getMaxLength());
            }
        }
    }
}
