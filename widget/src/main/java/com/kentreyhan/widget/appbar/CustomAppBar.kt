package com.kentreyhan.widget.appbar

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.kentreyhan.widget.R
import com.kentreyhan.widget.databinding.CustomAppBarBinding

class CustomAppBar(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {

    var binding: CustomAppBarBinding = CustomAppBarBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val defaultTextColor = ResourcesCompat.getColor(
        context.resources,
        R.color.primary, null
    )

    private val defaultBackButtonDrawable = ResourcesCompat.getDrawable(
        context.resources,
        R.drawable.ic_back, null
    )

    var textColor: Int = defaultTextColor
        set(value) {
            field = value
            binding.appBarTitle.setTextColor(textColor)
        }

    var backButtonDrawable: Drawable? = defaultBackButtonDrawable
        set(value) {
            field=value
            binding.appBarButtonBack.setImageDrawable(value)
        }

    var title: CharSequence
        get() = binding.appBarTitle.text
        set(value) {
            binding.appBarTitle.text = value
        }

    var backButtonVisibility: Int
        get() = binding.appBarButtonBack.visibility
        set(value) {
            binding.appBarTitle.visibility = value
        }

    private var backClickListener: OnClickListener? = null

    init {
        val attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomAppBar)

        try {
            title = attributes.getString(R.styleable.CustomAppBar_appBarTitle) ?: ""
            textColor = attributes.getColor(R.styleable.CustomAppBar_appBarTitleColor, defaultTextColor)
            backButtonDrawable = attributes.getDrawable(R.styleable.CustomAppBar_backButtonDrawable)
            backButtonVisibility = attributes.getInteger(
                R.styleable.CustomAppBar_backButtonVisibility,
                View.VISIBLE
            )
        } finally {
            attributes.recycle()

        }

        binding.appBarButtonBack.setOnClickListener {
            if (backClickListener == null) {
                if (context is Activity) {
                    context.finish()
                }
            } else {
                backClickListener?.onClick(it)
            }
        }


    }

    fun setOnBackClickListener(listener: OnClickListener) {
        backClickListener = listener
    }

}