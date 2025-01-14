package com.ainsln.binlist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ainsln.feature.bincards.navigation.BinLookUpDestination
import com.ainsln.feature.bincards.navigation.binCardsDestination
import com.ainsln.feature.bincards.navigation.binCardsHistory
import com.ainsln.feature.bincards.navigation.navigateToHistory

@Composable
fun AppNavHost(
    onShowSnackbar: suspend (String) -> Unit
){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = BinLookUpDestination
    ){
        binCardsDestination(
            navController::navigateToHistory,
            onShowSnackbar
        )
        binCardsHistory(
            navController::popBackStack,
            onShowSnackbar
        )
    }
}
