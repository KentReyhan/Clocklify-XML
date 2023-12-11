package com.kentreyhan.widget.textfield

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import com.kentreyhan.widget.R
import com.kentreyhan.widget.databinding.CustomTextFieldBinding

class CustomTextField(context: Context, attributeSet: AttributeSet?) :
    ConstraintLayout(context, attributeSet) {
    var binding: CustomTextFieldBinding = CustomTextFieldBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var hint: CharSequence?
        get() = binding.customTextField.hint
        set(value) {
            binding.customTextField.hint = value
        }
    var inputType: Int = DEFAULT_INPUT_TYPE
        set(value) {
            field = value
            val typeface = binding.customTextField.typeface
            binding.customTextField.inputType = value
            binding.customTextField.typeface = typeface
            if (value == InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD || value == InputType
                .TYPE_CLASS_NUMBER + InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
                binding.customTextFieldLayout.endIconMode =
                    TextInputLayout.END_ICON_PASSWORD_TOGGLE
                binding.customTextFieldLayout.endIconDrawable =
                    ContextCompat.getDrawable(context, R.drawable.selector_password_toggle)
            }
        }

    var imeOptions: Int
        get() = binding.customTextField.imeOptions
        set(value) {
            binding.customTextField.imeOptions = value
        }

    var text: String
        get() = binding.customTextField.text.toString()
        set(value) {
            binding.customTextField.setText(value)
        }

    var errorText: CharSequence?
        get() = binding.errorText.text
        set(value) {
            if(!value.isNullOrEmpty()){
                binding.errorText.visibility = View.VISIBLE
            }
            binding.errorText.text = value
        }


    inline fun addTextChangedListener(crossinline afterTextChanged: (text: Editable?) -> Unit = {}) {
        binding.customTextField.addTextChangedListener {
            afterTextChanged(it)
        }
    }

    init {
        val attributes =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomTextField)

        try {
            val hintValue = attributes.getString(R.styleable.CustomTextField_android_hint)
            hintValue?.let {
                hint = it
            }
            inputType = attributes.getInteger(
                R.styleable.CustomTextField_android_inputType,
                DEFAULT_INPUT_TYPE
            )
            imeOptions = attributes.getInteger(
                R.styleable.CustomTextField_android_imeOptions,
                EditorInfo.IME_ACTION_NEXT
            )
            val textValue = attributes.getString(R.styleable.CustomTextField_android_text)
            textValue?.let {
                text = it
            }
            val errorValue = attributes.getString(R.styleable.CustomTextField_errorText)
            errorValue?.let {
                errorText = it
            }
        } finally {
            attributes.recycle()
        }
    }

    companion object {
        private const val DEFAULT_INPUT_TYPE = InputType.TYPE_TEXT_FLAG_CAP_WORDS
    }
}