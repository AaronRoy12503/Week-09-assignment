package edu.farmingdale.datastoredemo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import edu.farmingdale.datastoredemo.data.local.UserPreferencesRepository


private const val LAYOUT_PREFERENCE_NAME = "layout_preferences"
// Single instance of DataStore created for the application
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = LAYOUT_PREFERENCE_NAME
)

class EmojiReleaseApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        // Initialize repository with DataStore instance
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}