package com.dowplay.dowplay

import android.os.Bundle
import androidx.media3.exoplayer.ExoPlayer
import io.flutter.embedding.android.FlutterActivity

class CustomPlayerActivity : FlutterActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_player)
        var exoPlayer:ExoPlayer = ExoPlayer.Builder(applicationContext).build()
    }
}