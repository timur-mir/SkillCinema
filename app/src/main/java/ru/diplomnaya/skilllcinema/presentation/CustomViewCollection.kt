package ru.diplomnaya.skilllcinema.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import ru.diplomnaya.skilllcinema.databinding.CustomCollectionViewBinding

class CustomViewCollection@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAtrr:Int=0
): LinearLayout(context,attrs,defStyleAtrr) {
private val cv=CustomCollectionViewBinding.inflate(LayoutInflater.from(context))
    init {
        addView(cv.root)

    }

    fun setTextForNameCollection(text:String){
        cv.textViewCol.text=text
    }
    fun getNameCollection():String{
        return cv.textViewCol.text.toString()
    }
    fun setImageCollection(imageRes:Int){
        cv.iconViewCol.setImageResource(imageRes)
    }
    fun setAmountFilmInCollection(text:String){
        cv.buttonViewCollection.text=text
    }

    fun setDeleteCollectionButtonVisibility(){
        cv.deleteCollection.visibility= View.GONE
           }

}