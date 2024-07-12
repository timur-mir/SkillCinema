package ru.diplomnaya.skilllcinema.model.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.AllCountriesGenresID
import ru.diplomnaya.skilllcinema.model.entities.AnyCountriesAndGenresFilms
import ru.diplomnaya.skilllcinema.model.entities.AnyCountriesAndGenresFilmsSecondVariant
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.entities.FilmPresentOnNetPlatform
import ru.diplomnaya.skilllcinema.model.entities.Images
import ru.diplomnaya.skilllcinema.model.entities.PremieresList
import ru.diplomnaya.skilllcinema.model.entities.Top250List
import ru.diplomnaya.skilllcinema.model.entities.TopAwaitList
import ru.diplomnaya.skilllcinema.model.entities.SearchModel
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.model.entities.Similar
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred


interface MovieListApi {

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/premieres")
    suspend fun premieres(@Query("year") year: Int, @Query("month") month: String): PremieresList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/top?type=TOP_100_POPULAR_FILMS")
    suspend fun topAwaitList(@Query("page") page: Int): TopAwaitList

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/top?type=TOP_250_BEST_FILMS")
    suspend fun top250List(@Query("page") page: Int): Top250List

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films?type=FILM&ratingTo=9&ratingFrom=10")
    suspend fun anyCountriesAndGenresFilms(
        @Query("countries") countries: Int,
        @Query("genre") genre: Int,
        @Query("page") page: Int
    ): AnyCountriesAndGenresFilms

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films?type=FILM&ratingTo=9&ratingFrom=10")
    suspend fun anyCountriesAndGenresFilmsSecondVariant(
        @Query("countries") countries: Int,
        @Query("genre") genre: Int,
        @Query("page") page: Int
    ): AnyCountriesAndGenresFilmsSecondVariant
    //Available values : FILM, TV_SHOW, TV_SERIES, MINI_SERIES, ALL

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films?type=TV_SERIES&ratingTo=8&ratingFrom=10")
    suspend fun anyCountriesAndGenresTv_Series(
        @Query("countries") countries: Int,
        @Query("genre") genre: Int,
        @Query("page") page: Int
    ): AnyCountriesAndGenresFilms

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films/{id}")
    suspend fun getFilmById(@Path("id") id: Int): FilmDetailInfo


    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v2.2/films/{id}/seasons")
    suspend fun getInfoSerial(@Path("id") id: Int): Serial

    @Headers("X-API-KEY: $api_key")
    @GET("/api/v2.2/films")
    suspend fun searchFilms(
        @Query("type") type: String,
        @Query("countries") countries: Int,
        @Query("genre") genre: Int,
        @Query("page") page: Int,
        @Query("order") order: String,
        @Query("ratingFrom") ratingFrom: Int,
        @Query("ratingTo") ratingTo: Int,
        @Query("yearFrom") yearFrom: Int,
        @Query("yearTo") yearTo: Int,
        @Query("keyword") keyword: String

    ): SearchModel

    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v1/staff")
    suspend fun getStaffStarred(
        @Query("filmId") filmId: Int
    ): List<StaffStarred>

    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v1/staff/{id}")
    suspend fun getInfoAboutStaff(
        @Path("id") id: Int
    ): Actor

    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v2.2/films/filters")
    suspend fun getAllIdCountriesGenres(): AllCountriesGenresID

    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v2.2/films/{id}/images")
    suspend fun getImageAboutFilm(
        @Path("id") filmId: Int,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Images

    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v2.2/films/{id}/similars")
    suspend fun getSimilarFilms(
        @Path("id") filmId: Int
    ): Similar
    @Headers("X-API-KEY: ${Companion.api_key}")
    @GET("/api/v2.2/films/{id}/videos")
    suspend fun getMoreUrlFilmOnNet(
        @Path("id") filmId: Int
    ): FilmPresentOnNetPlatform
    private companion object {
       /// private const val api_key="5555_fake"
       ///private const val api_key = "9a07af8a-7786-4d00-8698-56e45464260f"
////private const val api_key =  "621732c6-c821-469e-8805-ddda7860874d"
////private const val api_key = "db714756-fc99-4f97-b83a-33c87611ebc0"
  ////private const val api_key = "55ecd8a9-9cca-4d49-bbdb-e1a7658a012d"
private const val api_key ="ce993e3d-935a-4c1c-ba3e-dacd2537cb3c"
    }
}

val retrofit = Retrofit
    .Builder()
    .client(
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }).build()
    )
    .baseUrl("https://kinopoiskapiunofficial.tech")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(MovieListApi::class.java)

