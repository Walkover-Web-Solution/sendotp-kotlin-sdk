package com.msg91.sendotp

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtils {
    private const val MAX_REDIRECTS = 10

    @Throws(IOException::class)
    fun post(urlString: String, jsonBody: String, redirectCount: Int = 0): String {
        if (redirectCount > MAX_REDIRECTS) {
            throw IOException("Too many redirects")
        }

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.doOutput = true
        connection.doInput = true

        val writer = BufferedWriter(OutputStreamWriter(connection.outputStream, "UTF-8"))
        writer.write(jsonBody)
        writer.flush()
        writer.close()

        val responseCode = connection.responseCode
        val redirectUrl = connection.getHeaderField("Location")

        return if (responseCode in 300..399 && redirectUrl != null) {
            get(redirectUrl, redirectCount + 1)
        } else {
            val inputStream = if (responseCode >= 200 && responseCode < 400) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            inputStream.bufferedReader().use { it.readText() }
        }
    }

    @Throws(IOException::class)
    fun get(urlString: String, redirectCount: Int = 0): String {
        if (redirectCount > MAX_REDIRECTS) {
            throw IOException("Too many redirects")
        }

        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.doInput = true

        val responseCode = connection.responseCode
        val redirectUrl = connection.getHeaderField("Location")

        return if (responseCode in 300..399 && redirectUrl != null) {
            // Redirect with GET
            get(redirectUrl, redirectCount + 1)
        } else {
            val inputStream = if (responseCode >= 200 && responseCode < 400) {
                connection.inputStream
            } else {
                connection.errorStream
            }
            inputStream.bufferedReader().use { it.readText() }
        }
    }
}