package com.ezytopup.reseller.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.ezytopup.reseller.Eztytopup;

public class EZTextView extends android.support.v7.widget.AppCompatTextView {
    public EZTextView(Context context) {
        this(context, null);
    }

    public EZTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EZTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(!isInEditMode()) {
            int style = Typeface.NORMAL;

            if(attrs != null) {
                style = attrs.getAttributeIntValue(
                        "http://schemas.android.com/apk/res/android", "textStyle", Typeface.NORMAL);
            }

            setTypeface(Eztytopup.getPrintedFont(), style);
        }
    }
}
