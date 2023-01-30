package com.unisonpharmaceuticals.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.unisonpharmaceuticals.R;

/**
 * Created by Tushar Vataliya on 14-Jul-18.
 */
public class SemiBoldEditText extends AppCompatEditText
{
    public SemiBoldEditText(Context context) {
        super(context);
        setType(context);
    }

    public SemiBoldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public SemiBoldEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context)
    {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_semibold)));
    }
}
