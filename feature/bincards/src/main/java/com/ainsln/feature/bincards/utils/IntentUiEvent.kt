package com.ainsln.feature.bincards.utils

sealed interface IntentUiEvent {
    data class ShowMap(val latitude: Double, val longitude: Double) : IntentUiEvent
    data class Call(val phone: String) : IntentUiEvent
    data class OpenWebPage(val url: String) : IntentUiEvent
}
