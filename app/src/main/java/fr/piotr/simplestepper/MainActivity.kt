package fr.piotr.simplestepper

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.stepper_view_sample.*

class MainActivity : AppCompatActivity(), StepperPageAdapter.StepFragmentProvider, StepperView.OnStepChangeListener, View.OnTouchListener{
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return true
    }

    override fun onStepChange(position: Int) {
        stepper_view_pager.setCurrentItem(position, true)
    }

    override fun provideFragment(position: Int): Fragment {
            var dummyFragment = DummyFragment()
            var bundle = Bundle()
            bundle.putString(DummyFragment.ARG_TEXT, String.format("Page %s", position))
            dummyFragment.arguments = bundle
            return dummyFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stepper_view_sample)

        stepper_view.setStepTitle(0, "first")
        stepper_view.setStepTitle(1, "second")
        stepper_view.setStepTitle(2, "third")
        stepper_view.setOnStepChangeListener(this)

        previous_btn.setOnClickListener(this::previousStep)
        next_btn.setOnClickListener(this::nextStep)

        stepper_view_pager.adapter = StepperPageAdapter(supportFragmentManager, stepper_view, this)
        stepper_view_pager.setOnTouchListener(this)
    }

    fun previousStep(v: View){
        stepper_view.previousStep()
    }

    fun nextStep(v: View){
        stepper_view.nextStep()
    }

    override fun onBackPressed() {
        if(stepper_view.currentPosition>0){
            stepper_view.previousStep()
        } else {
            super.onBackPressed()
        }
    }
}
