package com.sds.xr.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sds.xr.R;

import java.lang.reflect.Field;

public class CustomClearableEditText extends FrameLayout implements View.OnClickListener, TextWatcher, IDrawableStateObservable.OnDrawableStateChanged {
    protected static final String TAG = null;
    private ViewGroup mRootLayout;
    private View mBackground;
    private EditText mEditText;
    private ImageButton mClearButton;
    private ImageView mUnderLine;

    private int mSearchIconResId;
    private int mMaxLength;
    @Deprecated
    private OnMaxLengthReachListener mListener;
    private OnClickListener mClearButtonClickListener;
    private CharSequence mOriginalHint;
    private boolean mImageGone;

    public CustomClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.customClearableEditTextStyle);
    }

    public CustomClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initialize(context, attrs, defStyle);
    }

    @Deprecated
    public void setOnMaxLengthReachListener(OnMaxLengthReachListener listener) {
        mListener = listener;
    }

    @Deprecated
    public OnMaxLengthReachListener getOnMaxLengthReachListener() {
        return mListener;
    }

    public OnClickListener getClearButtonClickListener() {
        return mClearButtonClickListener;
    }

    public void setClearButtonClickListener(OnClickListener clearButtonClickListener) {
        this.mClearButtonClickListener = clearButtonClickListener;
    }

    @Override
    public void setMinimumHeight(int minHeight) {
        super.setMinimumHeight(minHeight);

        mRootLayout.setMinimumHeight(minHeight);
    }

    @Override
    public void setOnKeyListener(OnKeyListener l) {
        mEditText.setOnKeyListener(l);
    }

    public CharSequence getText() {
        return mEditText.getText();
    }

    public void setImageGone(boolean gone){
        if(gone){
            mClearButton.setVisibility(View.GONE);
        }else{
            mClearButton.setVisibility(View.VISIBLE);
        }
        mImageGone = gone;
    }

    public void setText(CharSequence text) {
        mEditText.setText(text);
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public void setText (CharSequence text, TextView.BufferType type) {
        mEditText.setText(text,type);
    }

    // Added MSC yh4813.ryu add append method for last point of cusor
    public void append(CharSequence text) {
        mEditText.append(text);
    }

    // Added MSC yh4813.ryu add append method for last point of cusor

    public CharSequence getHint() {
        return mOriginalHint;
    }

    public void setHint(CharSequence hint) {
        mOriginalHint = hint;

        mEditText.setHint(getDecoreatedHint(hint));
    }

    public void setHint(int resId) {
        setHint(getContext().getResources().getText(resId));
    }

    public void setHintWithoutIamge(CharSequence hint) {
        mOriginalHint = hint;
        mEditText.setHint(hint);
    }

    public void setMaxLength(int length) {
        mMaxLength = length;

        mEditText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(mMaxLength) });
    }

    public int getMaxLength() {
        return mMaxLength;
    }

    public void setSelection(int index) {
        mEditText.setSelection(index);
    }

    public void setSelection(int start, int stop) {
        mEditText.setSelection(start, stop);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mEditText.addTextChangedListener(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        mEditText.removeTextChangedListener(watcher);
    }

    public void setTextColor(int color) {
        mEditText.setTextColor(color);
    }

    public void setImeOptions(int imeOptions) {
        mEditText.setImeOptions(imeOptions);
    }

    public int getImeOptions() {
        return mEditText.getImeOptions();
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        mEditText.setOnEditorActionListener(l);
    }

    public void setFilters(InputFilter[] filters) {
        mEditText.setFilters(filters);
    }

    public InputFilter[] getFilters() {
        return mEditText.getFilters();
    }

    public void setInputType(int type) {
        mEditText.setInputType(type);
    }

    public int getInputType() {
        return mEditText.getInputType();
    }

    public void setSearchIcon(int resId) {
        mSearchIconResId = resId;

        setHint(mOriginalHint);
    }

    public int getCurrentTextColor() {
        return mEditText.getCurrentTextColor();
    }

    public void setOnFocusChangeListener(View.OnFocusChangeListener listener) {
        mEditText.setOnFocusChangeListener(listener);
    }

    public void setCursorVisible(boolean visible) {
        mEditText.setCursorVisible(visible);
    }

    //+ 2015.05
    public void setCursorColor(int color) {
        setCursorDrawableColor(mEditText, color);
    }

    private void setCursorDrawableColor(EditText editText, int color) {
        try {
            // Get the cursor resource id
            if(Build.VERSION.SDK_INT >= 28){//set differently in Android P (API 28)
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                int drawableResId = field.getInt(editText);

                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);

                // Get the drawable and set a color filter
                Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);

                // Set the drawables
                field = editor.getClass().getDeclaredField("mDrawableForCursor");
                field.setAccessible(true);
                field.set(editor, drawable);
            }else {
                Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
                field.setAccessible(true);
                int drawableResId = field.getInt(editText);

                // Get the editor
                field = TextView.class.getDeclaredField("mEditor");
                field.setAccessible(true);
                Object editor = field.get(editText);

                // Get the drawable and set a color filter
                Drawable drawable = ContextCompat.getDrawable(editText.getContext(), drawableResId);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
                Drawable[] drawables = {drawable, drawable};

                // Set the drawables
                field = editor.getClass().getDeclaredField("mCursorDrawable");
                field.setAccessible(true);
                field.set(editor, drawables);
            }
        } catch (Exception ignored) {
        }
    }

    //- 2015.05

    public boolean isCursorVisible() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return mEditText.isCursorVisible();
        }
        return false;
    }
    public void setChangeClearButton() {
/*        if(CustomTheme.getCurrentTheme() == SkinType.Night) {
            Drawable drawable = getResources().getDrawable(R.drawable.close_input_night);
            mClearButton.setImageDrawable(drawable);
        }else*/{
            Drawable drawable = getResources().getDrawable(R.drawable.close_input);
            mClearButton.setImageDrawable(drawable);
        }
    }

    public void setOnEditTextClickListener(View.OnClickListener listener) {
        mEditText.setOnClickListener(listener);
    }

    public void setEnabled(boolean enabled) {
        mEditText.setEnabled(enabled);
        mClearButton.setEnabled(enabled);
        if (enabled) {
            mRootLayout.requestFocus();
        } else {
            mRootLayout.clearFocus();
        }
    }

    public void setErrorBackground() {
        mBackground.setBackgroundColor(Color.parseColor("#19dd1c0c"));
        mUnderLine.setBackgroundColor(Color.parseColor("#dd1c0c"));
    }

    public void setTextsize(int size){
        mEditText.setTextSize(size);
    }

    public void setHintTextColor(int color){
        mEditText.setHintTextColor(color);
    }

    public EditText getEditText() {
        return mEditText;
    }
