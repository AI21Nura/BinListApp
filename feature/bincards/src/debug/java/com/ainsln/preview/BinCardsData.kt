package com.ainsln.preview

import com.ainsln.core.model.Bank
import com.ainsln.core.model.CardInfo
import com.ainsln.core.model.CardNumber
import com.ainsln.core.model.Country
import java.util.Date

data object BinCardsData {
    val infoCard = CardInfo(
        bin = "377750",
        date = Date(System.currentTimeMillis()),
        number = CardNumber(16, true),
        scheme = "Visa",
        type = "Debit",
        brand = "Debit Mastercard (Enhanced)",
        prepaid = false,
        country = Country(
            numeric = 208,
            alpha2 = "DK",
            name = "United States of America (USA)",
            emoji = "DK",
            currency = "DKK",
            latitude = 13.361563,
            longitude = 24.671190
        ),
        bank = Bank(
            id = 0,
            name = "Jyske Bank",
            city = "Hjørring",
            url = "www.jyskebank.dk",
            phone = "+4589893300"
        )
    )

}
