package com.peerbitskuldeep.currencyconvtrial.apiresponse

data class ResponseData(
    val base: String,
    val date: String,
    val rates: Rates,
    val success: Boolean,
    val timestamp: Int
)