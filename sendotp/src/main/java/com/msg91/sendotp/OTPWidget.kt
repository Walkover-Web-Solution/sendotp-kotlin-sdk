package com.msg91.sendotp

import org.json.JSONObject

object OTPWidget {

    @JvmStatic
    fun sendOTP(widgetId: String, tokenAuth: String, identifier: String): String {

        val payload = JSONObject().apply {
            put("widgetId", widgetId)
            put("tokenAuth", tokenAuth)
            put("identifier", identifier)
        }

        return NetworkUtils.post(ApiUrls.sendOTP, payload.toString())
    }

    @JvmStatic
    fun verifyOTP(widgetId: String, tokenAuth: String, reqId: String, otp: String): String {

        val payload = JSONObject().apply {
            put("widgetId", widgetId)
            put("tokenAuth", tokenAuth)
            put("otp", otp)
            put("reqId", reqId)
        }

        return NetworkUtils.post(ApiUrls.verifyOTP, payload.toString())
    }

    @JvmStatic
    fun retryOTP(widgetId: String, tokenAuth: String, reqId: String, retryChannel: Number? = null): String {

        val payload = JSONObject().apply {
            put("widgetId", widgetId)
            put("tokenAuth", tokenAuth)
            put("reqId", reqId)
            retryChannel?.let {
                put("retryChannel", it)
            }
        }

        return NetworkUtils.post(ApiUrls.retryOTP, payload.toString())
    }

    @JvmStatic
    fun getWidgetProcess(widgetId: String, tokenAuth: String): String {

        val url = ApiUrls.getWidgetProcess
            .replace(":widgetId", widgetId)
            .replace(":tokenAuth", tokenAuth)

        return NetworkUtils.get(url)
    }
}