package com.msg91.sendotp

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private lateinit var identifierEditText: EditText
    private lateinit var otpEditText: EditText

    private lateinit var sendOTPButton: MaterialButton
    private lateinit var verifyOTPButton: MaterialButton
    private lateinit var retrySMSButton: MaterialButton
    private lateinit var retryVoiceButton: MaterialButton
    private lateinit var retryEmailButton: MaterialButton
    private lateinit var retryWhatsappButton: MaterialButton


    private lateinit var getWidgetProcessButton: MaterialButton
    private lateinit var resultTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar

    private lateinit var authTokenEditText: EditText
    private lateinit var widgetIdEditText: TextInputEditText

    var reqId = ""

    val defaultWidgetId = "33696b6b3344363232333039"
    val defaultAuthToken = "362701TlKfG31gfG65705d0cP1"

    val widgetId = "326b6b6c7330313734343537"
    val tokenAuth = "278060TX9AIhxEpR7H6427de5cP1"

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        identifierEditText = findViewById(R.id.identifierEditText)
        otpEditText = findViewById(R.id.otpEditText)
        sendOTPButton = findViewById(R.id.sendOTPButton)
        verifyOTPButton = findViewById(R.id.verifyOTPButton)

        retrySMSButton = findViewById(R.id.retrySMSButton)
        retryVoiceButton = findViewById(R.id.retryVoiceButton)
        retryEmailButton = findViewById(R.id.retryEmailButton)
        retryWhatsappButton = findViewById(R.id.retryWhatsappButton)

        getWidgetProcessButton = findViewById(R.id.getWidgetProcessButton)
        resultTextView = findViewById(R.id.resultTextView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        authTokenEditText = findViewById(R.id.authTokenEditText)
        widgetIdEditText = findViewById(R.id.widgetIdEditText)

        authTokenEditText.setText(defaultAuthToken)
        widgetIdEditText.setText(defaultWidgetId)

        sendOTPButton.setOnClickListener { sendOTP() }
        verifyOTPButton.setOnClickListener { verifyOTP() }

        retrySMSButton.setOnClickListener { retryOnSms() }
        retryVoiceButton.setOnClickListener { retryOnVoice() }
        retryEmailButton.setOnClickListener { retryOnEmail() }
        retryWhatsappButton.setOnClickListener { retryOnWhatsapp() }

        getWidgetProcessButton.setOnClickListener { getWidgetProcess() }

        setupUI(findViewById(R.id.rootLayout))  // Add this line to setup touch listener
    }

    private fun getAuthToken(): String {
        val authToken = authTokenEditText.text.toString()
        return if (authToken.isNotEmpty()) authToken else defaultAuthToken
    }

    private fun getOTPWidgetId(): String {
        val widgetId = widgetIdEditText.text.toString()
        return if (widgetId.isNotEmpty()) widgetId else defaultWidgetId
    }

    private fun sendOTP() {
        showLoading()
        val identifier = identifierEditText.text.toString()
        val tokenAuth = getAuthToken()
        val widgetId = getOTPWidgetId()
        coroutineScope.launch {
            try {
                val body = JSONObject().apply {
                    put("identifier", identifier)
                    put("widgetId", widgetId)
                    put("tokenAuth", tokenAuth)
                }
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.sendOTP(widgetId, tokenAuth, identifier)
                }
                val jsonObject = JSONObject(result)
                val message = jsonObject.getString("message")
                val invisibleVerified = jsonObject.optString("invisibleVerified")?.let { it == "true" } ?: false

                println("invisibleVerified : $invisibleVerified")
                if (!invisibleVerified) {
                    reqId = message
                } else {
                    reqId = ""
                }

                println("SendOTP Success Message: $message")
                resultTextView.text = result

            } catch (e: Exception) {
                println(e)
                resultTextView.text = "Error: ${e.message}"
            } finally {
                hideLoading()
            }
        }
    }

    private fun verifyOTP() {
        showLoading()
        val otp = otpEditText.text.toString()
        val tokenAuth = getAuthToken()
        val widgetId = getOTPWidgetId()
        coroutineScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.verifyOTP(widgetId, tokenAuth, reqId, otp)
                }
                val jsonObject = JSONObject(result)
                val responseType = jsonObject.getString("type")
                val message = jsonObject.getString("message")

                if (responseType != "error") {
                    reqId = message
                }

                println("VerifyOTP Success Message: $message")
                resultTextView.text = result
            } catch (e: Exception) {
                resultTextView.text = "Error: ${e.message}"
            } finally {
                hideLoading()
            }
        }
    }

    private fun retryOTP(channel: Number) {
        showLoading()
        val tokenAuth = getAuthToken()
        val widgetId = getOTPWidgetId()
        coroutineScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.retryOTP(widgetId, tokenAuth, reqId, channel)
                }
                resultTextView.text = result
            } catch (e: Exception) {
                resultTextView.text = "Error: ${e.message}"
            } finally {
                hideLoading()
            }
        }
    }

    private fun getWidgetProcess() {
        showLoading()
        val tokenAuth = getAuthToken()
        val widgetId = getOTPWidgetId()
        coroutineScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    OTPWidget.getWidgetProcess(widgetId, tokenAuth)
                }
                resultTextView.text = result
            } catch (e: Exception) {
                resultTextView.text = "Error: ${e.message}"
            } finally {
                hideLoading()
            }
        }
    }

    private fun retryOnSms() {
        retryOTP(11)
    }
    private fun retryOnVoice() {
        retryOTP(4)
    }
    private fun retryOnEmail() {
        retryOTP(3)
    }
    private fun retryOnWhatsapp() {
        retryOTP(12)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun showLoading() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingProgressBar.visibility = View.GONE
    }

    // Add this function to set up touch listener
    private fun setupUI(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, _ ->
                hideKeyboard(v)
                view.clearFocus()
                false
            }
        }

        // If a layout container, iterate over children and set up touch listener for non-text box views.
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    // Function to hide the keyboard
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}