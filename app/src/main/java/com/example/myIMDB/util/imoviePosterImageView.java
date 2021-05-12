package com.example.myIMDB.util;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class imoviePosterImageView extends AppCompatImageView {
    public imoviePosterImageView(Context context) {
        super(context);
    }

    public imoviePosterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public imoviePosterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Unpack given dimens to new aspect ratio
        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 4 / 3;
        int threeTwoHeightSpec = MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }
}
