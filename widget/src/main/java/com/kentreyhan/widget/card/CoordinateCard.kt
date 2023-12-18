package com.kentreyhan.widget.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.kentreyhan.widget.R
import com.kentreyhan.widget.databinding.CoordinateCardBinding

class CoordinateCard (context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    var binding: CoordinateCardBinding = CoordinateCardBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var text: CharSequence?
        get() = binding.coordinateText.text
        set(value) {
            binding.coordinateText.text = value
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CoordinateCard)
        try {
            text= attributes.getString(R.styleable.CoordinateCard_coordinateText)

        } finally {
            attributes.recycle()
        }
    }
}