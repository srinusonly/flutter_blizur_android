# Flutter Blizur Android Integration

A new Flutter project with Blizur Android SDK integration

## Getting Started

To integrate blizur android sdk follow below steps in your flutter project:

1. Navigate to the `android` folder inside the Flutter project and open the `android/app/build.gradle` file.

2. Add the following lines at the end of the dependencies block in the build.gradle file.
  ```
    implementation 'com.github.srinusonly:blizur-android-sdk:1.0.7'
  ```
3. Navigate to the `android` folder inside the Flutter project and open the `android/build.gradle` file.

4. Add the following lines at the end of the dependencies block in the build.gradle file.
  ```
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
  ```
  maven{ url 'https://jitpack.io' } should present under repositories in buildscript and allprojects.

5. Open the MainActivity.kt file inside the android/app/src/main/kotlin/<your_package_name>/ folder and add the following import statements at the top of the file:
```
import android.os.Bundle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.blizur.androidsdk.BlizurAPI
import io.flutter.embedding.android.FlutterActivity
```
Make sure to remove any duplicate import files.

6. Then, create a constant variable to hold the channel name:
```
  private const val BLIZUR_CHANNEL = "com.blizur.android_sdk_channel"
```

7. Next, override the `configureFlutterEngine()` method to initialize the FlutterEngine and set up the MethodChannel for communication:

```
  class MainActivity: FlutterActivity() {
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
          super.configureFlutterEngine(flutterEngine)

          // Set up the MethodChannel
          MethodChannel(flutterEngine.dartExecutor.binaryMessenger, BLIZUR_CHANNEL).setMethodCallHandler { call, result ->
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
```

8. Finally, call this method from your Flutter Dart code (main.dart) using the MethodChannel.invokeMethod() method:
a. import required packages
  ```
  import 'package:flutter/material.dart';
  import 'package:flutter/services.dart';

  ```
b. initialize the blizurChannel with same channel name which is initialized in MainActivity.kt file.
  ```
    final MethodChannel blizurChannel = MethodChannel('com.blizur.android_sdk_channel');
  ```
c. define below method
  ```
    void initBlizurSDK(String apiKey, String apiSecret) async {
      try {
        await blizurChannel.invokeMethod('initBlizurSDK', {
          'apiKey': apiKey,
          'apiSecret': apiSecret,
        });
      } on PlatformException catch (e) {
        print("Failed to initialize Blizur SDK: '${e.message}'.");
      }
    }
  ```
d. call initBlizurSDK method in main method like below, make sure that `WidgetsFlutterBinding.ensureInitialized()` is called before calling initBlizurSDK.

  ```
    WidgetsFlutterBinding.ensureInitialized();
    initBlizurSDK(<apiKey>, <secret>);
  ```

  finally main.dart file should be like the below:

  ```
    import 'package:flutter/material.dart';
    import 'package:flutter/services.dart';

    final MethodChannel blizurChannel = MethodChannel('com.blizur.android_sdk_channel');

    void initBlizurSDK(String apiKey, String apiSecret) async {
      try {
        await blizurChannel.invokeMethod('initBlizurSDK', {
          'apiKey': apiKey,
          'apiSecret': apiSecret,
        });
      } on PlatformException catch (e) {
        print("Failed to initialize Blizur SDK: '${e.message}'.");
      }
    }
    void main() {
      WidgetsFlutterBinding.ensureInitialized();
      initBlizurSDK(<apiKey>, <secret>);
      runApp(const MyApp());
    }
  ```

That's all, if you find any issues please reach out to tech@blizur.com