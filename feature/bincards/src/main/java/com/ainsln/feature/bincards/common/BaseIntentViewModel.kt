package com.ainsln.feature.bincards.common

import androidx.lifecycle.ViewModel
import com.ainsln.core.ui.utils.IntentSender
import com.ainsln.feature.bincards.utils.IntentUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
open class BaseIntentViewModel @Inject constructor(
    private val intentSender: IntentSender
) : ViewModel() {

    private val _showSnackbarMessage = MutableStateFlow<Boolean?>(null)
    val showSnackbarMessage = _showSnackbarMessage.asStateFlow()

    fun handleEvent(event: IntentUiEvent) {
        val success = when (event) {
            is IntentUiEvent.ShowMap -> intentSender.showMap(event.latitude, event.longitude)
            is IntentUiEvent.Call -> intentSender.call(event.phone)
            is IntentUiEvent.OpenWebPage -> intentSender.openWebPage(event.url)
        }
        if (!success)
            _showSnackbarMessage.value = true
    }

    fun snackbarShown() {
        _showSnackbarMessage.value = null
    }

}
