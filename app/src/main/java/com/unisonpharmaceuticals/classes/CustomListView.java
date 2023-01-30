package com.unisonpharmaceuticals.classes;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView {

private android.view.ViewGroup.LayoutParams params;
private int prevCount = 0;

public CustomListView(Context context, AttributeSet attrs)
{
    super(context, attrs);
}

@Override
protected void onDraw(Canvas canvas)
{
    if (getCount() != prevCount) 
    {
        int height = getChildAt(0).getHeight() + 1 ;
        prevCount = getCount();
        params = getLayoutParams();
        params.height = getCount() * height;
        setLayoutParams(params);
    }

    super.onDraw(canvas);
}

}