/*	public void setTransformationMethod(){
		mEditText.setTransformationMethod(new MyPasswordTransformationMethod());
	}*/

    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText, defStyle, 0);

        int layoutResId = arr.getResourceId(R.styleable.ClearableEditText_sqLayout, R.layout.layout_custom_clearable_edit_text);

        mRootLayout = (ViewGroup) LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
        mBackground = mRootLayout.findViewById(R.id.root);
        addView(mRootLayout);


        if (mRootLayout instanceof IDrawableStateObservable) {
            ((IDrawableStateObservable) mRootLayout).setOnDrawableStateChanged(this);

            Drawable drawable = getBackground();

            if (drawable != null) {
                drawable.setState(mRootLayout.getDrawableState());
            }
        }

        mEditText = (EditText) mRootLayout.findViewById(R.id.clearable_text1);
        mEditText.setSingleLine();
        mEditText.addTextChangedListener(this);

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    mClearButton.setVisibility(View.VISIBLE);
//					mEditText.setPadding(60, 0, 0, 0);
//					setImageGone(false);
                } else {
                    mClearButton.setVisibility(View.GONE);
//					mEditText.setPadding(0, 0, 0, 0);
//					setImageGone(true);
                }
            }
        });

        // Because FrameLayout has bug about minHeight, set minimum height to root layout of children.
        int minHeight = arr.getDimensionPixelSize(R.styleable.ClearableEditText_android_minHeight, 0);
        mRootLayout.setMinimumHeight(minHeight);

        int maxLength = arr.getInt(R.styleable.ClearableEditText_android_maxLength, 0);
        String hint = arr.getString(R.styleable.ClearableEditText_android_hint);
        int inputType = arr.getInt(R.styleable.ClearableEditText_android_inputType, mEditText.getInputType());
        int imeOptions = arr.getInt(R.styleable.ClearableEditText_android_imeOptions, mEditText.getImeOptions());
        mSearchIconResId = arr.getResourceId(R.styleable.ClearableEditText_sqSearchIcon, 0);

        arr.recycle();

        if (maxLength > 0) {
            setMaxLength(maxLength);
        }

        setHint(hint);
        mEditText.setInputType(inputType);
        mEditText.setImeOptions(imeOptions);

        mClearButton = (ImageButton) mRootLayout.findViewById(R.id.clearable_button1);
        mClearButton.setOnClickListener(this);

