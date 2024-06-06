package ru.diplomnaya.skilllcinema

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo


class SomeClassNameTest {
    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }
    @Test
    fun TestSomeClassName(){
        var mockf: FilmDetailInfo = Mockito.mock(FilmDetailInfo::class.java)
        val mockCreationSettings = Mockito.mockingDetails(mockf).mockCreationSettings
        Assert.assertEquals(FilmDetailInfo::class.java, mockCreationSettings.typeToMock)
    }

}
