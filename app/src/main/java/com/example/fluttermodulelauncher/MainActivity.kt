package com.example.fluttermodulelauncher

import android.content.Context
import io.flutter.embedding.android.FlutterActivity;

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fluttermodulelauncher.App.Companion
import com.example.fluttermodulelauncher.App.Companion.channelId
import com.example.fluttermodulelauncher.App.Companion.engineId
import com.example.fluttermodulelauncher.App.Companion.flutterEngine
import com.example.fluttermodulelauncher.App.Companion.methodChannel
import com.example.fluttermodulelauncher.ui.theme.FlutterModuleLauncherTheme
import com.google.gson.Gson
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlutterModuleLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = { startFlutterModule() }) {
                            Text(text = "Start")
                        }
                        Button(onClick = { startFlutterModule("/create-ticket") }) {
                            Text(text = "Create Ticket")
                        }
                        Button(onClick = { startFlutterModule("/manage-ticket") }) {
                            Text(text = "Manage Ticket")
                        }
                        Button(onClick = {
                            if (flutterEngine == null) {
                                initEngine();
                            }
                            methodChannel.invokeMethod(App.getPmsVersion, null, object : MethodChannel.Result {
                                override fun success(result: Any?) {
                                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                                }

                                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                                    TODO("Not yet implemented")
                                }

                                override fun notImplemented() {
                                    TODO("Not yet implemented")
                                }
                            });


                        }) {
                            Text(text = "Get version")
                        }
                    }
                }
            }
        }

    }

    private var flutterEngine: FlutterEngine? = null

    private fun initEngine() {
        flutterEngine = FlutterEngine(this)

        // trỏ vào hàm main có @pragma('vm:entry-point') trên code Flutter
        flutterEngine!!.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint(
                FlutterInjector.instance().flutterLoader().findAppBundlePath(),
                "nativeEntry"
            )
        )

        // Handle các function giao tiếp giữa Flutte với native
        handleMethodChannel(flutterEngine!!)

        // Cache the FlutterEngine
        FlutterEngineCache.getInstance().put(engineId, flutterEngine)
    }

    private fun startFlutterModule(route: String = "/login-test") {
        Toast.makeText(this, "Starting Flutter...", Toast.LENGTH_SHORT).show()


        // Khởi tạo Flutter engine nếu chưa có
//        if (flutterEngine == null) {
            initEngine();
//        }

        // Khỏi chạy Activity với Flutter engine đã cache
        startActivity(
            FlutterActivity.withCachedEngine(engineId)
                .backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque)
                .build(this)
        )

        // navigate tới route đã chọn
        navigator(route)
    }

    private fun navigator(route: String) {
        val jsonMap = mapOf("route" to route)
        val jsonString = Gson().toJson(jsonMap)



        methodChannel.invokeMethod(App.methodNameNavigator, jsonString)
    }

    private fun handleMethodChannel(flutterEngine: FlutterEngine) {
        val psId = "9894824"
//        val token =
//            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImI1MGE0MzY4LTQ4MGUtMTFlZi04Yzk2LTAwNTA1NmEzOWI5OCIsImVtYWlsIjoiZGFpZHBAZnB0LmNvbSIsImZsYWciOiJ3ZWIiLCJpYXQiOjE3NDI4ODY2ODUsImV4cCI6MTc0MzQ5MTQ4NX0.-Cs7VHCuu2OMx2R68fr_NuteE8oLK_gR1bn04qYCIbE"
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYyYWE2NDk1LTFiNmQtNGViNy1hNTc2LTIyOTU0NzZkOWRjYSIsImVtYWlsIjoibmd1eWVubnQ1MEBmcHQuY29tIiwiZmxhZyI6IndlYiIsImlhdCI6MTc0NTU0ODY0OCwiZXhwIjoxNzQ2MTUzNDQ4fQ.bDAur3kHdg_TXcOxuS_pDxsrlRj0g5k9DTs1lsmz5pU"
        val fcmToken = "67ea5f2b-7768-800f-aab1-f088b34716da"

        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelId)
        methodChannel.setMethodCallHandler { call, result ->
            Log.i("flutter", "${call.method}: run native")
            if (call.method.equals("initialize")) {
                val jsonMap: MutableMap<String, Any> = HashMap()
                jsonMap["DeviceToken"] = fcmToken
                jsonMap["Token"] = token
                jsonMap["RefSourceCode"] = "SOP"
                jsonMap["PsId"] = psId
                jsonMap["Environment"] = "staging" // staging or production
                jsonMap["Mode"] = "SPF"
//                    jsonMap["Parameters"] = parameters
                val gson = Gson()
                val jsonString: String = gson.toJson(jsonMap)

                result.success(jsonString)
            } else if (call.method.equals("initRootRoute")) {
                result.success("/create-ticket")
            } else {
                result.notImplemented()
            }
        }
    }
}
