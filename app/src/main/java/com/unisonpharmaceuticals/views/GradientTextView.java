package com.unisonpharmaceuticals.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bruce Too
 * On 8/27/15.
 * At 16:42
 */
@SuppressWarnings("unused")
public class GradientTextView extends TextView
{
    private int mTextWidth = 0;
    private int mTextHeight = 0;

    private int mTextStartX;
	private int mTextStartY;
    private float offset;
    private String mText;
    private int mDirection = DIRECTION_LEFT_TO_RIGHT;
    //Direction
    private static int DIRECTION_LEFT_TO_RIGHT = 0;
    private static int DIRECTION_RIGHT_TO_LEFT = 1;

    //left text color
    private int textLeftColor = Color.parseColor("#616161");
    //right text color
    private int textRightColor = Color.BLACK;
    public GradientTextView(Context context)
    {
        super(context);
    }

    public GradientTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GradientTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOffset(float offset)
    {
        this.offset = offset;
        invalidate();
    }

    public void setTextLeftColor(int textLeftColor)
    {
        this.textLeftColor = textLeftColor;
        invalidate();
    }

    public void setTextRightColor(int textRightColor)
    {
        this.textRightColor = textRightColor;
        invalidate();
    }

    public void setmDirection(int mDirection)
    {
        this.mDirection = mDirection;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTextWidth = (int) getPaint().measureText(getText().toString());
        mTextHeight = getMeasuredHeight();

        Rect mTextRect = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().toString().length(),mTextRect);
        mTextHeight = mTextRect.height();

        mTextStartX = (getMeasuredWidth() - mTextWidth) / 2;
        mTextStartY = (getMeasuredHeight() - mTextHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //the middle of gradient,like divider
        int middle = (int) (mTextStartX + offset*mTextWidth);
        mText = getText().toString();
//        getPaint().setTextSize(40);
        if(mDirection == DIRECTION_LEFT_TO_RIGHT)
        {
            drawLeft(middle, textLeftColor, canvas);
            drawRight(middle, textRightColor,canvas);
        }
        else if(mDirection == DIRECTION_RIGHT_TO_LEFT)
        {
            middle = (int) (mTextStartX + (1-offset)*mTextWidth);
            drawLeft(middle, textRightColor,canvas);
            drawRight(middle, textLeftColor,canvas);
        }
    }

    private void drawLeft(int middle, int mTextLeftColor,Canvas canvas)
    {
        getPaint().setColor(mTextLeftColor);

        canvas.save();//save canvas,u can ignore this to see effect
        canvas.clipRect(mTextStartX, 0, middle, getMeasuredHeight());
        canvas.drawText(mText, mTextStartX,getMeasuredHeight() / 2  
                        - ((getPaint().descent() + getPaint().ascent()) / 2), getPaint());
        canvas.restore();
    }

    /**
     * Clip right
     * @param middle
     * @param mTextRightColor
     * @param canvas
     */
    private void drawRight(int middle, int mTextRightColor,Canvas canvas)
    {
        getPaint().setColor(mTextRightColor);

        canvas.save();
        canvas.clipRect(middle, 0, mTextWidth+mTextStartX, getMeasuredHeight());
        canvas.drawText(mText, mTextStartX,getMeasuredHeight() / 2  
                        - ((getPaint().descent() + getPaint().ascent()) / 2), getPaint());
        canvas.restore();
    }
}
