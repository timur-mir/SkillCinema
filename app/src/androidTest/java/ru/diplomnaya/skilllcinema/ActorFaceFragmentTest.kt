package ru.diplomnaya.skilllcinema

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ru.diplomnaya.skilllcinema.presentation.actors.ActorFaceFragment
import ru.diplomnaya.skilllcinema.presentation.actors.ActorFaceFragmentArgs


@RunWith(AndroidJUnit4::class)
class ActorFaceFragmentTest: TestCase() {
    private  var scenario: FragmentScenario<ActorFaceFragment>? = null

    @Before
    fun setup() {
        val origin =String()
        val bundle = bundleOf("ru.diplomnaya.skilllcinema.presentation.actors.ActorFaceFragment" to origin)
        scenario = launchFragmentInContainer(fragmentArgs =bundle,themeResId = R.style.Theme_Skillcinema)
        scenario!!.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun checkTitle() {
        Espresso.onView(ViewMatchers.withText("Actor face"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

}