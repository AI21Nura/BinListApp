package com.ainsln.feature.bincards.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ainsln.feature.bincards.history.BinHistoryScreen
import com.ainsln.feature.bincards.lookup.BinInputScreen
import kotlinx.serialization.Serializable


@Serializable data object BinLookUpDestination
@Serializable data object BinHistoryDestination

fun NavGraphBuilder.binCardsDestination(
    navigateToHistory: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
){
    composable<BinLookUpDestination>{
        BinInputScreen(navigateToHistory, onShowSnackbar)
    }
}

fun NavGraphBuilder.binCardsHistory(
    onBack: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit
){
    composable<BinHistoryDestination>{
        BinHistoryScreen(onBack, onShowSnackbar)
    }
}

fun NavController.navigateToHistory(){
    navigate(BinHistoryDestination)
}
