# Send OTP Android Sdk!

**The SendOtp SDK makes verifying OTP easy. SDK supports the verification of email and phone numbers via SMS, Calls & Whatsapp.**

**This SDK supports invisible OTP verification also.**


## Getting started

Login or create account at MSG91 to use sendOTP services.

**Get your widgetId and authToken:**

After login at MSG91, follow below steps to get your widgetId and authToken:
1. Select OTP option available on dashboard.
2. Create and configure your widget.
3. If you are first time user then generate new token and keep it enable.
4. The widgetId and authToken generated from the above steps will be required for initializing the widget.

**Note:** To ensure that this SDK functions correctly within your mobile application, please enable Mobile Integration while configuring the widget.

## Installation

Add the dependency in ***build.gradle.kts***
```shell
	dependencies {
            implementation("com.msg91.lib:sendotp:1.0.0")
	}
```


## Example

For complete example check [MainActivity.kt](https://github.com/Walkover-Web-Solution/sendotp-kotlin-sdk/blob/main/app/src/main/java/com/msg91/sendotp/MainActivity.kt) of example app.




# SDK Methods

We provide methods, which helps you integrate the OTP verification within your own user interface.

`getWidgetProcess` is an optional method, this will receive the widget configuration data.
<br>
<br>

There are three methods `sendOTP`, `retryOTP` and `verifyOTP` for the otp verification process.

You can call these methods as follow:

### `sendOTP`

The sendOTP method is used to send an OTP to an identifier. The identifier can be an email or mobile number (it must contain the country code without +).
<br>
You can call this method on a button press.
<br>
<br>

*NOTE:* If you have enabled the invisible option in a widget configuration and you are trying to verify the mobile number with the mobile network then your number will be verified without OTP and if in any case the invisible verification gets fail in between the process then you will receive the normal OTP on your entered number.

```kt
    private fun handleSendOTP() {
        val widgetId = "3461******************38";
        val tokenAuth = "125*******************TP1";

        val identifier = '91758XXXXXXX'; // or 'example@xyz.com'
        
        coroutineScope.launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    OTPWidget.sendOTP(widgetId, tokenAuth, identifier)
                }
                println("Result: $result")

            } catch (e: Exception) {
                println("Error in SendOTP")
            }
        }
    }
```

### `retryOTP`

The retryOTP method allows retrying the OTP on desired communication channel.
<br>
retryOTP method takes optional channel code for SMS -`11`, VOICE -`4`, 'EMAIL -`3`, WHATSAPP -`12` for retrying otp.

*Note:* If the widget uses the default configuration, don't pass the channel as argument.

```kt
    private fun handleRetryOTP(channel: Number) {
        coroutineScope.launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    OTPWidget.retryOTP(widgetId, tokenAuth, reqId, channel)
                }
                println("Result: $result")

            } catch (e: Exception) {
                println("Error in RetryOTP")
            }
        }
    }
```

### `verifyOTP`

The verifyOTP method is used to verify an OTP entered by the user.

```kt
    private fun handleVerifyOtp() {

        val otp = '****';
        coroutineScope.launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    OTPWidget.verifyOTP(widgetId, tokenAuth, reqId, otp)
                }
                println("Result: $result")

            } catch (e: Exception) {
               println("Error in VerifyOTP")
            }
        }
    }
```

### `getWidgetProcess`

This is an ***optional*** method, this will receive the widget configuration data.

```kt
    private fun getWidgetProcess() {
        coroutineScope.launch {
            try {

                val result = withContext(Dispatchers.IO) {
                    OTPWidget.getWidgetProcess(widgetId, tokenAuth)
                }
                println("Widget Data: $result")

            } catch (e: Exception) {
                println("Error in GetWidgetData")
            }
        }
    }
```



<br>
<br>
<br>

## License

```
Copyright 2022 MSG91
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```@