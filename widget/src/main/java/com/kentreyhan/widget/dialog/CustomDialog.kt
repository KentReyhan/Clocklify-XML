package com.kentreyhan.widget.dialog

import android.app.Dialog
import android.content.Context
import com.kentreyhan.widget.databinding.CustomDialogBinding

class CustomDialog (
        context: Context,
        private val title: String,
        private val description: String = "",
    ) : Dialog(context) {

    val binding = CustomDialogBinding.inflate(layoutInflater, null, false)

    init {
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout() {
        binding.titleDialog.text = title
        binding.descriptionDialog.text = description
    }
}