package com.example.marvelcomics.data.constants

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

class Constants {
    companion object {
        const val PUBLIC_KEY = "080a502746c8a60aeab043387a56eef0"
        const val PRIVATE_KEY = "d288984e618e4e54177df9f44d52ed76f819157e"
        const val BASE_URL = "https://gateway.marvel.com"
        const val HASH = "6edc18ab1a954d230c1f03c590d469d2"
        const val PAGE_SIZE = 5
        val ts = Timestamp(System.currentTimeMillis()).toString()

        fun hashMD5(): String {
            val input = "${ts}${PRIVATE_KEY}${PUBLIC_KEY}"
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(
                1,
                md.digest(input.toByteArray()))
                .toString(16)
                .padStart(32, '0')
        }
    }
}