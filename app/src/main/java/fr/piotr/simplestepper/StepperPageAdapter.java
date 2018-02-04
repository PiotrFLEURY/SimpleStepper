package fr.piotr.simplestepper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by piotr on 04/02/2018.
 *
 */

public class StepperPageAdapter extends FragmentStatePagerAdapter {

    public interface StepFragmentProvider {
        Fragment provideFragment(int position);
    }

    private StepperView stepperView;
    private StepFragmentProvider stepFragmentProvider;

    public StepperPageAdapter(FragmentManager fm,
                              StepperView stepperView,
                              StepFragmentProvider stepFragmentProvider){
        super(fm);
        this.stepperView = stepperView;
        this.stepFragmentProvider = stepFragmentProvider;
    }

    @Override
    public int getCount() {
        return stepperView.getStepCount();
    }

    @Override
    public Fragment getItem(int position) {
        return stepFragmentProvider.provideFragment(position);
    }

}
