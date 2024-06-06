package ru.diplomnaya.skilllcinema


import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.crashlytics.FirebaseCrashlytics
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment


class App : Application() {
    val Context.datastore: DataStore<Preferences> by preferencesDataStore(
        name = "settings_search_store"
    )
    companion object{
        var appContext: Context? =null
    }

    override fun onCreate() {
        super.onCreate()
        appContext=applicationContext
        applicationDatastore=datastore
        DataBase.initDatabase(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
    object DatastoreLauncher{
       lateinit var  applicationDatastore:DataStore<Preferences>
    }
}
