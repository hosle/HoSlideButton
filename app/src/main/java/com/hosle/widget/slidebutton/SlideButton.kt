package com.hosle.widget.slidebutton

import android.content.Context
import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by tanjiahao on 2018/10/23
 * Original Project HoSlideButton
 */
open class SlideButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val floatView: View
    private val floatViewLayoutParams:LayoutParams

    private val textView:TextView

    private val radioAutoScroll = 0.66
    private val radius2 = 45f

    init {
        layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)

        isClickable = false

        val txtLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER_VERTICAL
            marginStart = context.dp2px(radius2) - context.dp2px(8f)
        }
        textView = TextView(context)
        textView.gravity = Gravity.CENTER
        this.addView(textView,txtLayoutParams)


        floatViewLayoutParams = LayoutParams(context.dp2px(radius2), context.dp2px(radius2)).apply {
            gravity = Gravity.CENTER_VERTICAL
            marginStart = context.dp2px(5f)
            marginEnd = context.dp2px(5f)
        }

        floatView = ImageView(context).apply {
            layoutParams = floatViewLayoutParams
            setImageResource(R.drawable.button_round_shape)
        }
        this.addView(floatView)

        this.setBackgroundResource(R.drawable.bg_slide_btn_green)

    }

    fun setText(text:CharSequence){
        textView.text = text
    }

    fun setTextSize(size:Float){
        textView.textSize = size
    }

    fun setTextSize(unit :Int, size:Float){
        textView.setTextSize(unit,size)
    }

    fun setTextColor(@ColorInt resId:Int){
        textView.setTextColor(resId)
    }

    fun setTextStyle(tf:Typeface){
        textView.typeface = tf
    }

    fun setGravity(gravity:Int){
        textView.gravity = gravity
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        when(event.action){
            MotionEvent.ACTION_DOWN ->{
                parent.requestDisallowInterceptTouchEvent(true)

                if (event.x in floatView.left..floatView.right && event.y in floatView.top..floatView.bottom) {
                    return super.dispatchTouchEvent(event)
                } else {
                    return false
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }

    private var lastX:Float = 0f
    private var lastY:Float = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){
            MotionEvent.ACTION_MOVE -> {

                val deltaX = event.x - lastX
                val deltaY = event.y - lastY

                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    //move
                    val distanceX = Math.max(floatViewLayoutParams.marginStart - floatView.left,
                            Math.min(deltaX.toInt(), measuredWidth - floatView.right - floatViewLayoutParams.marginEnd))

                    val newFloatViewLeft = floatView.left + distanceX
                    floatView.layout(newFloatViewLeft, floatView.top, floatView.right + distanceX, floatView.bottom)
                    notifyTextAlpha(newFloatViewLeft)
                }
            }

            MotionEvent.ACTION_UP -> {
                if (event.x < measuredWidth * radioAutoScroll) {
                    autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
                } else {
                    autoScrollToEnd((measuredWidth - floatView.right - floatViewLayoutParams.marginEnd).toFloat())
                }
            }

            MotionEvent.ACTION_CANCEL ->{
                autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
            }
        }
        lastX = event.x
        lastY = event.y

        return true
    }

    private fun notifyTextAlpha(floatViewLeft:Int){
        textView.alpha = 1 - (floatViewLeft / measuredWidth.toFloat())
    }

    fun slideToStartPos(){
        autoScrollToStart(floatViewLayoutParams.marginStart - floatView.left.toFloat())
    }

    private fun autoScrollToEnd(deltaX: Float){
        autoScroll(deltaX,true)
    }

    private fun autoScrollToStart(deltaX: Float){
        autoScroll(deltaX,false)
    }

    private fun autoScroll(deltaX:Float,activated:Boolean){
        val translateAnimation = TranslateAnimation(0f,deltaX,0f,0f)
        translateAnimation.duration = 100
        translateAnimation.setAnimationListener(object :Animation.AnimationListener{
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                floatView.clearAnimation()

                val floatViewLeft = floatView.left + deltaX.toInt()
                floatView.layout(floatViewLeft, floatView.top, floatView.right + deltaX.toInt(), floatView.bottom)
                notifyTextAlpha(floatViewLeft)

                if(activated){
                    callOnClick()
                }
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })
        floatView.startAnimation(translateAnimation)

    }
}