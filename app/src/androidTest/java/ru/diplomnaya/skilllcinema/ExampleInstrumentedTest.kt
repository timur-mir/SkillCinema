package ru.diplomnaya.skilllcinema

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented custom_year_picker_search_fragment, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under custom_year_picker_search_fragment.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.diplomnaya.skilllcinema", appContext.packageName)
    }
}