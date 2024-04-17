package com.hypeapps.instasplit.core.utils

fun Double.formatMoney(): String {
    val value = if (this == -0.0) 0.0 else this
    return "$" + String.format("%.2f", value)
}