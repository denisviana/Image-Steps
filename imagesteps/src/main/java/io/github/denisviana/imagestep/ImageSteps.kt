package io.github.denisviana.imagestep

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.viewpager.widget.ViewPager
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

    private var root: LinearLayout? = null
    private var stepsImages : MutableList<Int> = mutableListOf()
    private var stepNumber: Int = 0
    private var selectedStep: Int = 0
    private var stepSize : Int = 0
    private var defaultColor: Int = 0
    private var lastPosition : Int = 0
    private var hasViewPager : Boolean = false

    private var onPageListener : OnViewPagerChangeListener? = null

    private lateinit var viewPager : ViewPager

    var animationDuration : Long = 500
    var scaleUp = 2.0f

    private val START_STEP = 0

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
        View.inflate(context,R.layout.main,this)
        root = findViewById(R.id.ll_main_view)
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
        var view = LayoutInflater.from(context).inflate(R.layout.step, root,false)
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
        root?.addView(view)

        if(!isLastStep){
            view = LayoutInflater.from(context).inflate(R.layout.line, root,false)
            view.setBackgroundColor(defaultColor)
            root?.addView(view)
        }

    }

    private fun setStepColorStatus(view : View){
        val gd = view.background.current as GradientDrawable
        gd.setColor(defaultColor)
    }

    fun setSteps(@DrawableRes vararg drawableResources: Int) {
        for (drawableId in drawableResources) {
            stepsImages.add(drawableId)
            addView(stepNumber, stepNumber == 0, drawableResources.size - 1 == stepNumber)
            stepNumber++
        }
        goToStep(START_STEP)
    }

    fun next() {
        if (stepsImages.lastIndex == selectedStep || hasViewPager) return
        selectedStep++
        goToStep(selectedStep)
    }


    fun previous() {
        if (selectedStep == START_STEP || hasViewPager) return
        selectedStep--
        goToStep(selectedStep)
    }


    private fun goToStep(stepNumber: Int) {

        val stepView = root?.findViewWithTag<ImageView>(stepNumber)

        for (i in START_STEP until stepsImages.lastIndex) {
            root?.findViewWithTag<ImageView>(i)?.setBackgroundResource(R.drawable.circle_step_view)
            setStepColorStatus(root?.findViewWithTag(i)!!)
        }

        stepView?.setBackgroundResource(R.drawable.circle_step_view)
        setStepColorStatus(root?.findViewWithTag(stepNumber)!!)

        selectedStep = stepNumber

        val scaleUpX = ObjectAnimator.ofFloat(stepView, "scaleX", scaleUp)
        val scaleUpY = ObjectAnimator.ofFloat(stepView, "scaleY", scaleUp)

        scaleUpX.duration = animationDuration
        scaleUpY.duration = animationDuration

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)
        scaleUp.start()

        if(stepNumber > START_STEP){
            val previousView = root?.findViewWithTag<ImageView>(stepNumber-1)
            previousView?.setImageDrawable(null)
            val scaleDownX = ObjectAnimator.ofFloat(previousView, "scaleX", 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(previousView, "scaleY", 1.0f)
            scaleDownX.duration = animationDuration
            scaleDownY.duration = animationDuration
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)
            scaleDown.start()
        }

        if(stepNumber < stepsImages.lastIndex){
            val nextView = root?.findViewWithTag<ImageView>(stepNumber+1)
            nextView?.setImageDrawable(null)
            val scaleDownX = ObjectAnimator.ofFloat(nextView, "scaleX", 1.0f)
            val scaleDownY = ObjectAnimator.ofFloat(nextView, "scaleY", 1.0f)
            scaleDownX.duration = animationDuration
            scaleDownY.duration = animationDuration
            val scaleDown = AnimatorSet()
            scaleDown.play(scaleDownX).with(scaleDownY)
            scaleDown.start()
        }

        stepView?.setImageResource(stepsImages[stepNumber])
    }

    fun setupWithViewPager(viewPager : ViewPager){

        this.viewPager = viewPager

        hasViewPager = true

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                onPageListener?.onViewPagerPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                onPageListener?.onViewPagerPageScrolled(position,positionOffset,positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {

                onPageListener?.onViewPagerPageSelected(position)

                if(position > lastPosition) {

                    if (stepsImages.lastIndex == selectedStep) return
                    selectedStep++

                    goToStep(selectedStep)

                    lastPosition = position

                }else if(position < lastPosition) {

                    if (selectedStep == START_STEP) return
                    selectedStep--
                    goToStep(selectedStep)

                    lastPosition = position
                }
            }

        })
    }

    fun addOnViewPagerChangeListener(listener : OnViewPagerChangeListener){
        this.onPageListener = listener
    }

    interface OnViewPagerChangeListener{
        fun onViewPagerPageScrollStateChanged(state: Int)
        fun onViewPagerPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)
        fun onViewPagerPageSelected(position: Int)
    }


}