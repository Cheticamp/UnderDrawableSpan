package com.example.underdrawablespan

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dotDrawable = ContextCompat.getDrawable(this, R.drawable.gradient_drawable)!!
        setUnderDrawable(R.id.textViewLtr1, 0, 1, dotDrawable, 20, 20)
        setUnderDrawable(R.id.textViewLtr2, 1, 2, dotDrawable, 20, 20)
        setUnderDrawable(R.id.textViewLtr3, 2, 3, dotDrawable, 20, 20)

        setUnderDrawable(R.id.textViewLtr4, 0, 1, dotDrawable, 5, 5, 0)
        setUnderDrawable(R.id.textViewLtr4, 1, 2, dotDrawable, 5, 5, 0)
        setUnderDrawable(R.id.textViewLtr4, 2, 3, dotDrawable, 5, 5, 0)

        setUnderDrawable(R.id.textViewLtr5, 0, 1, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewLtr5, 1, 2, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewLtr5, 2, 3, dotDrawable, 5, 5, 8)

        setUnderDrawable(R.id.textViewLtr6, 0, 1, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewLtr6, 1, 2, dotDrawable, 20, 20, 8)
        setUnderDrawable(R.id.textViewLtr6, 2, 3, dotDrawable, 5, 5, 8)

        setUnderDrawable(R.id.textViewRtl1, 0, 1, dotDrawable, 20, 20)
        setUnderDrawable(R.id.textViewRtl2, 1, 2, dotDrawable, 20, 20)
        setUnderDrawable(R.id.textViewRtl3, 2, 3, dotDrawable, 20, 20)

        setUnderDrawable(R.id.textViewRtl4, 0, 1, dotDrawable, 5, 5, 0)
        setUnderDrawable(R.id.textViewRtl4, 1, 2, dotDrawable, 5, 5, 0)
        setUnderDrawable(R.id.textViewRtl4, 2, 3, dotDrawable, 5, 5, 0)

        setUnderDrawable(R.id.textViewRtl5, 0, 1, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewRtl5, 1, 2, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewRtl5, 2, 3, dotDrawable, 5, 5, 8)

        setUnderDrawable(R.id.textViewRtl6, 0, 1, dotDrawable, 5, 5, 8)
        setUnderDrawable(R.id.textViewRtl6, 1, 2, dotDrawable, 20, 20, 8)
        setUnderDrawable(R.id.textViewRtl6, 2, 3, dotDrawable, 5, 5, 8)
    }

    private fun setUnderDrawable(
        @IdRes id: Int,
        start: Int,
        end: Int,
        drawable: Drawable,
        width: Int,
        height: Int,
        margin: Int = 8
    ) {
        val textView = findViewById<TextView>(id)
        val spannable = ensureSpannable(textView)
        spannable.setSpan(
            UnderDrawableSpan(this, drawable, width, height, margin),
            start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    private fun ensureSpannable(tv: TextView): Spannable {
        if (tv.text !is Spannable) {
            tv.setText(SpannableString(tv.text), TextView.BufferType.SPANNABLE)
        }
        return tv.text as Spannable
    }

    fun onClick(view: View) {
        Toast.makeText(this, "Does nothing...but it could.", Toast.LENGTH_LONG).show()
    }
}