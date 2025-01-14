package com.ainsln.feature.bincards.history

import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.BinInfoRepository
import com.ainsln.core.model.CardInfo
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.state.toState
import com.ainsln.core.ui.utils.IntentSender
import com.ainsln.feature.bincards.common.BaseIntentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinHistoryViewModel @Inject constructor(
    private val repository: BinInfoRepository,
    intentSender: IntentSender
) : BaseIntentViewModel(intentSender) {

    val uiState: StateFlow<UiState<List<CardInfo>>> = repository.getAll()
        .map { result -> result.toState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            UiState.Loading
        )

    fun clear(){
        viewModelScope.launch { repository.clearAll() }
    }
}
