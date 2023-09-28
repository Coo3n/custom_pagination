package com.test.moviespaging.data.mapper

import com.test.moviespaging.data.local.entity.CryptoEntity
import com.test.moviespaging.data.remote.dto.CryptoJson
import com.test.moviespaging.domain.model.Crypto

fun CryptoJson.toCrypto(): Crypto {
    return Crypto(
        id = id,
        name = name,
        symbol = symbol,
        cmcRank = cmcRank,
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
        price = quote.usd.price,
        percentChange = quote.usd.percentChange1h
    )
}

fun Crypto.toCryptoEntity(): CryptoEntity {
    return CryptoEntity(
        id = id,
        name = name,
        symbol = symbol,
        cmcRank = cmcRank,
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
        price = price,
        percentChange = percentChange
    )
}


fun CryptoEntity.toCrypto(): Crypto {
    return Crypto(
        id = id,
        name = name,
        symbol = symbol,
        cmcRank = cmcRank,
        dateAdded = dateAdded,
        lastUpdated = lastUpdated,
        price = price,
        percentChange = percentChange
    )
}