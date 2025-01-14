package com.ainsln.feature.bincards.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ainsln.core.model.CardInfo
import com.ainsln.core.ui.components.AppAlertDialog
import com.ainsln.core.ui.components.AppPlaceholder
import com.ainsln.core.ui.components.BinListTopBar
import com.ainsln.core.ui.components.ErrorScreen
import com.ainsln.core.ui.components.LoadingScreen
import com.ainsln.core.ui.state.UiState
import com.ainsln.core.ui.theme.BinListTheme
import com.ainsln.feature.bincards.R
import com.ainsln.feature.bincards.common.BinInfoCard
import com.ainsln.feature.bincards.utils.IntentUiEvent

@Composable
internal fun BinHistoryScreen(
    onBack: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
    viewModel: BinHistoryViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    val showSnackbarMessage by viewModel.showSnackbarMessage.collectAsState()
    var showAlertDialog by remember { mutableStateOf(false) }
    var showClearAction by remember { mutableStateOf(false) }
    val snackbarMsg = stringResource(R.string.activity_launch_error)

    LaunchedEffect(showSnackbarMessage) {
        if (showSnackbarMessage != null){
            onShowSnackbar(snackbarMsg)
            viewModel.snackbarShown()
        }
    }

    Scaffold(
        topBar = {
            BinListTopBar(
                title = stringResource(R.string.history_title),
                actions = {
                    if (showClearAction)
                        TextButton(onClick = { showAlertDialog = true }) {
                            Text(stringResource(R.string.clear_all))
                        }
                },
                canNavigateBack = true,
                onBack = onBack
            )
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)){
            BinHistoryContent(
                uiState = uiState,
                onEvent = viewModel::handleEvent,
                showClearAction = { showClearAction = true }
            )
        }
    }

    if (showAlertDialog){
        AppAlertDialog(
            title = stringResource(R.string.confirm_deletion),
            text = stringResource(R.string.clear_history_dialog),
            onDismissClick = { showAlertDialog = false },
            onConfirmClick = {
                showAlertDialog = false
                showClearAction = false
                viewModel.clear()
            }
        )
    }
}

@Composable
internal fun BinHistoryContent(
    uiState: UiState<List<CardInfo>>,
    onEvent: (IntentUiEvent) -> Unit,
    showClearAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = 16.dp,
        vertical = 8.dp
    )
){
    Box(modifier = modifier.padding(contentPadding)){
        when(uiState){
            is UiState.Success -> {
                BinHistoryList(
                    items = uiState.data,
                    onEvent = onEvent,
                    showClearAction = showClearAction
                )
            }
            is UiState.Error -> {
                ErrorScreen(
                    e = uiState.e,
                    icon = painterResource(R.drawable.ic_error)
                )
            }
            else -> { LoadingScreen() }
        }
    }
}

@Composable
private fun BinHistoryList(
    items: List<CardInfo>,
    onEvent: (IntentUiEvent) -> Unit,
    showClearAction: () -> Unit
){
    if (items.isEmpty()){
        AppPlaceholder(
            text = stringResource(R.string.empty_history),
            icon = painterResource(R.drawable.ic_empty_history)
        )
    } else {
        LazyColumn {
            items(items){ card ->
                BinInfoCard(
                    info = card,
                    onEvent = onEvent,
                    isExpandable = true,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
        showClearAction()
    }
}

@Preview
@Composable
private fun BinHistoryContentPreview(){
    BinListTheme {
        Surface(Modifier.fillMaxSize()) {
            BinHistoryContent(
                uiState = UiState.Success(emptyList()),
                onEvent = {},
                showClearAction = {}
            )
        }
    }
}
