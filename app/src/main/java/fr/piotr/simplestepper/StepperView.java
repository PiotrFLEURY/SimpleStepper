package fr.piotr.simplestepper;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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

public class StepperView extends CardView {

    public interface OnStepChangeListener {
        void onStepChange(int position);
    }

    private static final int DEFAULT_STEP_COUNT = 3;
    private static final int DEFAULT_CURRENT_POSITION = 0;
    private static final String DEFAULT_STEP_TITLE = "Step %s";

    int stepCount;
    int currentPosition;

    private OnStepChangeListener onStepChangeListener;

    LinearLayout stepperContainer;
    TextView tvMessage;

    public StepperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StepperView,
                0, 0);

        try {

            stepCount = a.getInteger(R.styleable.StepperView_step_count, DEFAULT_STEP_COUNT);
            if(isInEditMode()) {
                currentPosition = a.getInteger(R.styleable.StepperView_current_position, DEFAULT_CURRENT_POSITION);
            } else {
                currentPosition = 0;
            }

        } finally {
            a.recycle();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View contentView = layoutInflater.inflate(R.layout.stepper_layout, this, false);
        stepperContainer = contentView.findViewById(R.id.stepper_container);
        tvMessage = contentView.findViewById(R.id.stepper_message);
        for(int i=0;i<stepCount;i++){
            inflateStep(layoutInflater, i);
        }
        addView(contentView);

        selectButton(currentPosition, false);

    }

    private void inflateStep(LayoutInflater layoutInflater, int i) {
        View stepBubbleLayout = layoutInflater.inflate(R.layout.step_bubble, stepperContainer, false);
        View leftLine = stepBubbleLayout.findViewById(R.id.step_left_line);
        View rightLine = stepBubbleLayout.findViewById(R.id.step_right_line);
        ImageButton stepBubbleButton = stepBubbleLayout.findViewById(R.id.step_bubble_button);
        TextView stepBubbleTextView = stepBubbleLayout.findViewById(R.id.step_bubble_title);

        stepperContainer.addView(stepBubbleLayout);

        if(isInEditMode()){
            stepBubbleTextView.setText(String.format(DEFAULT_STEP_TITLE, i+1));
        }

        stepBubbleButton.setTag(i);
        stepBubbleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton((Integer) v.getTag(), true);
            }
        });

        if(i==0){
            leftLine.setVisibility(INVISIBLE);
        } else if(i==stepCount-1){
            rightLine.setVisibility(INVISIBLE);
        }
    }

    private void selectButton(int tag, boolean notify) {
        findViewWithTag(currentPosition).animate().scaleX(1f).scaleY(1f).alpha(0.8f).start();
        for(int i=0;i<stepCount;i++){
            View viewWithTag = findViewWithTag(i);
            viewWithTag.setSelected(i<tag);
            viewWithTag.setEnabled(i<=tag);
            if(i==tag){
                viewWithTag.animate().scaleX(1.2f).scaleY(1.2f).alpha(1f).start();
            }
        }
        currentPosition = tag;
        if(notify && onStepChangeListener!=null){
            onStepChangeListener.onStepChange(currentPosition);
        }
    }

    public void setStepTitle(int position, String title) {
        TextView tvTtitle = stepperContainer.getChildAt(position).findViewById(R.id.step_bubble_title);
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

    public void showMessage(String message) {
        stepperContainer.setVisibility(View.INVISIBLE);
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage(){
        tvMessage.setVisibility(View.INVISIBLE);
        stepperContainer.setVisibility(View.VISIBLE);
    }
}
