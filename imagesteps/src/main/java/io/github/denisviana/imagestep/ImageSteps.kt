package io.github.denisviana.imagestep

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

/**
* Created by Denis Viana on 17/02/2018.
* e-mail denisvcosta@gmail.com
* github https://github.com/denisviana
*/
class ImageSteps: RelativeLayout {

    private var rootView : LinearLayout? = null
    private var stepsImages : MutableList<Int> = mutableListOf()
    private var stepNumber: Int = 0
    private var selectedStep: Int = 0
    private var stepSize : Int = 0
    private var defaultColor: Int = 0

    constructor(context: Context?) : super(context){
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        handlAttributes(attrs)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        handlAttributes(attrs)
        init()
    }


    private fun init(){
        View.inflate(context,R.layout.main_view,this)
        rootView = findViewById(R.id.ll_main_view)
    }

    private fun handlAttributes(attrs: AttributeSet?){

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.ImageStep,0,0)

        try {
            stepSize = typedArray.getDimensionPixelSize(R.styleable.ImageStep_step_size,25)
            defaultColor = typedArray.getColor(R.styleable.ImageStep_default_color, Color.parseColor("#E9EBEE"))
        } catch (e : Exception){
            e.printStackTrace()
        }

        typedArray.recycle()
    }

    private fun addView(tag : Int, isFirstStep : Boolean, isLastStep : Boolean){
        var view = LayoutInflater.from(context).inflate(R.layout.step_view,rootView,false)
        val params = LinearLayout.LayoutParams(stepSize,stepSize)

        if(isFirstStep)
            params.setMargins(55,0,0,0)
        else if(isLastStep)
            params.setMargins(0,0,55,0)
        else
            params.setMargins(0,0,0,0)

        view.findViewById<ImageView>(R.id.iv_main_step_image).layoutParams = params
        view.tag = tag

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            view.elevation = 1f

        setStepColorStatus(view)

        //view.findViewById<ImageView>(R.id.iv_main_step_image).setImageResource(drawableResource)
        rootView?.addView(view)

        if(!isLastStep){
            view = LayoutInflater.from(context).inflate(R.layout.split_view,rootView,false)
            view.setBackgroundColor(defaultColor)
            rootView?.addView(view)
        }

    }

    private fun setStepColorStatus(view : View){
        val gd = view.background.current as GradientDrawable
        gd.setColor(defaultColor)
    }

    fun addSteps(@DrawableRes vararg drawableResources: Int) {
        // add steps to main view
        for (drawableId in drawableResources) {
            stepsImages.add(drawableId)
            addView(stepNumber, stepNumber == 0, drawableResources.size - 1 == stepNumber)
            stepNumber++
        }
        //get to the 1st step
        goToStep(0)
    }

    fun nextStep() {
        if (stepsImages.lastIndex == selectedStep) return
        selectedStep++
        goToStep(selectedStep)
    }


    fun previousStep() {
        if (selectedStep == 0) return
        selectedStep--
        goToStep(selectedStep)
    }


    fun goToStep(stepNumber: Int) {

        val stepView = rootView?.findViewWithTag<ImageView>(stepNumber)

        for (i in 0 until stepsImages.lastIndex) {
            rootView?.findViewWithTag<ImageView>(i)?.setBackgroundResource(R.drawable.circle_step_view)
            setStepColorStatus(rootView?.findViewWithTag(i)!!)
        }

        stepView?.setBackgroundResource(R.drawable.circle_step_view)
        setStepColorStatus(rootView?.findViewWithTag(stepNumber)!!)

        selectedStep = stepNumber

        val scaleUpX = ObjectAnimator.ofFloat(stepView, "scaleX", 2.0f)
        val scaleUpY = ObjectAnimator.ofFloat(stepView, "scaleY", 2.0f)

        scaleUpX.duration = 1000
        scaleUpY.duration = 1000

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)
        scaleUp.start()

        if(stepNumber > 0){
            val previousView = rootView?.findViewWithTag<ImageView>(stepNumber-1)
            previousView?.setImageDrawable(null)
            val scaleDownX = ObjectAnimator.ofFloat(previousView, "scaleX", 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(previousView, "scaleY", 1.0f)
            scaleDownX.duration = 1000
            scaleDownY.duration = 1000
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)
            scaleDown.start()
        }

        if(stepNumber < stepsImages.lastIndex){
            val nextView = rootView?.findViewWithTag<ImageView>(stepNumber+1)
            nextView?.setImageDrawable(null)
            val scaleDownX = ObjectAnimator.ofFloat(nextView, "scaleX", 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(nextView, "scaleY", 1.0f)
            scaleDownX.duration = 1000
            scaleDownY.duration = 1000
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)
            scaleDown.start()
        }

        stepView?.setImageResource(stepsImages[stepNumber])
    }

}