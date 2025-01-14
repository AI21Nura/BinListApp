package com.ainsln.feature.bincards.lookup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ainsln.core.data.repository.BinInfoRepository
import com.ainsln.core.model.CardInfo
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.state.toState
import com.ainsln.core.ui.utils.IntentSender
import com.ainsln.feature.bincards.common.BaseIntentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BinInputViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: BinInfoRepository,
    intentSender: IntentSender
) : BaseIntentViewModel(intentSender) {

    val binInput = savedStateHandle.getStateFlow(BIN_INPUT, "")
    val cardInfoResult: MutableStateFlow<UiState<CardInfo>> = MutableStateFlow(UiState.Idle)

    fun onInputChange(input: String){
        savedStateHandle[BIN_INPUT] = input.filter { it.isDigit() }.take(8)
    }

    fun onSearchClick(){
        if (!validateInput(binInput.value)) return
        viewModelScope.launch {
            repository.getByBin(binInput.value).collectLatest { result ->
                cardInfoResult.update { result.toState() }
            }
        }
    }

    private fun validateInput(input: String): Boolean{
        return !(input.length != 6 && input.length != 8)
    }

    companion object {
        private const val BIN_INPUT = "binInput"
    }
}
