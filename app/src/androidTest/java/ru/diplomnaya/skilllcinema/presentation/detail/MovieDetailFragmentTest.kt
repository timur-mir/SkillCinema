package ru.diplomnaya.skilllcinema.presentation.detail

import android.content.Intent
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.presentation.actors.ActorFaceFragment

@RunWith(AndroidJUnit4::class)
class MovieDetailFragmentTest: TestCase() {

    private var scenario: FragmentScenario<MovieDetailFragment>? = null

    @Before
    fun setup() {
        var genres= arrayListOf<Genre>(Genre("фентези"))
        var countries= arrayListOf<Country>(Country("Россия"))
        val origin:Movie=Movie(4536580,"Баба Яга спасает мир","https://kinopoiskapiunofficial.tech/images/posters/kp/4536580.jpg",
            "https://kinopoiskapiunofficial.tech/images/posters/kp_small/4536580.jpg", genres ,"2023-08-03",
        countries ,"7.3",false,4536580)
        val bundle = bundleOf("movieDetailInfo" to origin)
        scenario =
            launchFragmentInContainer(fragmentArgs = bundle, themeResId = R.style.Theme_Skillcinema)
        scenario!!.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun sendUrlFilmPerf() {
        Intents.init()
        val expected = allOf(hasAction(Intent.ACTION_SEND))
        Espresso.onView(withId(R.id.share)).perform(click())
        intended(chooser(expected))
        Intents.release()
    }
    fun chooser(matcher: Matcher<Intent>): Matcher<Intent> {
        return allOf(
            hasAction(Intent.ACTION_CHOOSER),
            hasExtra(`is`(Intent.EXTRA_INTENT), matcher))
    }
//    private val expectedIntent = Matchers.allOf(
//        IntentMatchers.hasAction(Intent.ACTION_SEND),
//        IntentMatchers.hasExtra("Your key", "Watch ${dummyMovieData.title} with me!\n\n${dummyMovieData.summary}"),
//        IntentMatchers.hasType("text/plain")
//    )
}