package com.mkdev.core_framework.extention

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.asMD5(): String? {
    return try {
        val md = MessageDigest.getInstance("MD5")

        val hexString = StringBuilder()
        for (digestByte in md.digest(toByteArray()))
            hexString.append(String.format("%02X", digestByte))

        hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
        null
    }

}

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        Html.fromHtml(
            this,
            Html.FROM_HTML_MODE_COMPACT
        )
    } else {
        Html.fromHtml(this)
    }
}