package com.example.underdrawablespan

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.text.style.LineHeightSpan
import android.text.style.ReplacementSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.core.graphics.withSave

/**
 * Place a drawable at the bottom center of text within a span. Because this class is extended
 * from [ReplacementSpan], the span must reside on a single line and cannot span lines.
 */
class UnderDrawableSpan(
    context: Context, drawable: Drawable, drawableWidth: Int, drawableHeight: Int, margin: Int
) : ReplacementSpan(), LineHeightSpan.WithDensity {
    // The image to draw under the spanned text. The image and text will be horizontally centered.
    private val mDrawable: Drawable

    // The width if the drawable in dip
    private var mDrawableWidth: Int

    // The width if the drawable in dip
    private var mDrawableHeight: Int

    // Margin in dip to place around the drawable
    private var mMargin: Int

    // Amount to offset the text from the start.
    private var mTextOffset = 0f

    // Amount to offset the drawable from the start.
    private var mDrawableOffset = 0f

    // Descent specified in font metrics of the TextPaint.
    private var mBaseDescent = 0f

    init {
        val metrics: DisplayMetrics = context.resources.displayMetrics

        mDrawable = drawable
        mDrawableWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, drawableWidth.toFloat(), metrics
        ).toInt()
        mDrawableHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, drawableHeight.toFloat(), metrics
        ).toInt()
        mMargin = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, margin.toFloat(), metrics
        ).toInt()
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int,
        bottom: Int, paint: Paint
    ) {
        canvas.drawText(text, start, end, x + mTextOffset, y.toFloat(), paint)

        mDrawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight)
        canvas.withSave {
            canvas.translate(x + mDrawableOffset + mMargin, y + mBaseDescent + mMargin)
            mDrawable.draw(canvas)
        }
    }

    // ReplacementSpan override to determine the width that the text and drawable should occupy.
    // The computed width is determined by the greater of the text width and the drawable width
    // plus the requested margins.
    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?
    ): Int {
        val textWidth = paint.measureText(text, start, end)
        val additionalWidthNeeded = mDrawableWidth + mMargin * 2 - textWidth

        // If the width of the text is less than the width of our drawable, increase the text width
        // to match the drawable's width; otherwise, just return the width of the text.
        return if (additionalWidthNeeded >= 0) {
            // Drawable is wider than text, so we need to offset the text to center it.
            mTextOffset = additionalWidthNeeded / 2
            textWidth + additionalWidthNeeded
        } else {
            // Text is wider than the drawable, so we need to offset the drawable to center it.
            // We do not need to expand the width.
            mDrawableOffset = -additionalWidthNeeded / 2
            textWidth
        }.toInt()
    }

    // Determine the height for the ReplacementSpan.
    override fun chooseHeight(
        text: CharSequence?, start: Int, end: Int, spanstartv: Int, lineHeight: Int,
        fm: Paint.FontMetricsInt, paint: TextPaint
    ) {
        // The text height must accommodate the size of the drawable. To make the accommodation,
        // change the bottom of the font so there is enough room to fit the drawable between the
        // font bottom and the font's descent.
        val tpMetric = paint.fontMetrics

        mBaseDescent = tpMetric.descent
        val spaceAvailable = fm.descent - mBaseDescent
        val spaceNeeded = mDrawableHeight + mMargin * 2

        if (spaceAvailable < spaceNeeded) {
            fm.descent += (spaceNeeded - spaceAvailable).toInt()
            fm.bottom = fm.descent + (tpMetric.bottom - tpMetric.descent).toInt()
        }
    }

    // StaticLayout prefers LineHeightSpan.WithDensity over this function.
    override fun chooseHeight(
        charSequence: CharSequence?, i: Int, i1: Int, i2: Int, i3: Int, fm: Paint.FontMetricsInt
    ) = throw IllegalStateException("LineHeightSpan.chooseHeight() called but is not supported.")
}