package com.dowplay.dowplay

import android.content.Context
import android.content.Intent
import android.util.Log
import com.dowplay.dowplay.MovieMedia.Companion.fromJson
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler

/** DowplayPlugin  */
class DowplayPluginK : FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private var channel: MethodChannel? = null
    private var context: Context? = null
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "dowplay")
        channel!!.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "play_movie") {
            Log.d(TAG, "onMethodCall: play_movie")
            Log.d(TAG, "onMethodCall: " + call.arguments.toString())
            val movieMedia = fromJson(call.arguments.toString())
            val intent = Intent(context, CustomPlayerActivity::class.java)
            intent.putExtra("extra", movieMedia as java.io.Serializable);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context!!.startActivity(intent)
            result.success(true)
        } else if (call.method == "config_downloader") {
            result.success(true)
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        channel!!.setMethodCallHandler(null)
    }

    companion object {
        private const val TAG = "DowplayPlugin"
    }
}