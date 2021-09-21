package com.udacity

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import kotlin.properties.Delegates

private const val END_ROTATION_DEGREE = 360
private const val PERCENTAGE = 100f
private const val PERCENTAGE_VALUE_HOLDER = "percentage"
private const val START_ROTATION_DEGREE = 0f

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var downloadingButtonPaint: Paint
    private var downloadingCirclePaint: Paint
    private var buttonTextPaint: Paint
    private var buttonBackgroundPaint: Paint
    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0
    private var widthSize = resources.getDimension(R.dimen.buttonWidth)
    private var heightSize = resources.getDimension(R.dimen.buttonHeight)
    private var buttonLabel: String?
    private val animator = ValueAnimator()
    private val buttonRectF = RectF(0f, 0f, widthSize, heightSize)
    private var currentPercentage = 0
    private var attrsArray =
        context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed)
    { _, _, new: ButtonState ->
        when (new) {
            ButtonState.Loading -> beginAnimation()
            ButtonState.Completed -> endAnimation()
        }
    }

    private fun endAnimation() {
        buttonLabel = resources.getString(R.string.download_button_label)
        animator.end()
        currentPercentage = 0

    }

    // Circle animation implementation concept is courtesy of Paul Nunez
    // https://medium.com/@paulnunezm/
    // canvas-animations-simple-circle-progress-view-on-android-8309900ab8ed

    private fun beginAnimation() {
        buttonLabel = resources.getString(R.string.button_loading_label)
        //remove from memory the attrsArray as we already extracted the colors from it
        val valuesHolder = PropertyValuesHolder.ofFloat("percentage", 0f, 100f)
        animator.apply {
            setValues(valuesHolder)
            duration = 3000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener {
                val animationProgress = it.getAnimatedValue(PERCENTAGE_VALUE_HOLDER) as Float
                currentPercentage = animationProgress.toInt()
                invalidate()
            }
        }
        animator.start()


    }

    init {
        isClickable = true
    }

    init {
        buttonBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            buttonBackgroundColor = attrsArray.getColor(
                R.styleable.LoadingButton_lb_background_color,
                resources.getColor(R.color.colorPrimary, null)
            )
            color = buttonBackgroundColor
            style = Paint.Style.FILL


        }

        buttonTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            buttonLabel = attrsArray.getString(R.styleable.LoadingButton_lb_text)
                ?: resources.getString(R.string.download_button_label)
            buttonTextColor = attrsArray.getColor(
                R.styleable.LoadingButton_lb_text_color,
                resources.getColor(R.color.white, null)
            )
            color = buttonTextColor
            textSize = resources.getDimension(R.dimen.default_text_size)
            textAlign = Paint.Align.CENTER


        }

        downloadingCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = resources.getColor(R.color.colorAccent, null)
            style = Paint.Style.FILL

        }
        downloadingButtonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = resources.getColor(R.color.colorPrimaryDark, null)
            style = Paint.Style.FILL

        }
        //recycle the attributes array to free resources,
        // as we already used it to expose our custom button attributes to the xml layout
        attrsArray.recycle()


    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawDefaultButtonBackground()
            drawLoadingButton()
            drawLoadingCicle()
            drawButtonText()
        }
    }

    private fun Canvas.drawDefaultButtonBackground() {
        drawRect(buttonRectF, buttonBackgroundPaint)

    }

    private fun Canvas.drawButtonText() {
        val textHeight: Float = buttonTextPaint.descent() - buttonTextPaint.ascent()
        val textOffset: Float = textHeight / 2 - buttonTextPaint.descent()
        drawText(
            buttonLabel as String,
            widthSize / 2, heightSize / 2 + textOffset, buttonTextPaint
        )
    }

    private fun Canvas.drawLoadingButton() {
        val loadingButtonFillPercentage = buttonLoadingPercentage()
        drawRect(
            0f,
            0f,
            loadingButtonFillPercentage,
            heightSize,
            downloadingButtonPaint
        )
    }

    private fun Canvas.drawLoadingCicle() {
        val circleFillPercentage = circleLoadingPercentage()
        drawArc(
            (widthSize - 200f),
            (heightSize / 2) - 40f,
            (widthSize - 120f),
            (heightSize / 2) + 40f,
            START_ROTATION_DEGREE,
            circleFillPercentage,
            true,
            downloadingCirclePaint

        )

    }


    private fun circleLoadingPercentage() =
        (END_ROTATION_DEGREE * (currentPercentage / PERCENTAGE))

    private fun buttonLoadingPercentage() = (widthSize * (currentPercentage / PERCENTAGE))


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        this.widthSize = w.toFloat()
        heightSize = h.toFloat()
        setMeasuredDimension(w, h)
    }


}