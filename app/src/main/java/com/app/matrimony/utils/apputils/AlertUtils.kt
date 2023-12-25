package com.app.matrimony.utils.apputils

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager.LayoutParams
import com.app.matrimony.databinding.LayoutAlertBinding

object AlertUtils {

    fun alertDialogTwoAction(
        context: Context,
        title: String,
        msg: String,
        callback: (Boolean) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val binding = LayoutAlertBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        binding.title = title
        binding.message = msg
        binding.tvPositive.setOnClickListener {
            dialog.dismiss()
            callback.invoke(true)
        }
        binding.tvNegative.setOnClickListener {
            dialog.dismiss()
            callback.invoke(false)
        }
        dialog.show()
        dialog.window?.apply {
            setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        }
    }

}