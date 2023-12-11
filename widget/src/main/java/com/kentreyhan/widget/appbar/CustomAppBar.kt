package com.kentreyhan.widget.appbar

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.kentreyhan.widget.R
import com.kentreyhan.widget.databinding.CustomAppBarBinding

class CustomAppBar(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {

    var binding: CustomAppBarBinding = CustomAppBarBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

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
            title = attributes.getString(R.styleable.CustomAppBar_title) ?: ""
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