package fr.piotr.simplestepper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by piotr on 04/02/2018.
 *
 */

public class StepperView extends LinearLayout implements ViewPager.OnPageChangeListener {

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        selectButton(position, false);
    }

    @Override
    public void onPageSelected(int position) {
        selectButton(position, false);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //
    }

    public interface OnStepChangeListener {
        void onStepChange(int position);
    }

    private static final int DEFAULT_STEP_COUNT = 3;
    private static final int DEFAULT_CURRENT_POSITION = 0;
    private static final String DEFAULT_STEP_TITLE = "Step %s";

    int stepCount;
    int currentPosition;

    private OnStepChangeListener onStepChangeListener;

    public StepperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StepperView,
                0, 0);

        try {

            stepCount = a.getInteger(R.styleable.StepperView_step_count, DEFAULT_STEP_COUNT);
            currentPosition = a.getInteger(R.styleable.StepperView_current_position, DEFAULT_CURRENT_POSITION);

        } finally {
            a.recycle();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for(int i=0;i<stepCount;i++){
            View stepBubbleLayout = layoutInflater.inflate(R.layout.step_bubble, this, false);
            ImageButton stepBubbleButton = stepBubbleLayout.findViewById(R.id.step_bubble_button);
            TextView stepBubbleTextView = stepBubbleLayout.findViewById(R.id.step_bubble_title);

            addView(stepBubbleLayout);

            if(isInEditMode()){
                if(i<currentPosition) {
                    stepBubbleButton.setSelected(true);
                } else if(i==currentPosition){
                    stepBubbleButton.setPressed(true);
                }
                stepBubbleTextView.setText(String.format(DEFAULT_STEP_TITLE, i+1));
            }

            stepBubbleButton.setTag(i);
            stepBubbleButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectButton((Integer) v.getTag(), true);
                }
            });

        }

    }

    private void selectButton(int tag, boolean notify) {
        for(int i=0;i<stepCount;i++){
            findViewWithTag(i).setSelected(i<tag);
            findViewWithTag(i).setPressed(i==tag);
        }
        currentPosition = tag;
        if(notify && onStepChangeListener!=null){
            onStepChangeListener.onStepChange(currentPosition);
        }
    }

    public void setStepTitle(int position, String title) {
        TextView tvTtitle = getChildAt(position).findViewById(R.id.step_bubble_title);
        tvTtitle.setText(title);
    }

    public void nextStep() {
        if(currentPosition<stepCount-1) {
            selectButton(currentPosition + 1, true);
        }
    }

    public void previousStep() {
        if(currentPosition>0) {
            selectButton(currentPosition - 1, true);
        }
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setOnStepChangeListener(OnStepChangeListener onStepChangeListener) {
        this.onStepChangeListener = onStepChangeListener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }
}
