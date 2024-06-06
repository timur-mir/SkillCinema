package ru.diplomnaya.skilllcinema.presentation.intro

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class CompositionScreen(
    @StringRes val textRes:Int,
    @ColorRes val bgColorRes:Int,
    @DrawableRes val drawableRes: Int
)
