package com.ainsln.feature.bincards

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.ainsln.core.common.result.AppException
import com.ainsln.core.model.Bank
import com.ainsln.core.model.CardInfo
import com.ainsln.core.model.CardNumber
import com.ainsln.core.model.Country
import com.ainsln.core.ui.state.UiState
import com.ainsln.feature.bincards.lookup.BinInputScreenContent
import org.junit.Rule
import org.junit.Test
import java.util.Date

class BinInputContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun idle_state_shows_placeholder() {
        composeTestRule.setStateContent(UiState.Idle)
        composeTestRule
            .onNodeWithStringId(R.string.enter_card_digits)
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescriptionId(R.string.clear_field)
            .assertIsNotDisplayed()
    }

    @Test
    fun loading_state_shows_progress() {
        composeTestRule.setStateContent(UiState.Loading)
        composeTestRule
            .onNode(hasTestTag("loadingIndicator"))
            .assertIsDisplayed()
    }

    @Test
    fun error_state_shows_error_message() {
        composeTestRule.setStateContent(UiState.Error(AppException.NetworkError(0, "")))
        composeTestRule
            .onNodeWithStringId(com.ainsln.core.ui.R.string.error_network)
            .assertIsDisplayed()
    }

    @Test
    fun success_state_shows_card_info() {
        composeTestRule.setStateContent(UiState.Success(createBaseCardInfo()))
        // Verify basic info is displayed
        composeTestRule.onNodeWithText("visa").assertIsDisplayed()
        composeTestRule.onNodeWithText("classic").assertIsDisplayed()
        composeTestRule.onNodeWithText("debit").assertIsDisplayed()
        // Verify country info
        composeTestRule.onNodeWithTag("countryBlock").assertIsDisplayed()
        // Verify bank info
        composeTestRule.onNodeWithTag("bankBlock").assertIsDisplayed()

    }

    @Test
    fun input_field_shows_clear_button_when_not_empty() {
        composeTestRule.setStateContent(UiState.Idle, "123")
        composeTestRule
            .onNodeWithContentDescriptionId(R.string.clear_field)
            .assertIsDisplayed()
    }

    @Test
    fun success_state_with_minimal_card_info() {
        val minimalCardInfo = createBaseCardInfo().copy(
            country = null,
            bank = null
        )
        composeTestRule.setStateContent(UiState.Success(minimalCardInfo))

        // Verify only basic info is shown
        composeTestRule.onNodeWithText("visa").assertIsDisplayed()
        composeTestRule.onNodeWithText("16").assertIsDisplayed()
        // Verify optional fields are not shown
        composeTestRule.onNodeWithTag("bankBlock").assertDoesNotExist()
        composeTestRule.onNodeWithTag("countryBlock").assertDoesNotExist()
    }

    private fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.setStateContent(
        uiState: UiState<CardInfo>,
        inputText: String = ""
    ){
        setContent {
            BinInputScreenContent(
                uiState = uiState,
                inputText = inputText,
                onInputChange = {},
                onSearchClick = {},
                onEvent = {}
            )
        }
    }

    companion object {
        fun createBaseCardInfo(bin: String = "123456") = CardInfo(
            bin = bin,
            date = Date(),
            number = CardNumber(length = 16, luhn = true),
            scheme = "visa",
            type = "debit",
            brand = "classic",
            prepaid = false,
            country = Country(
                numeric = 840,
                alpha2 = "US",
                name = "United States",
                emoji = "🇺🇸",
                currency = "USD",
                latitude = 38.8951,
                longitude = -77.0364
            ),
            bank = Bank(
                id = 1,
                name = "Test Bank",
                url = "www.test.com",
                phone = "+1234567890",
                city = "Test City"
            )
        )
    }
}
