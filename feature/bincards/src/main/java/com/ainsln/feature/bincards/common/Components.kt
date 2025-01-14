package com.ainsln.feature.bincards.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.ainsln.core.model.Bank
import com.ainsln.core.model.CardInfo
import com.ainsln.core.model.Country
import com.ainsln.core.ui.components.ClickableFeatureText
import com.ainsln.core.ui.components.Feature
import com.ainsln.core.ui.components.FeatureLabelLevel
import com.ainsln.core.ui.theme.BinListTheme
import com.ainsln.feature.bincards.R
import com.ainsln.feature.bincards.utils.IntentUiEvent
import com.ainsln.preview.BinCardsData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
internal fun BinInfoCard(
    info: CardInfo,
    onEvent: (IntentUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    isExpandable: Boolean = false
) {
    OutlinedCard(
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (isExpandable) {
            var isExpanded by rememberSaveable { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.bin_iin, info.bin),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                    Text(
                        text = formatDate(info.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    ExpandButton(
                        isExpanded = isExpanded,
                        onClick = { isExpanded = !isExpanded }
                    )
                }
                if (isExpanded) {
                    BinInfoCardContent(info, onEvent)
                }
            }
        } else {
            BinInfoCardContent(info, onEvent)
        }
    }
}

@Composable
private fun ExpandButton(
    isExpanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = null
        )
    }
}

@Composable
private fun BinInfoCardContent(
    info: CardInfo,
    onEvent: (IntentUiEvent) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        BaseInfo(info)
        HorizontalDivider()
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            info.country?.let {
                Box(Modifier.weight(1f)){
                    CountryBlock(it, onEvent)
                }
            }
            info.bank?.let {
                Box(Modifier.weight(1f)){
                    BankBlock(it, onEvent)
                }
            }
        }
    }
}

@Composable
private fun BaseInfo(
    info: CardInfo
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Feature(stringResource(R.string.scheme_label), info.scheme)
            Feature(stringResource(R.string.brand_label), info.brand)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Feature(stringResource(R.string.type_label), info.type)
                Feature(stringResource(R.string.prepaid_label), info.prepaid.toYesNo())
            }

            Feature(stringResource(R.string.card_number_label)) {
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                    Feature(
                        label = stringResource(R.string.length_label),
                        value = info.number.length?.toString(),
                        level = FeatureLabelLevel.LOWER
                    )
                    Feature(
                        label = stringResource(R.string.luhn_label),
                        value = info.number.luhn.toYesNo(),
                        level = FeatureLabelLevel.LOWER
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryBlock(
    country: Country,
    onEvent: (IntentUiEvent) -> Unit
){
    val lan = country.latitude
    val lon = country.longitude
    Feature(stringResource(R.string.country_label)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("countryBlock")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${country.numeric} " +
                            "${country.alpha2 ?: ""} " +
                            (country.name ?: ""),
                    lineHeight = 1.2.em
                )
            }
            Feature(
                label = stringResource(R.string.currency_label),
                value = country.currency,
                level = FeatureLabelLevel.LOWER
            )
            if(lan != null && lon != null) {
                ClickableFeatureText(
                    label = stringResource(R.string.lan_lon_label),
                    clickableText = stringResource(R.string.lan_lon, lan, lon),
                    onTextClick = { onEvent(IntentUiEvent.ShowMap(lan, lon)) },
                    linkTag = "lanLonTag"
                )
            }
        }
    }
}

@Composable
private fun BankBlock(
    bank: Bank,
    onEvent: (IntentUiEvent) -> Unit
){
    Feature(stringResource(R.string.bank_label)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.testTag("bankBlock")
        ) {
            Text(
                text = "${bank.name} (${bank.city ?: "N/A"})",
                lineHeight = 1.2.em
            )

            bank.url?.let { url ->
                ClickableFeatureText(
                    label = stringResource(R.string.url_label),
                    clickableText = url,
                    onTextClick = { onEvent(IntentUiEvent.OpenWebPage(url)) },
                    linkTag = "urlTag"
                )
            }

            bank.phone?.let { phone ->
                ClickableFeatureText(
                    label = stringResource(R.string.phone_label),
                    clickableText = phone,
                    onTextClick = { onEvent(IntentUiEvent.Call(phone)) },
                    linkTag = "phoneTag"
                )
            }
        }
    }
}

@Composable
private fun Boolean?.toYesNo(): String? =
    this?.let {
        if (this) stringResource(R.string.yes)
        else stringResource(R.string.no)
    }

private fun formatDate(date: Date): String{
    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return formatter.format(date)
}

@Preview
@Composable
private fun BinInfoCardPreview(){
    BinListTheme {
        Surface {
            BinInfoCard(
                info = BinCardsData.infoCard,
                onEvent = {},
                isExpandable = true,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
