package com.ainsln.feature.bincards.lookup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ainsln.core.model.CardInfo
import com.ainsln.core.ui.components.AppPlaceholder
import com.ainsln.core.ui.components.BinListTopBar
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.BinListTheme
import com.ainsln.feature.bincards.R
import com.ainsln.feature.bincards.common.BinInfoCard
import com.ainsln.feature.bincards.utils.BinVisualTransformation
import com.ainsln.feature.bincards.utils.IntentUiEvent
import com.ainsln.preview.BinCardsData

@Composable
internal fun BinInputScreen(
    goToHistory: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: BinInputViewModel = hiltViewModel(),
){
    val uiState by viewModel.cardInfoResult.collectAsState()
    val input by viewModel.binInput.collectAsState()

    val showSnackbarMessage by viewModel.showSnackbarMessage.collectAsState()
    val snackbarMsg = stringResource(R.string.activity_launch_error)

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showSnackbarMessage) {
        if (showSnackbarMessage != null){
            onShowSnackbar(snackbarMsg)
            viewModel.snackbarShown()
        }
    }

    Scaffold(
        topBar = {
            BinListTopBar(
                title = stringResource(R.string.bin_input_title),
                actions = {
                    IconButton(onClick = goToHistory) {
                        Icon(
                            painter = painterResource(R.drawable.ic_history),
                            contentDescription = stringResource(R.string.open_bin_history)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            BinInputScreenContent(
                uiState = uiState,
                inputText = input,
                onInputChange = viewModel::onInputChange,
                onSearchClick = {
                    keyboardController?.hide()
                    viewModel.onSearchClick()
                },
                onEvent = viewModel::handleEvent
            )
        }
    }
}

@Composable
internal fun BinInputScreenContent(
    uiState: UiState<CardInfo>,
    inputText: String,
    onInputChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onEvent: (IntentUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp
    )
){
    Column(
        verticalArrangement = Arrangement.spacedBy(28.dp),
        modifier = modifier.padding(contentPadding).testTag("BinInputScreenContent")
    ) {
        InputBlock(inputText, onInputChange, onSearchClick)
        InfoBlock(uiState, onEvent)
    }
}

@Composable
private fun InputBlock(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSearchClick: () -> Unit
){
    OutlinedTextField(
        value = inputText,
        onValueChange = onInputChange,
        placeholder = {
            Text(
                text = stringResource(R.string.enter_bin_iin),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth()
            )
        },
        leadingIcon = {
            if (inputText.isNotBlank()) {
                IconButton(onClick = { onInputChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.clear_field)
                    )
                }
            }
        },
        trailingIcon = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(onSearch = { onSearchClick() }),
        singleLine = true,
        visualTransformation = BinVisualTransformation(),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace
        ),
        modifier = Modifier.fillMaxWidth().testTag("inputBinField")
    )
}

@Composable
private fun InfoBlock(
    uiState: UiState<CardInfo>,
    onEvent: (IntentUiEvent) -> Unit
){
    when(uiState){
        is UiState.Idle -> {
            AppPlaceholder(
                text = stringResource(R.string.enter_card_digits),
                icon = painterResource(R.drawable.ic_placeholder)
            )
        }
        is UiState.Error -> {
            ErrorScreen(
                e = uiState.e,
                icon = painterResource(R.drawable.ic_error)
            )
        }
        is UiState.Loading -> { LoadingScreen() }
        is UiState.Success -> {
            BinInfoCard(uiState.data, onEvent)
        }
    }

}

@Preview
@Composable
private fun BinInputScreenContentPreview(){
    BinListTheme {
        Surface(Modifier.fillMaxSize()) {
            BinInputScreenContent(
                uiState = UiState.Success(BinCardsData.infoCard),
                inputText = "",
                onInputChange = {},
                onSearchClick = {},
                onEvent = {}
            )
        }
    }
}
