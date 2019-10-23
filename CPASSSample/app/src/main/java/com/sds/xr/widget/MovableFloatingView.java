package com.sds.xr.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.sds.xr.R;

public class MovableFloatingView extends AppCompatImageView implements View.OnTouchListener {

    private final static float CLICK_DRAG_TOLERANCE = 10; // Often, there will be a slight, unintentional, drag when the user taps the FAB, so we need to account for this.
    private int defaultIcon;
    private int pressedIcon;
    private boolean isScalable;
    private boolean isMovable;
    private boolean isEscapable;

    private float rawX, rawY;
    private float dX, dY;

    private int originHeight;
    private int originWidth;
    private int scaledHeight;
    private int scaledWidth;
    private boolean isFirst = true;

    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    public MovableFloatingView(Context context) {
        this(context, null);
    }

    public MovableFloatingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovableFloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(attrs, defStyleAttr);
    }

    private void init() {
        setOnTouchListener(this);
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MovableFloatingView);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MovableFloatingView, defStyleAttr, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        this.defaultIcon = typedArray.getResourceId(R.styleable.MovableFloatingView_defaultIcon, R.drawable.ic_streaming_menu_button);
        this.pressedIcon = typedArray.getResourceId(R.styleable.MovableFloatingView_pressedIcon, R.drawable.ic_streaming_menu_button);
        this.isMovable = typedArray.getBoolean(R.styleable.MovableFloatingView_isMovable, true);
        this.isScalable = typedArray.getBoolean(R.styleable.MovableFloatingView_isScalable, true);
        this.isEscapable = typedArray.getBoolean(R.styleable.MovableFloatingView_isEscapable, true);

        this.setImageResource(this.defaultIcon);

        typedArray.recycle();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        View viewParent;

        if( isScalable ) {
            scaleGestureDetector.onTouchEvent(motionEvent);
        }

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                mode = DRAG;

                this.setImageResource(this.pressedIcon);
                this.setScaleX(scaleFactor * 1.1f);
                this.setScaleY(scaleFactor * 1.1f);
                this.setAlpha(1.0f);

                rawX = motionEvent.getRawX();
                rawY = motionEvent.getRawY();
                dX = view.getX() - rawX;
                dY = view.getY() - rawY;

                return true; // Consumed

            case MotionEvent.ACTION_MOVE:

                if( isMovable && mode == DRAG ) {

                    int viewWidth = view.getWidth();
                    int viewHeight = view.getHeight();

                    viewParent = (View) view.getParent();
                    int parentWidth = viewParent.getWidth();
                    int parentHeight = viewParent.getHeight();

                    float minX = isEscapable ? ((float)viewWidth / 2.0f) : 0;
                    float maxX = isEscapable ? ((float)viewWidth / 2.0f) : viewWidth;

                    float newX = motionEvent.getRawX() + dX;
                    newX = Math.max(minX * -1, newX);
                    newX = Math.min(parentWidth - maxX, newX);

                    float minY = isEscapable ? ((float)viewHeight / 2.0f) : 0;
                    float maxY = isEscapable ? ((float)viewHeight / 2.0f) : viewHeight;

                    float newY = motionEvent.getRawY() + dY;
                    newY = Math.max(minY * -1, newY);
                    newY = Math.min(parentHeight - maxY, newY);

                    view.animate().x(newX).y(newY).setDuration(0).start();
                }

                return true; // Consumed

            case MotionEvent.ACTION_UP:

                mode = NONE;

                this.setImageResource(this.defaultIcon);
                this.setScaleX(scaleFactor);
                this.setScaleY(scaleFactor);
                this.setAlpha(0.5f);

                float upRawX = motionEvent.getRawX();
                float upRawY = motionEvent.getRawY();

                float upDX = upRawX - rawX;
                float upDY = upRawY - rawY;

                if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                    return performClick();
                }
                else {
                    return true;
                }

                // A drag consumed
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                return true;
            default:
                return super.onTouchEvent(motionEvent);
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.4f, Math.min(scaleFactor, 5.0f));

            setScaleX(scaleFactor);
            setScaleY(scaleFactor);

            return super.onScale(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

            scaledHeight = (int)(originHeight * scaleFactor);
            scaledWidth = (int)(originWidth * scaleFactor);

            super.onScaleEnd(detector);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if( this.isFirst ) {

            this.originHeight = getHeight();
            this.originWidth = getWidth();
            this.scaledHeight = getHeight();
            this.scaledWidth = getWidth();

            this.isFirst = false;
        }
        super.onDraw(canvas);
    }

    public int getScaledHeight() {
        return scaledHeight;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }
}
