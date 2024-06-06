package ru.diplomnaya.skilllcinema.presentation.intro

import android.view.View
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.diplomnaya.skilllcinema.R

class IntroActivityTest{

    @JvmField
    @Rule
 val introActivityTestRule = ActivityTestRule(IntroActivity::class.java)
 var introActivity: IntroActivity? =null

    @Before
//    @Throws(Exception::class)
    fun setUpp() {
        introActivity=introActivityTestRule.activity
    }
    @Test
    fun testLaunch(){
        val view: View = introActivity!!.findViewById(R.id.viewpager)
        assertNotNull(view )
    }
    @After
//    @Throws(Exception::class)
    fun tearDown(){
        introActivity=null
    }


}