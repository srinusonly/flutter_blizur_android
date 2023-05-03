package com.example.flutter_application_1

import android.os.Bundle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.blizur.androidsdk.BlizurAPI
import io.flutter.embedding.android.FlutterActivity

import android.app.Activity


private const val CHANNEL = "com.blizur.android_sdk_channel"

class MainActivity: FlutterActivity() {
  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        // Set up the MethodChannel
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "initBlizurSDK" -> {
                    val apiKey = call.argument<String>("apiKey")
                    val apiSecret = call.argument<String>("apiSecret")

                    val activity = this@MainActivity
                    if (activity == null) {
                        result.error("NO_ACTIVITY", "Could not get instance of Activity.", null)
                    } else {
                        // Call the BlizurAPI.init() method with the activity as the first parameter
                        BlizurAPI.init(activity, apiKey, apiSecret)
                        result.success(null)
                    }

                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
}
