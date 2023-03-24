package com.unisonpharmaceuticals.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import com.unisonpharmaceuticals.R;


public class CustomTextInputLayout extends TextInputLayout
{
    public CustomTextInputLayout(Context context)
    {
        super(context);
      //  initFont(context);
    }

    public CustomTextInputLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
     //   initFont(context);
    }

    @Override
    public void setError(@Nullable CharSequence error)
    {
        try {
            if(error != null)
            {
                /*final Typeface typeface = Typeface.createFromAsset(this.getContext().getAssets(), this.getContext().getResources().getString(R.string.font_regular));
                final SpannableString ss = new SpannableString(error);
                ss.setSpan(new EditTextErrorTypeFace(typeface), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);*/
                super.setError(error);
            }
            else
            {
                super.setError(error);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFont(Context context)
    {
        try {
            final Typeface typeface = Typeface.createFromAsset(context.getAssets(), context.getResources().getString(R.string.font_regular));

            EditText editText = getEditText();
            if (editText != null)
            {
                editText.setTypeface(typeface);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
       /* try
        {
            // Retrieve the CollapsingTextHelper Field
            final Field cthf = TextInputLayout.class.getDeclaredField("mCollapsingTextHelper");
            cthf.setAccessible(true);

            // Retrieve an instance of CollapsingTextHelper and its TextPaint
            final Object cth = cthf.get(this);
            final Field tpf = cth.getClass().getDeclaredField("mTextPaint");
            tpf.setAccessible(true);

            // Apply your Typeface to the CollapsingTextHelper TextPaint
            ((TextPaint) tpf.get(cth)).setTypeface(typeface);
        }
        catch (Exception ignored)
        {
        	ignored.printStackTrace();
        }*/
    }
}