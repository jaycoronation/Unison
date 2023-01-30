package com.unisonpharmaceuticals.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.unisonpharmaceuticals.R;

public class RegularEditText extends AppCompatEditText
{
		public RegularEditText(Context context) {
			super(context);
			setType(context);
		}

		public RegularEditText(Context context, AttributeSet attrs) {
			super(context, attrs);
			setType(context);
		}

		public RegularEditText(Context context, AttributeSet attrs, int defStyleAttr) {
			super(context, attrs, defStyleAttr);
			setType(context);
		}

		private void setType(Context context)
		{
			this.setTypeface(Typeface.createFromAsset(context.getAssets(),context.getString(R.string.font_regular)));
		}
}