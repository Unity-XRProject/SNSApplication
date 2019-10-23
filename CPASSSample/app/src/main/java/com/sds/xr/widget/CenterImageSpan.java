package com.sds.xr.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.sds.GLApplication;

import java.lang.ref.WeakReference;

public class CenterImageSpan extends ImageSpan {
	private WeakReference<Drawable> mDrawableRef;
	private Context mContext;

	  public CenterImageSpan(Drawable drawable) {
	    super(drawable);
	  }
	
	  public CenterImageSpan(Context context, int drawableRes) {
	    super(context, drawableRes);
	  }

	  public CenterImageSpan(Drawable drawable, int verticalAlignment) {
		  super(drawable, verticalAlignment);
	  }
	  
	  public CenterImageSpan(Context context, Drawable drawable, int verticalAlignment) {
		  super(drawable, verticalAlignment);
		  mContext = context;
	  }
	  
	  @Override
	  public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
	    Drawable d = getCachedDrawable();
	    Rect rect = d.getBounds();

	    if (fm != null) {
	      Paint.FontMetricsInt pfm = paint.getFontMetricsInt();
	      // keep it the same as paint's fm
	      fm.ascent = pfm.ascent;
	      fm.descent = pfm.descent;
	      fm.top = pfm.top;
	      fm.bottom = pfm.bottom;
	    }

	    return rect.right;
	  }

	  @Override
	  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x,
	                   int top, int y, int bottom, @NonNull Paint paint) {
	    Drawable b = getCachedDrawable();
	    canvas.save();

	    int drawableHeight = b.getIntrinsicHeight();
	    int fontAscent = paint.getFontMetricsInt().ascent;
	    int fontDescent = paint.getFontMetricsInt().descent;
	    int transY = bottom - b.getBounds().bottom +  // align bottom to bottom
	        (drawableHeight - fontDescent + fontAscent) / 2;  // align center to center

	    transY -= 3;
	    
	    if (mContext != null) {
	    	int width = getDisplayWidth();
		    int margin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, mContext.getResources().getDisplayMetrics());
		    x = width - margin;
	    }
	    canvas.translate(x, transY);
	    b.draw(canvas);
	    canvas.restore();
	  }

	  @SuppressLint("NewApi")
	  private static int getDisplayWidth() {
		  int measuredWidth = 0;
		  Point size = new Point();
		  WindowManager w = ((WindowManager) GLApplication.getInstance().getSystemService(Context.WINDOW_SERVICE));

		  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			  w.getDefaultDisplay().getSize(size);
			  measuredWidth = size.x;
		  } else {
			  Display d = w.getDefaultDisplay();
			  measuredWidth = d.getWidth();
		  }
		  return measuredWidth;
	  }

	  // Redefined locally because it is a private member from DynamicDrawableSpan
	  private Drawable getCachedDrawable() {
	    WeakReference<Drawable> wr = mDrawableRef;
	    Drawable d = null;

	    if (wr != null)
	      d = wr.get();

	    if (d == null) {
	      d = getDrawable();
	      mDrawableRef = new WeakReference<Drawable>(d);
	    }

	    return d;
	  }
}
