package com.example.fluttermodulelauncher

import android.app.Activity
import androidx.activity.ComponentActivity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class App : Application() {

    companion object {
        lateinit var methodChannel: MethodChannel
        var engineId: String = "launcher_engine_id"
        var channelId: String = "com.fpt.isc.pms.sdk_pms.methodchannel"
        var methodNameNavigator: String = "navigator"
        var getPmsVersion: String = "getPmsVersion"
    }

    override fun onCreate() {
        super.onCreate()

        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
    }

}