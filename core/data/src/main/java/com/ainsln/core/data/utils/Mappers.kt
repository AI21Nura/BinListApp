package com.ainsln.core.data.utils

import com.ainsln.core.database.model.FullCardInfo
import com.ainsln.core.database.model.entity.BankEntity
import com.ainsln.core.database.model.entity.CardInfoEntity
import com.ainsln.core.database.model.entity.CountryEntity
import com.ainsln.core.model.Bank
import com.ainsln.core.model.CardInfo
import com.ainsln.core.model.CardNumber
import com.ainsln.core.model.Country
import com.ainsln.network.model.BankDTO
import com.ainsln.network.model.CardInfoDTO
import com.ainsln.network.model.CountryDTO
import java.util.Date

fun BankDTO.toBankEntity(): BankEntity? {
    val bankName = name
    return if (bankName != null){
        BankEntity(
            id = 0,
            name = bankName,
            url = url,
            phone = phone,
            city = city
        )
    } else null
}

fun BankDTO.toBankModel(): Bank? {
    val bankName = name
    return if (bankName != null){
        Bank(
            id = 0,
            name = bankName,
            url = url,
            phone = phone,
            city = city
        )
    } else null
}

fun CountryDTO.toCountryEntity(): CountryEntity? {
    val countryNumeric = numeric
    return if (countryNumeric != null) {
        CountryEntity(
            numeric = countryNumeric.toLong(),
            alpha2 = alpha2,
            name = name,
            emoji = emoji,
            currency = currency,
            latitude = latitude,
            longitude = longitude
        )
    } else null
}

fun CountryDTO.toCountryModel(): Country? {
    val countryNumeric = numeric
    return if (countryNumeric != null) {
        Country(
            numeric = countryNumeric.toLong(),
            alpha2 = alpha2,
            name = name,
            emoji = emoji,
            currency = currency,
            latitude = latitude,
            longitude = longitude
        )
    } else null
}

fun CardInfoDTO.toCardInfoEntity(bin: String, bankId: Long?, countryId: Long?) = CardInfoEntity(
    bin = bin,
    date = Date(System.currentTimeMillis()),
    numberLength = number?.length,
    numberLuhn = number?.luhn,
    scheme = scheme,
    type = type,
    brand = brand,
    prepaid = prepaid,
    countryId = countryId,
    bankId = bankId
)

fun CardInfoDTO.toCardInfoModel(
    bin: String,
    dateTimeProvider: DateTimeProvider
) = CardInfo(
    bin = bin,
    date = dateTimeProvider.getCurrentDate(),
    number = CardNumber(number?.length, number?.luhn),
    scheme = scheme,
    type = type,
    brand = brand,
    prepaid = prepaid,
    country = country?.toCountryModel(),
    bank = bank?.toBankModel()
)

fun FullCardInfo.toCardInfoModel() = CardInfo(
    bin = cardInfo.bin,
    date = cardInfo.date,
    number = CardNumber(cardInfo.numberLength, cardInfo.numberLuhn),
    scheme = cardInfo.scheme,
    type = cardInfo.type,
    brand = cardInfo.brand,
    prepaid = cardInfo.prepaid,
    country = country?.toCountryModel(),
    bank = bank?.toBankModel()
)

fun BankEntity.toBankModel() = Bank(id, name, url, phone, city)

fun CountryEntity.toCountryModel() = Country(
    numeric, alpha2, name, emoji, currency, latitude, longitude
)
