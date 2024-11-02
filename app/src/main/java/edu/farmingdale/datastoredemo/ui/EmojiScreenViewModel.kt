package edu.farmingdale.datastoredemo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import edu.farmingdale.datastoredemo.R
import edu.farmingdale.datastoredemo.EmojiReleaseApplication
import edu.farmingdale.datastoredemo.data.local.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine


class EmojiScreenViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    // Combine flows to create a single state object containing all UI-related states
    // This includes the theme preference (isDarkTheme)
    val uiState: StateFlow<EmojiReleaseUiState> =
        combine(
            userPreferencesRepository.isLinearLayout,
            userPreferencesRepository.isDarkTheme // Flow of theme preference
        ) { isLinearLayout, isDarkTheme ->
            EmojiReleaseUiState(
                isLinearLayout = isLinearLayout,
                isDarkTheme = isDarkTheme // Theme state passed to UI
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = EmojiReleaseUiState()
        )

    fun selectLayout(isLinearLayout: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveLayoutPreference(isLinearLayout)
        }
    }

    // Function to handle theme toggle
    // Launches a coroutine to save the new theme preference
    fun toggleTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemePreference(isDarkTheme)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as EmojiReleaseApplication)
                EmojiScreenViewModel(application.userPreferencesRepository)
            }
        }
    }
}

data class EmojiReleaseUiState(
    val isLinearLayout: Boolean = true,
    val isDarkTheme: Boolean = false,
    val toggleContentDescription: Int =
        if (isLinearLayout) R.string.grid_layout_toggle else R.string.linear_layout_toggle,
    val toggleIcon: Int =
        if (isLinearLayout) R.drawable.ic_grid_layout else R.drawable.ic_linear_layout
)