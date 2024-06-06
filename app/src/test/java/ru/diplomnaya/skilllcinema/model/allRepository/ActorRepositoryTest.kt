package ru.diplomnaya.skilllcinema.model.allRepository

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.network.MovieListApi

class ActorRepositoryTest {
    lateinit var mockWebServer: MockWebServer
    lateinit var movieListApi: MovieListApi

    @Before
    fun setup() {

        mockWebServer = MockWebServer()
        movieListApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(MovieListApi::class.java)
    }

    @Test
    fun testAboutInfoStaff() = runTest {
        val mockResponse = MockResponse()
        mockResponse.setBody( helpInfo.infoAboutActor)
        mockWebServer.enqueue(mockResponse)
        val response = movieListApi.getInfoAboutStaff(1208251)
        mockWebServer.takeRequest()
        Assert.assertEquals("https://www.kinopoisk.ru/name/1208251/", response.webUrl)
    }



    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }
}
object helpInfo{
    val infoAboutActor="{\"personId\":1208251,\"webUrl\":\"https://www.kinopoisk.ru/name/1208251/\",\"nameRu\":\"Глафира\n" +
            "Тарханова\",\"nameEn\":\"\",\"sex\":\"FEMALE\",\"posterUrl\":\"https://kinopoiskapiunofficial.tech/images/actor_posters/kp/1208251.jpg\",\"growth\":172,\"birthday\":\"1983-11-09\",\"death\":null,\"age\":39,\"birthplace\":\"Электросталь,\n" +
            "СССР (Россия)\",\"deathplace\":null,\"spouses\":[{\"personId\":1805592,\"name\":\"Алексей\n" +
            "Фаддеев\",\"divorced\":false,\"divorcedReason\":\"\",\"sex\":\"MALE\",\"children\":4,\"webUrl\":\"https://www.kinopoisk.ru/name/1805592/\",\"relation\":\"супруг\"}],\"hasAwards\":0,\"profession\":\"Актриса\",\"facts\":[\"Родители\n" +
            "— по образованию актёры кукольного театра, работали в Москонцерте.\",\"Окончила актёрский факультет Школы-студии МХАТ в\n" +
            "2005 году (курс Константина Райкина).\",\"Окончила психологический факультет\n" +
            "МГУ.\"],\"films\":[{\"filmId\":161032,\"nameRu\":\"Гибель\n" +
            "Империи\",\"nameEn\":null,\"rating\":\"8.0\",\"general\":false,\"description\":\"Таня\n" +
            "Зайцева\",\"professionKey\":\"ACTOR\"},{\"filmId\":251734,\"nameRu\":\"Громовы\",\"nameEn\":null,\"rating\":\"7.9\",\"general\":false,\"description\":\"Настя\n" +
            "Громова\",\"professionKey\":\"ACTOR\"},{\"filmId\":252008,\"nameRu\":\"Казароза\",\"nameEn\":null,\"rating\":\"6.8\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":277380,\"nameRu\":\"Пороки\n" +
            "и их поклонники\",\"nameEn\":null,\"rating\":\"6.9\",\"general\":false,\"description\":\"Маша\n" +
            "Тюрина\",\"professionKey\":\"ACTOR\"},{\"filmId\":279043,\"nameRu\":\"Три дня в\n" +
            "Одессе\",\"nameEn\":null,\"rating\":\"5.9\",\"general\":false,\"description\":\"Майя\",\"professionKey\":\"ACTOR\"},{\"filmId\":279771,\"nameRu\":\"Хроника\n" +
            "«Ада»\",\"nameEn\":null,\"rating\":\"6.0\",\"general\":false,\"description\":\"Людмила\",\"professionKey\":\"ACTOR\"},{\"filmId\":280167,\"nameRu\":\"Любовники\",\"nameEn\":null,\"rating\":\"7.4\",\"general\":true,\"description\":\"Дина\",\"professionKey\":\"ACTOR\"},{\"filmId\":280169,\"nameRu\":\"Бесы\",\"nameEn\":null,\"rating\":\"4.9\",\"general\":false,\"description\":\"Лиза\",\"professionKey\":\"ACTOR\"},{\"filmId\":400085,\"nameRu\":\"Женская\n" +
            "дружба\",\"nameEn\":null,\"rating\":\"6.1\",\"general\":false,\"description\":\"Наталья\",\"professionKey\":\"ACTOR\"},{\"filmId\":409777,\"nameRu\":\"Александровский\n" +
            "сад\n" +
            "2\",\"nameEn\":null,\"rating\":\"6.4\",\"general\":false,\"description\":\"Майя\",\"professionKey\":\"ACTOR\"},{\"filmId\":412378,\"nameRu\":\"Главный\n" +
            "калибр\",\"nameEn\":null,\"rating\":\"6.7\",\"general\":false,\"description\":\"Людмила\",\"professionKey\":\"ACTOR\"},{\"filmId\":416722,\"nameRu\":\"Морозов\",\"nameEn\":null,\"rating\":\"6.8\",\"general\":false,\"description\":\"Юля\n" +
            "Морозова\",\"professionKey\":\"ACTOR\"},{\"filmId\":425958,\"nameRu\":\"Охота на\n" +
            "Берию\",\"nameEn\":null,\"rating\":\"6.3\",\"general\":false,\"description\":\"Майя\n" +
            "Кольцова\",\"professionKey\":\"ACTOR\"},{\"filmId\":426061,\"nameRu\":\"Кружева\",\"nameEn\":null,\"rating\":\"6.7\",\"general\":false,\"description\":\"Евгения\n" +
            "Вершинина\",\"professionKey\":\"ACTOR\"},{\"filmId\":438347,\"nameRu\":\"Срочно в\n" +
            "номер\",\"nameEn\":null,\"rating\":\"5.4\",\"general\":false,\"description\":\"Валентина\",\"professionKey\":\"ACTOR\"},{\"filmId\":438350,\"nameRu\":\"Срочно\n" +
            "в номер\n" +
            "2\",\"nameEn\":null,\"rating\":\"5.2\",\"general\":false,\"description\":\"Валентина\",\"professionKey\":\"ACTOR\"},{\"filmId\":447648,\"nameRu\":\"Громовы.\n" +
            "Дом\n" +
            "надежды\",\"nameEn\":null,\"rating\":\"7.5\",\"general\":false,\"description\":\"Настя\",\"professionKey\":\"ACTOR\"},{\"filmId\":464868,\"nameRu\":\"Театральный\n" +
            "Блюз\",\"nameEn\":null,\"rating\":\"6.0\",\"general\":false,\"description\":\"Фиона\",\"professionKey\":\"ACTOR\"},{\"filmId\":465012,\"nameRu\":\"Силеньсио\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":471789,\"nameRu\":\"Три\n" +
            "женщины\n" +
            "Достоевского\",\"nameEn\":null,\"rating\":\"6.4\",\"general\":false,\"description\":\"Апполинария\",\"professionKey\":\"ACTOR\"},{\"filmId\":477468,\"nameRu\":\"Застывшие\n" +
            "депеши\",\"nameEn\":null,\"rating\":\"6.7\",\"general\":false,\"description\":\"Даша\",\"professionKey\":\"ACTOR\"},{\"filmId\":487267,\"nameRu\":\"Цвет\n" +
            "пламени\",\"nameEn\":null,\"rating\":\"6.1\",\"general\":false,\"description\":\"Алина\",\"professionKey\":\"ACTOR\"},{\"filmId\":491399,\"nameRu\":\"Чужие\n" +
            "души\",\"nameEn\":null,\"rating\":\"6.4\",\"general\":false,\"description\":\"Жанна\",\"professionKey\":\"ACTOR\"},{\"filmId\":493798,\"nameRu\":\"Путь\n" +
            "к\n" +
            "себе\",\"nameEn\":null,\"rating\":\"7.4\",\"general\":true,\"description\":\"Рада\",\"professionKey\":\"ACTOR\"},{\"filmId\":500561,\"nameRu\":\"Шуточка\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":532995,\"nameRu\":\"Цветы\n" +
            "от\n" +
            "Лизы\",\"nameEn\":null,\"rating\":\"6.3\",\"general\":false,\"description\":\"Лиза\",\"professionKey\":\"ACTOR\"},{\"filmId\":566599,\"nameRu\":\"С\n" +
            "приветом,\n" +
            "Козаностра\",\"nameEn\":null,\"rating\":\"6.4\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":579537,\"nameRu\":\"Белая\n" +
            "ворона\",\"nameEn\":null,\"rating\":\"6.8\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":592128,\"nameRu\":\"Чужие\n" +
            "крылья\",\"nameEn\":null,\"rating\":\"6.0\",\"general\":false,\"description\":\"Полина\",\"professionKey\":\"ACTOR\"},{\"filmId\":592367,\"nameRu\":\"Лучший\n" +
            "друг семьи\",\"nameEn\":null,\"rating\":\"6.9\",\"general\":false,\"description\":\"Вера в молодости / Таня, дочь Веры в наши\n" +
            "дни\",\"professionKey\":\"ACTOR\"},{\"filmId\":638850,\"nameRu\":\"Эта женщина ко\n" +
            "мне\",\"nameEn\":null,\"rating\":\"6.0\",\"general\":false,\"description\":\"Настя\n" +
            "Рогожкина\",\"professionKey\":\"ACTOR\"},{\"filmId\":661244,\"nameRu\":\"Сердце не\n" +
            "камень\",\"nameEn\":null,\"rating\":\"5.7\",\"general\":false,\"description\":\"Антонина\n" +
            "Погодина\",\"professionKey\":\"ACTOR\"},{\"filmId\":684235,\"nameRu\":\"Собачий\n" +
            "рай\",\"nameEn\":null,\"rating\":\"4.7\",\"general\":false,\"description\":\"Катя\",\"professionKey\":\"ACTOR\"},{\"filmId\":701565,\"nameRu\":\"Развод\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Наташа\",\"professionKey\":\"ACTOR\"},{\"filmId\":775506,\"nameRu\":\"Проверка\n" +
            "на любовь\",\"nameEn\":null,\"rating\":\"5.7\",\"general\":false,\"description\":\"Надя\n" +
            "Бурякова\",\"professionKey\":\"ACTOR\"},{\"filmId\":779057,\"nameRu\":\"Берега моей\n" +
            "мечты\",\"nameEn\":null,\"rating\":\"7.3\",\"general\":false,\"description\":\"Лена\",\"professionKey\":\"ACTOR\"},{\"filmId\":783249,\"nameRu\":\"Золотая\n" +
            "невеста\",\"nameEn\":null,\"rating\":\"5.8\",\"general\":false,\"description\":\"Надя\",\"professionKey\":\"ACTOR\"},{\"filmId\":789752,\"nameRu\":\"Счастливый\n" +
            "маршрут\",\"nameEn\":null,\"rating\":\"6.1\",\"general\":false,\"description\":\"Женя\",\"professionKey\":\"ACTOR\"},{\"filmId\":793512,\"nameRu\":\"Два\n" +
            "Ивана\",\"nameEn\":null,\"rating\":\"6.3\",\"general\":false,\"description\":\"Ольга\n" +
            "Круглова\",\"professionKey\":\"ACTOR\"},{\"filmId\":806645,\"nameRu\":\"Год в\n" +
            "Тоскане\",\"nameEn\":null,\"rating\":\"6.6\",\"general\":false,\"description\":\"Марина\",\"professionKey\":\"ACTOR\"},{\"filmId\":818569,\"nameRu\":\"Кураж\",\"nameEn\":null,\"rating\":\"3.2\",\"general\":false,\"description\":\"Вера,\n" +
            "жена\n" +
            "Дербенева\",\"professionKey\":\"ACTOR\"},{\"filmId\":839356,\"nameRu\":\"Измены\",\"nameEn\":null,\"rating\":\"8.0\",\"general\":false,\"description\":\"Даша\",\"professionKey\":\"ACTOR\"},{\"filmId\":840328,\"nameRu\":\"Папа\n" +
            "для Софии\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Рита\n" +
            "Серова\",\"professionKey\":\"ACTOR\"},{\"filmId\":861655,\"nameRu\":\"Слабая\n" +
            "женщина\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Людмила\n" +
            "Щербакова\",\"professionKey\":\"ACTOR\"},{\"filmId\":910095,\"nameRu\":\"По секрету всему\n" +
            "свету\",\"nameEn\":null,\"rating\":\"6.1\",\"general\":false,\"description\":\"Нюта\",\"professionKey\":\"ACTOR\"},{\"filmId\":949033,\"nameRu\":\"Семейное\n" +
            "счастье\",\"nameEn\":null,\"rating\":\"6.5\",\"general\":false,\"description\":\"Варя\",\"professionKey\":\"ACTOR\"},{\"filmId\":957842,\"nameRu\":\"Танцы\n" +
            "со звездами\",\"nameEn\":null,\"rating\":\"3.4\",\"general\":false,\"description\":\"играет саму\n" +
            "себя\",\"professionKey\":\"HERSELF\"},{\"filmId\":983091,\"nameRu\":\"Сказочный\n" +
            "патруль\",\"nameEn\":null,\"rating\":\"8.7\",\"general\":false,\"description\":\"Алиса,\n" +
            "озвучка\",\"professionKey\":\"ACTOR\"},{\"filmId\":1006709,\"nameRu\":\"Письмо\n" +
            "надежды\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Вера\",\"professionKey\":\"ACTOR\"},{\"filmId\":1009725,\"nameRu\":\"Перекрестки\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Алиса\",\"professionKey\":\"ACTOR\"},{\"filmId\":1045518,\"nameRu\":\"Любить\n" +
            "и\n" +
            "верить\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Светлана\",\"professionKey\":\"ACTOR\"},{\"filmId\":1048659,\"nameRu\":\"Благие\n" +
            "намерения\",\"nameEn\":null,\"rating\":\"5.8\",\"general\":false,\"description\":\"Инга, жена Семёна\n" +
            "Фирсова\",\"professionKey\":\"ACTOR\"},{\"filmId\":1049528,\"nameRu\":\"Блюз для\n" +
            "сентября\",\"nameEn\":null,\"rating\":\"5.7\",\"general\":false,\"description\":\"Наташа\n" +
            "Исаева\",\"professionKey\":\"ACTOR\"},{\"filmId\":1049661,\"nameRu\":\"Чудная\n" +
            "баба\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":1097416,\"nameRu\":\"Куда\n" +
            "течет\n" +
            "море\",\"nameEn\":null,\"rating\":\"6.3\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":1112778,\"nameRu\":\"Третий\n" +
            "должен уйти\",\"nameEn\":null,\"rating\":\"5.8\",\"general\":false,\"description\":\"Лиза\n" +
            "Андреева\",\"professionKey\":\"ACTOR\"},{\"filmId\":1162500,\"nameRu\":\"Ускользающая\n" +
            "жизнь\",\"nameEn\":null,\"rating\":\"6.8\",\"general\":false,\"description\":\"Альбина Виленская,\n" +
            "хирург\",\"professionKey\":\"ACTOR\"},{\"filmId\":1209745,\"nameRu\":\"Синичка\",\"nameEn\":null,\"rating\":\"7.2\",\"general\":false,\"description\":\"Ульяна\n" +
            "Синицына\",\"professionKey\":\"ACTOR\"},{\"filmId\":1209899,\"nameRu\":\"Синичка\n" +
            "2\",\"nameEn\":null,\"rating\":\"6.8\",\"general\":false,\"description\":\"Ульяна\n" +
            "Синицына\",\"professionKey\":\"ACTOR\"},{\"filmId\":1264376,\"nameRu\":\"100янов\",\"nameEn\":null,\"rating\":\"6.0\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":1272222,\"nameRu\":\"Паромщица\",\"nameEn\":null,\"rating\":\"6.3\",\"general\":false,\"description\":\"Надя\",\"professionKey\":\"ACTOR\"},{\"filmId\":1272407,\"nameRu\":\"Ведьма\",\"nameEn\":null,\"rating\":\"5.6\",\"general\":false,\"description\":\"Роза\",\"professionKey\":\"ACTOR\"},{\"filmId\":1379045,\"nameRu\":\"Синичка\n" +
            "4\",\"nameEn\":null,\"rating\":\"7.0\",\"general\":false,\"description\":\"Ульяна\",\"professionKey\":\"ACTOR\"},{\"filmId\":1379039,\"nameRu\":\"Синичка\n" +
            "3\",\"nameEn\":null,\"rating\":\"6.7\",\"general\":false,\"description\":\"Ульяна\",\"professionKey\":\"ACTOR\"},{\"filmId\":1379032,\"nameRu\":\"У\n" +
            "причала\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Оля\",\"professionKey\":\"ACTOR\"},{\"filmId\":4416941,\"nameRu\":\"Синичка\n" +
            "5\",\"nameEn\":null,\"rating\":\"6.2\",\"general\":false,\"description\":\"Ульяна\",\"professionKey\":\"ACTOR\"},{\"filmId\":4417853,\"nameRu\":\"Свет\n" +
            "в твоем\n" +
            "окне\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":4417853,\"nameRu\":\"Свет\n" +
            "в твоем\n" +
            "окне\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Алена\",\"professionKey\":\"ACTOR\"},{\"filmId\":4445851,\"nameRu\":\"Паромщица.\n" +
            "Долина мечты\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Надя Титова\n" +
            "\",\"professionKey\":\"ACTOR\"},{\"filmId\":4445840,\"nameRu\":\"Я всё начну\n" +
            "сначала\",\"nameEn\":null,\"rating\":\"5.9\",\"general\":false,\"description\":\"Карина\n" +
            "Лаврова\",\"professionKey\":\"ACTOR\"},{\"filmId\":4462928,\"nameRu\":\"Одно лето и вся\n" +
            "жизнь\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Вера\",\"professionKey\":\"ACTOR\"},{\"filmId\":4536580,\"nameRu\":\"Баба\n" +
            "Яга спасает мир\",\"nameEn\":null,\"rating\":\"7.2\",\"general\":false,\"description\":\"Марья\n" +
            "\",\"professionKey\":\"ACTOR\"},{\"filmId\":4642651,\"nameRu\":\"Закрыть\n" +
            "гештальт\",\"nameEn\":null,\"rating\":\"7.7\",\"general\":false,\"description\":\"Тобольцева\",\"professionKey\":\"ACTOR\"},{\"filmId\":4646678,\"nameRu\":\"Всё\n" +
            "как у\n" +
            "людей\",\"nameEn\":null,\"rating\":\"6.5\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":4740409,\"nameRu\":\"Тихие\n" +
            "воды\",\"nameEn\":null,\"rating\":\"5.9\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":5021593,\"nameRu\":\"Иду\n" +
            "за\n" +
            "тобой\",\"nameEn\":null,\"rating\":\"6.4\",\"general\":false,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":5190571,\"nameRu\":\"Тихие\n" +
            "воды\n" +
            "2\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Лёлечка\",\"professionKey\":\"ACTOR\"},{\"filmId\":5301073,\"nameRu\":\"Музыкальный\n" +
            "патруль. Сказочные\n" +
            "песни\",\"nameEn\":null,\"rating\":\"8.8\",\"general\":true,\"description\":\"\",\"professionKey\":\"ACTOR\"},{\"filmId\":5301073,\"nameRu\":\"Музыкальный\n" +
            "патруль. Сказочные\n" +
            "песни\",\"nameEn\":null,\"rating\":\"8.8\",\"general\":false,\"description\":\"озвучка\",\"professionKey\":\"ACTOR\"},{\"filmId\":5399168,\"nameRu\":\"Обоюдное\n" +
            "согласие 2\",\"nameEn\":null,\"rating\":null,\"general\":false,\"description\":\"Марина Венишева\",\"professionKey\":\"ACTOR\"}]}"
}