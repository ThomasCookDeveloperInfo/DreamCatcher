package com.thomascook.dreamcatcher.application

import android.databinding.BindingAdapter
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView

/**
 * Used by binding adapter to implement custom UI properties
 */
@BindingAdapter("font")
fun setFont(textView: TextView, fontName: String?) {
    if (fontName != null) {
        textView.typeface = Typeface.createFromAsset(textView.context.assets, "fonts/$fontName")
    }
}

/**
 * The function is used to provide image view with setting image resource including vector drawables
 * @param imageView Image view.
 * @param resId     The image resource id
 */
@BindingAdapter("compatSrc")
fun setImageViewImage(imageView: ImageView, resId: Int) = imageView.setImageResource(resId)