/*        if(CustomTheme.getCurrentTheme() == SkinType.Night) {
            Drawable drawable = getResources().getDrawable(R.drawable.close_input_night);
            mClearButton.setImageDrawable(drawable);
        }*/

        updateViews();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // NOOP
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mMaxLength > 0 && s.length() == mMaxLength) {
            if (mListener != null) {
                mListener.onMaxLengthReachListener();
            }
        }

        updateViews();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // NOOP
    }

    @Override
    public void onClick(View view) {
        mEditText.setText("");

        if (mClearButtonClickListener != null) {
            mClearButtonClickListener.onClick(this);
        }

        mEditText.requestFocus();
    }

    @Override
    public void onDrawableStateChanged(View view, int[] state) {
        Drawable drawable = getBackground();

        if (drawable != null) {
            drawable.setState(state);
        }
    }

    @Deprecated
    public interface OnMaxLengthReachListener {
        public void onMaxLengthReachListener();
    }

    private void updateViews() {

        // Empty
        if (mEditText.getText() == null || mEditText.getText().length() == 0){
            mClearButton.setVisibility(View.GONE);
        }else if(mImageGone) {
            mClearButton.setVisibility(View.GONE);
        }else{
            mClearButton.setVisibility(View.VISIBLE);
        }
    }

    private CharSequence getDecoreatedHint(CharSequence hint) {
        if (mSearchIconResId == 0) {
            return hint;
        }

        if (hint == null) {
            return hint;
        }

        //+ 2015.05
        //SpannableStringBuilder builder = new SpannableStringBuilder("   ");
        SpannableStringBuilder builder = new SpannableStringBuilder(" ");
        //- 2015.05

        builder.append(hint);

        Drawable searchIcon = getContext().getResources().getDrawable(mSearchIconResId);
        if (searchIcon != null) {
            ImageSpan span = null;
            searchIcon.setBounds(0, 0, searchIcon.getIntrinsicWidth(), searchIcon.getIntrinsicHeight());

            //+ 2015.05
			/*if ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
				span = new ImageSpan(searchIcon, ImageSpan.ALIGN_BASELINE);
			}
			else {
				span = new ImageSpan(searchIcon, ImageSpan.ALIGN_BOTTOM);
			}
			builder.setSpan(span, 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/

            if ((getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE) {
                span = new CenterImageSpan(searchIcon, ImageSpan.ALIGN_BASELINE);
            }
            else {
                span = new CenterImageSpan(searchIcon, ImageSpan.ALIGN_BOTTOM);
            }
            builder.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //- 2015.05
        }
        return builder;
    }
}