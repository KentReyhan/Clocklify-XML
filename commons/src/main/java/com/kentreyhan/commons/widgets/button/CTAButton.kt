package com.kentreyhan.commons.widgets.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import com.kentreyhan.commons.R
import com.kentreyhan.commons.databinding.CtaButtonBinding

class CTAButton(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
    var binding: CtaButtonBinding = CtaButtonBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultTextColor = ResourcesCompat.getColor(
        context.resources,
        R.color.white, null
    )

    private val defaultBackgroundDrawable = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.background_light_blue_rounded, null
    )

    var labelText: CharSequence?
        get() = binding.ctaButton.text
        set(value) {
            if (!value.isNullOrEmpty()) {
                binding.ctaButton.text = value
                binding.ctaButton.visibility = View.VISIBLE
            } else {
                binding.ctaButton.visibility = View.GONE
            }
        }

    var textColor: Int = defaultTextColor
        set(value) {
            field = value
            binding.ctaButton.setTextColor(textColor)
        }

    var buttonBackgroundDrawable: Drawable? = defaultBackgroundDrawable
        set(value) {
            field=value
            binding.ctaButtonBackground.background = value
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CTAButton)
        try {
            labelText = attributes.getString(R.styleable.CTAButton_labelActionButton)
            textColor = attributes.getColor(R.styleable.CTAButton_labelActionButtonColor, defaultTextColor)
            val background = attributes.getDrawable(R.styleable.CTAButton_labelActionButtonBackground)
            buttonBackgroundDrawable = if(background==null){
                defaultBackgroundDrawable
            } else {
                attributes.getDrawable(R.styleable.CTAButton_labelActionButtonBackground)
            }

        } finally {
            attributes.recycle()

        }
    }
}