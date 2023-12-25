package com.app.matrimony.utils.apputils

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.app.matrimony.R

object BindingComponent {

    @BindingAdapter("startingIcon")
    @JvmStatic
    fun setImageResource(textView: TextView, resource: Int) {
        textView.setCompoundDrawablesWithIntrinsicBounds(
            resource,
            0,
            R.drawable.ic_arrow_right,
            0
        )
    }

    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImageView(imageView: ImageView, uri: String) {
        imageView.load(Uri.parse(uri)) {
        }
    }

}