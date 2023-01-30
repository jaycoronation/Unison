package com.unisonpharmaceuticals.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.unisonpharmaceuticals.R;

public class RegularTextView extends AppCompatTextView
{
    public RegularTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public RegularTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    public RegularTextView(Context context)
    {
        super(context);
        if (!isInEditMode())
        {
            setType(context);
        }
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular)));
        this.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4.0f,  getResources().getDisplayMetrics()), 1.0f);
    }
}