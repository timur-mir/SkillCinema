package ru.diplomnaya.skilllcinema

import android.os.Bundle
import androidx.fragment.app.Fragment

fun <T : Fragment> T.putArguments(action: Bundle.() -> Unit): T {
    return apply {
        val args: Bundle = Bundle().apply(action)
        arguments = args
    }


}