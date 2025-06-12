# PMS_launcher
+ flutter clean > copy thư mục ghi đè > sync gradle > pub get > run
+ sdk được chạy trên flutter 3.29.3
+ `qr_code_scanner`: C:\Users\Admin\AppData\Local\Pub\Cache\hosted\pub.dev\qr_code_scanner-1.0.1\android\build.gradle
+ ```l
    namespace = "net.touchcapture.qr.flutterqr"
  ....
  ```

+ change java version in:
```
 compileOptions {
        // Flag to enable support for the new language APIs
        coreLibraryDesugaringEnabled true
        // Sets Java compatibility to Java 8
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }
```