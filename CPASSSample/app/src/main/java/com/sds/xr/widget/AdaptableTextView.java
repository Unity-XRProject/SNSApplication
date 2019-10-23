package com.sds.xr.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AdaptableTextView extends TextView{
    public AdaptableTextView(Context context) {
        super(context);

        Helper.adapt(this);
    }

    public AdaptableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Helper.adapt(this);
    }

    public AdaptableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Helper.adapt(this);
    }

    public static class Helper {
        private static Method mSetDeviceDefaultThemeMethod;

        static {
            try {
                mSetDeviceDefaultThemeMethod = TextView.class.getMethod("setDeviceDefaultTheme", boolean.class);
            } catch (NoSuchMethodException e) {//NOPMD
                // Ignore...
            }
        }

        public static void adapt(TextView target) {
            if (mSetDeviceDefaultThemeMethod != null) {
                try {
                    mSetDeviceDefaultThemeMethod.invoke(target, true);
                } catch (IllegalArgumentException e) {//NOPMD
                } catch (IllegalAccessException e) {//NOPMD
                } catch (InvocationTargetException e) {//NOPMD
                }
            }
        }
    }
}
