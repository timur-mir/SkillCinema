package ru.diplomnaya.skilllcinema.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import ru.diplomnaya.skilllcinema.databinding.CustomCollectionViewBinding

class CustomDataPicker@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAtrr:Int=0
): LinearLayout(context,attrs,defStyleAtrr) {
    private val cv= CustomCollectionViewBinding.inflate(LayoutInflater.from(context))
    init {
        addView(cv.root)
    }

}