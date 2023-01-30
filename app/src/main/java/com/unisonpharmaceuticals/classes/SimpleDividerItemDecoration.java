package com.unisonpharmaceuticals.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.unisonpharmaceuticals.R;

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration
{
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context)
    {
        mDivider = ContextCompat.getDrawable(context , R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state)
    {
    	
    	//For Draw Vertical Line
        /*int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++)
        {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }*/
        
    	  final int top = parent.getPaddingTop();
          final int bottom = parent.getHeight() - parent.getPaddingBottom();
   
          final int childCount = parent.getChildCount();
          for (int i = 0; i < childCount; i++)
          {
              final View child = parent.getChildAt(i);
              final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
              final int left = child.getRight() + params.rightMargin;
              final int right = left + mDivider.getIntrinsicHeight();
              mDivider.setBounds(left, top, right, bottom);
              mDivider.draw(c);
          }
    }
}