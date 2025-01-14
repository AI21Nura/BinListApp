package com.ainsln.feature.bincards

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.ainsln.core.common.result.AppException
import com.ainsln.core.model.CardInfo
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.bincards.BinInputContentTest.Companion.createBaseCardInfo
import com.ainsln.feature.bincards.history.BinHistoryContent
import org.junit.Rule
import org.junit.Test

class BinHistoryContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showLoadingState() {
        composeTestRule.setStateContent(UiState.Loading)
        composeTestRule.onNodeWithTag("loadingIndicator").assertExists()
    }

    @Test
    fun showEmptyList() {
        composeTestRule.setStateContent(UiState.Success(emptyList()))
        composeTestRule.onNodeWithStringId(R.string.empty_history).assertExists()
    }

    @Test
    fun showListWithItems() {
        val testItems = listOf(
            createBaseCardInfo("123456"),
            createBaseCardInfo("789012")
        )

        composeTestRule.setStateContent(UiState.Success(testItems))
        composeTestRule.onNodeWithText("BIN/IIN: 123456").assertExists()
        composeTestRule.onNodeWithText("BIN/IIN: 789012").assertExists()
    }

    @Test
    fun showErrorState() {
        val error = AppException.DatabaseError("")
        composeTestRule.setStateContent(UiState.Error(error))
        composeTestRule.onNodeWithStringId(com.ainsln.core.ui.R.string.error_database).assertExists()
    }


    private fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.setStateContent(
        uiState: UiState<List<CardInfo>>
    ){
        setContent {
            BinHistoryContent(
                uiState = uiState,
                onEvent = {},
                showClearAction = {}
            )
        }
    }

}
