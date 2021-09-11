package com.udacity

import android.animation.ValueAnimator
import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var buttonBackgroundColor = 0
    private var buttonTextColor = 0
    private var widthSize = resources.getDimension(R.dimen.buttonWidth)
    private var heightSize = resources.getDimension(R.dimen.buttonHeight)
    private val valueAnimator = ValueAnimator()
    private var buttonLabel: String?
    private val rect = RectF(0f, 0f, widthSize, heightSize)
    private val attrsArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)

    var buttonState: ButtonState by Delegates.observable(ButtonState.Completed)
    { _, _, new: ButtonState ->
//        when(new){
//            ButtonState.Loading -> animationStart()
//            ButtonState.Completed -> animationStop()
//        }

    }

//    private fun animationStop() {
//        TODO("Not yet implemented")
//    }
//
//    private fun animationStart() {
//        TODO("Not yet implemented")
//    }

    private val buttonBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        buttonBackgroundColor = attrsArray.getColor(
            R.styleable.LoadingButton_lb_background_color,
            resources.getColor(R.color.colorPrimary, null)
        )
        style = Paint.Style.FILL


    }

    private val buttonTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        buttonLabel = attrsArray.getString(R.styleable.LoadingButton_lb_text)
            ?: resources.getString(R.string.download_button_label)
        buttonTextColor = attrsArray.getColor(
            R.styleable.LoadingButton_lb_text_color,
            resources.getColor(R.color.white, null)
        )
        textSize = resources.getDimension(R.dimen.default_text_size)
        textAlign = Paint.Align.CENTER


    }

    fun DownloadManager.calculateDownloadPercentage( id: Long,contentResolver: ContentResolver) {
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = query(query)
        if (cursor.moveToFirst()) {
            val sizeIndex = cursor.getColumnIndex(
                DownloadManager.COLUMN_TOTAL_SIZE_BYTES
            )
            val downloadedIndex = cursor.getColumnIndex(
                DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
            )
            val downloadSize = cursor.getInt(sizeIndex)
            val downloaded = cursor.getInt(downloadedIndex)

            val downloadProgress =
                if (downloadSize != -1) (downloaded * 100.0 / downloadSize).toFloat() else 0.0F

        }

    }


    init {
        isClickable = true
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            buttonBackgroundPaint.color = buttonBackgroundColor
            buttonTextPaint.color = buttonTextColor
            drawRect(rect, buttonBackgroundPaint)
            drawText(
                buttonLabel as String,
                widthSize / 2, heightSize / 2, buttonTextPaint
            )
        }
        attrsArray.recycle()


    }


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