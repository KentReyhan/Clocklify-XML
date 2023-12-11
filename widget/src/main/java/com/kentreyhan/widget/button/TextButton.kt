package com.kentreyhan.widget.button

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.kentreyhan.widget.R
import com.kentreyhan.widget.databinding.TextButtonBinding

class TextButton(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    var binding: TextButtonBinding = TextButtonBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultTextColor = ResourcesCompat.getColor(
        context.resources,
        R.color.lavender, null
    )

    var labelText: CharSequence?
        get() = binding.textButton.text
        set(value) {
            if (!value.isNullOrEmpty()) {
                binding.textButton.text = value
                binding.textButton.visibility = View.VISIBLE
                binding.textButton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            } else {
                binding.textButton.visibility = View.GONE
            }
        }

    var textColor: Int = binding.textButton.currentTextColor
        set(value) {
            field = value
            binding.textButton.setTextColor(textColor)
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TextButton)
        try {
            labelText = attributes.getString(R.styleable.TextButton_labelTextButton)
            textColor = attributes.getColor(R.styleable.TextButton_labelTextButtonColor, defaultTextColor)

        } finally {
            attributes.recycle()
        }
    }
}