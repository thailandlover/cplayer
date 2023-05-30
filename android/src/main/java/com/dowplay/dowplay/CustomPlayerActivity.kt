package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PictureInPictureParams
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.lifecycle.Lifecycle
import androidx.media3.common.*
import androidx.media3.common.util.Assertions
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.LoadControl
import androidx.media3.exoplayer.source.TrackGroupArray
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import androidx.media3.exoplayer.upstream.DefaultAllocator
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.DefaultTrackNameProvider
import androidx.media3.ui.PlayerView
import com.dowplay.dowplay.databinding.ActivityCustomPlayerBinding
import com.dowplay.dowplay.databinding.CustomControlViewBinding
import com.dowplay.dowplay.databinding.SettingBinding
import com.google.gson.Gson
import io.flutter.embedding.android.FlutterActivity


@UnstableApi
/**
 * A fullscreen activity to play audio or video streams.
 */

class CustomPlayerActivity() : FlutterActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCustomPlayerBinding.inflate(layoutInflater)
    }

    private val viewCustomControlViewBinding by lazy(LazyThreadSafetyMode.NONE) {
        CustomControlViewBinding.inflate(layoutInflater)
    }
    private var player: ExoPlayer? = null

    /*
     var videoUris = listOf(
         Uri.parse("https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
         Uri.parse("https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080pAr.mp4"),
         Uri.parse("https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
     )*/
    var videoMediaID = arrayOf<String>()
    var videoUris = arrayOf<String>()
    var videoTitle = arrayOf<String>()
    var videoSubTitle = arrayOf<String>()

    val movie: String = "movie"
    val series: String = "series"
    /*listOf(
       "Video 1","Klaus","Video 2"
    )*/
    //var currentVideoIndex = 0

    lateinit var trackSelection: DefaultTrackSelector

    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("current stats screen:", "onCreate")
        setContentView(viewBinding.root)

        initializeBinding()
    }

    private fun initializePlayer() {

        val loadControl: LoadControl = DefaultLoadControl.Builder()
            .setAllocator(DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE))
            .setBufferDurationsMs(
                15000, // Min buffer duration
                30000, // Max buffer duration
                5000,  // Buffer for playback
                5000   // Buffer for rebuffering
            )
            .setTargetBufferBytes(-1)
            .setPrioritizeTimeOverSizeThresholds(true).build()

        trackSelection = DefaultTrackSelector(this).apply {
            setParameters(buildUponParameters().setMaxVideoSizeSd())
            // Enable hardware acceleration
            setParameters(buildUponParameters().setRendererDisabled(C.TRACK_TYPE_VIDEO, false))
        }
        player = ExoPlayer.Builder(this)
            .setSkipSilenceEnabled(true)
            .setVideoScalingMode(2)
            .setAudioAttributes(AudioAttributes.DEFAULT, false)
            .setTrackSelector(trackSelection)
            .setLoadControl(loadControl)
            .build()
            .also { exoPlayer ->
                //val mediaItem = MediaItem.fromUri("https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080pAr.mp4")
                //val mediaItem = MediaItem.fromUri("/data/user/0/com.dowplay.dowplay_example/files/downplay/qvapqtuqtd0fgokeenfelnqli.mp4")
                //val mediaItemTest = MediaItem.fromUri("https://thekee.gcdn.co")
                //////////////////////

                val mediaItem = videoUris.map { MediaItem.fromUri(it) }
                /////////////////////
                exoPlayer.setMediaItems(mediaItem)
                exoPlayer.seekToDefaultPosition(startVideoPosition)
                exoPlayer.prepare()
                exoPlayer.play()
                viewBinding.playerView.player = exoPlayer
                //val bufferSize = 10 * 1024 * 1024 // 10 MB
                player?.playWhenReady = true
            }
        seekToLastWatching()
        playerEvent()
    }

    private fun seekToLastWatching() {
        if (playbackPosition == 0L) {
            if (mediaType == movie) {
                movieMedia?.info?.watching?.currentTime?.toLong()?.let {
                    player?.seekTo(it * 1000)
                    print("B7b creent time :::: " + it * 1000)
                }
            } else if (mediaType == series) {
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.watching?.currentTime?.toLong()
                    ?.let {
                        player?.seekTo(it * 1000)
                        print("B7b creent time :::: " + it * 1000)
                    }
            }
        } else {
            player?.seekTo(playbackPosition)
            print("B7b creent time :::: $playbackPosition")
        }
    }

    var playerHasError = false
    private fun playerEvent() {
        player?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(@Player.State state: Int) {
                /*if(player?.duration == player?.currentPosition){
                    next()
                }*/
                when (state) {
                    Player.STATE_READY -> {
                        // The player is able to immediately play from its current position. The player will be playing if getPlayWhenReady() is true, and paused otherwise.
                        viewBinding.progressLoadingVideo.visibility = View.GONE
                        viewBinding.errorContent.visibility = View.GONE
                        playerHasError = false
                        viewBinding.playPauseButton.setImageResource(R.drawable.pause_icon)
                        isReadyPlayer = true
                        callAddWatchedEpisodesToTheList()
                    }
                    Player.STATE_BUFFERING -> {
                        // The player is not able to immediately play the media, but is doing work toward being able to do so. This state typically occurs when the player needs to buffer more data before playback can start.
                        if (playerHasError) {
                            viewBinding.progressLoadingVideo.visibility = View.GONE
                            viewBinding.errorContent.visibility = View.VISIBLE
                        } else {
                            viewBinding.progressLoadingVideo.visibility = View.VISIBLE
                        }
                        viewBinding.playPauseButton.setImageResource(R.drawable.play_icon)
                    }
                    Player.STATE_IDLE -> {
                        // The player is idle, meaning it holds only limited resources.The player must be prepared before it will play the media.
                        viewBinding.playPauseButton.setImageResource(R.drawable.play_icon)
                        player?.prepare()
                        player?.play()
                    }
                    Player.STATE_ENDED -> {
                        // The player has finished playing the media.
                        viewBinding.playPauseButton.setImageResource(R.drawable.play_icon)
                        //player?.pause()
                    }
                    else -> {
                        // Other things
                    }
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                // Handle the player error here
                playerHasError = true
                viewBinding.errorContent.visibility = View.VISIBLE
                viewBinding.progressLoadingVideo.visibility = View.GONE
                if (error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
                    player?.stop()
                    player = null
                    viewBinding.errorText.text =
                        if (currentLanguage == "en") "Something is wrong" else "هناك خطأ ما"
                } else if (error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED
                    || error.errorCode == PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT
                ) {
                    viewBinding.errorText.text =
                        if (currentLanguage == "en") "Check your internet connection" else "قم بفحص اتصال الانترنت"
                } else if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
                ) {
                    player?.stop()
                    player = null
                    viewBinding.errorText.text =
                        if (currentLanguage == "en") "URL error" else "خطأ في عنوان URL"
                }
                Log.d("Player Error", "${error.errorCode}")
                Log.d("Player Error", "${error.errorCodeName}")
                Log.d("Player Error", "${error.message}")
                Log.d("Player Error", "Url Media not valid or has other error....")
            }
        })
    }

    ////////////////////////////////////////////////////////////////////////
    fun lastMediaWatchingData(
        id: Int,
        type: String,
        duration: Double,
        currentTime: Double
    ): HashMap<String, Any> {
        val result = HashMap<String, Any>()
        result["id"] = id
        result["type"] = type
        result["duration"] = duration
        result["currentTime"] = currentTime

        return result
    }

    fun callAddWatchedEpisodesToTheList() {
        if (isReadyPlayer) {
            addWatchedEpisodesToTheList(
                videoMediaID[startVideoPosition].toIntOrNull() ?: 0,
                player?.duration?.div(1000)?.toDouble() ?: 0.0,
                (player?.currentPosition?.div(1000))?.toDouble() ?: 0.0,
            )
        }
    }

    private var watchedEpisodesArray: List<HashMap<String, Any>> = listOf()
    fun addWatchedEpisodesToTheList(
        idL: Int,
        durationL: Double,
        currentTimeL: Double
    ) {
        if (mediaType == series) {
            val isIdNotFound = watchedEpisodesArray.none { it["id"] == idL }

            if (isIdNotFound) {
                //println("ID $idToCheck not found in the array.")
                val result = lastMediaWatchingData(idL, series, durationL, currentTimeL)
                watchedEpisodesArray += result
            } else {
                //println("ID $idToCheck found in the array.")
                for (map in watchedEpisodesArray) {
                    if (map["id"] == idL) {
                        map["duration"] = durationL
                        map["currentTime"] = currentTimeL
                    }
                }
            }
        }
    }

    fun returnDataAfterClosePlayer() {
        if (mediaType == movie) {
            /*"{type: movie, duration: " + player?.duration?.div(1000)
                .toString() + ", currentTime: " + (player?.currentPosition?.div(1000)).toString() + "}"*/

            val result = lastMediaWatchingData(
                movieMedia?.mediaID?.toInt() ?: 0,
                movie,
                player?.duration?.div(1000)?.toDouble() ?: 0.0,
                (player?.currentPosition?.div(1000))?.toDouble() ?: 0.0
            )

            var watchedMovieArray: List<HashMap<String, Any>> = listOf(result)
            if(DowplayPlugin.myResultCallback != null) {
                DowplayPlugin.myResultCallback.success(watchedMovieArray)
                DowplayPlugin.myResultCallback = null
            }
        } else {
            if(DowplayPlugin.myResultCallback != null) {
                DowplayPlugin.myResultCallback.success(watchedEpisodesArray)
                DowplayPlugin.myResultCallback = null
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////
    @SuppressLint("NewApi")
    private fun initializeBinding() {

        /////////////////////////////////////////
        viewBinding.backButton.setOnClickListener {
            vibratePhone()
            addToWatchingListAPI()
            returnDataAfterClosePlayer()
            finishAndRemoveTask()
            //finish()
        }
        viewBinding.fullScreenScale.setOnClickListener {
            vibratePhone()
            fullScreenScale()
        }
        viewBinding.playPauseButton.setOnClickListener {
            vibratePhone()
            if (player != null) {
                if (player?.isPlaying == true) {
                    pauseVideo()
                } else {
                    playVideo()
                }
            }
        }
        viewBinding.goNext10SecondButton.setOnClickListener {
            vibratePhone()
            goForward10Seconds()
        }
        viewBinding.goBack10SecondButton.setOnClickListener {
            vibratePhone()
            backOff10Seconds()
        }
        viewBinding.nextButton.setOnClickListener {
            vibratePhone()
            next()
        }
        viewBinding.previousButton.setOnClickListener {
            vibratePhone()
            previous()
        }
        viewBinding.settingButton.setOnClickListener {
            vibratePhone()
            if (player != null) {
                settingScreen()
            }
        }
        viewBinding.playerSettingText.setOnClickListener {
            vibratePhone()
            if (player != null) {
                settingScreen()
            }
        }
        viewBinding.downloadButton.setOnClickListener {
            vibratePhone()
            download()
        }
        viewBinding.pictureOnPictureButton.setOnClickListener {
            vibratePhone()
            if (isPiPSupported(activity)) {
                showVideoAsPictureOnPicture()
            } else {
                Toast.makeText(
                    this,
                    if (currentLanguage == "en") "Picture-in-Picture mode not supported" else "جهازك لا يدعم خاصية PIP",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

    var enable: Boolean = false
    private fun fullScreenScale() {
        if (enable) {
            viewBinding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            enable = false
        } else {
            viewBinding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            enable = true
        }
    }

    private fun playVideo() {
        viewBinding.playPauseButton.setImageResource(R.drawable.pause_icon)
        player?.play()
    }

    private fun pauseVideo() {
        viewBinding.playPauseButton.setImageResource(R.drawable.play_icon)
        player?.pause()
    }

    private fun goForward10Seconds() {
        val currentPosition = player?.currentPosition
        if (currentPosition != null) {
            player?.seekTo(currentPosition + 10000)
        }
    }

    private fun backOff10Seconds() {
        val currentPosition = player?.currentPosition
        if (currentPosition != null) {
            player?.seekTo(currentPosition - 10000)
        }
    }


    private fun previous() {
        if (startVideoPosition > 0) {
            addToWatchingListAPI()
            startVideoPosition = (startVideoPosition - 1 + videoUris.size) % videoUris.size
            setGreenColorForDownloadButtonIfIsDownloaded(mediaType)
            //player?.seekToPreviousMediaItem()
            player?.seekToDefaultPosition(startVideoPosition)
            viewBinding.videoSubTitle.text = videoSubTitle[startVideoPosition]
            playbackPosition = 0L
            seekToLastWatching()
        } else {
            Toast.makeText(
                this,
                if (currentLanguage == "en") "There are no previous videos" else "لا يوجد فيديو سابق",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun next() {
        if (videoUris.size - 1 > startVideoPosition) {
            addToWatchingListAPI()
            startVideoPosition = (startVideoPosition + 1) % videoUris.size
            setGreenColorForDownloadButtonIfIsDownloaded(mediaType)
            //player?.seekToNextMediaItem()
            player?.seekToDefaultPosition(startVideoPosition)
            viewBinding.videoSubTitle.text = videoSubTitle[startVideoPosition]
            playbackPosition = 0L
            seekToLastWatching()
        } else {
            Toast.makeText(
                this,
                if (currentLanguage == "en") "There are no next videos" else "لا يوجد فيديو تالي",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    var radioButtonAudioSelected = 0;
    var radioButtonSubtitleSelected = 0;
    private fun settingScreen() {
        pauseVideo()

        val customDialog =
            LayoutInflater.from(this).inflate(R.layout.setting, viewBinding.root, false)
        val bindingMF = SettingBinding.bind(customDialog)
        bindingMF.audioRadioGroup.visibility = View.GONE
        bindingMF.subtitleRadioGroup.visibility = View.GONE
        bindingMF.englishAudioRadioButton.visibility = View.GONE
        bindingMF.arabicAudioRadioButton.visibility = View.GONE
        bindingMF.englishSubtitleRadioButton.visibility = View.GONE
        bindingMF.arabicSubtitleRadioButton.visibility = View.GONE
        bindingMF.offSubtitleRadioButton.visibility = View.GONE
        bindingMF.audioTitle.visibility = View.GONE
        bindingMF.subtitleTitle.visibility = View.GONE
        bindingMF.noSettingsForVideoTitle.visibility = View.GONE
        (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked =
            true
        (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked =
            true
        setPlayerLanguage(currentLanguage, bindingMF)
        ////////////////////////////////////////////////////////////////////////////////////////////
        try {
            var mappedTrackInfo: MappingTrackSelector.MappedTrackInfo =
                Assertions.checkNotNull(trackSelection?.currentMappedTrackInfo);
            //var parameters: DefaultTrackSelector.Parameters = trackSelection?.parameters!!
            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                //var trackType: Int = mappedTrackInfo.getRendererType(rendererIndex)
                var trackGroupArray = mappedTrackInfo.getTrackGroups(rendererIndex)
                onTracksChanged(trackGroupArray, trackSelection, bindingMF, rendererIndex)
                /*var isRendererDisabled = parameters.getRendererDisabled(rendererIndex);
                var selectionOverride =
                parameters.getSelectionOverride(rendererIndex, trackGroupArray)*/

            }


            var builder: DefaultTrackSelector.Parameters.Builder =
                trackSelection!!.buildUponParameters()
            bindingMF.audioRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == bindingMF.englishAudioRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        //.setPreferredTextLanguage("en")
                        .setPreferredAudioLanguage("en")
                    trackSelection?.setParameters(builder)
                    radioButtonAudioSelected = 0
                    (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked =
                        true
                } else if (checkedId == bindingMF.arabicAudioRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        //.setPreferredTextLanguage("ar")
                        .setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonAudioSelected = 1
                    (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked =
                        true
                }
            }
            bindingMF.subtitleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == bindingMF.englishSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage("en")
                    //.setPreferredAudioLanguage("en")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 0
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked =
                        true
                } else if (checkedId == bindingMF.arabicSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage("ar")
                    //.setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 1
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked =
                        true
                } else if (checkedId == bindingMF.offSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage(null)
                    //.setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 2
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked =
                        true
                }
            }

            //////////////////////////////////////////////
            val builderDialog = AlertDialog.Builder(context, R.style.FullScreenDialogTheme)
            builderDialog.setView(customDialog)
            val alertDialog = builderDialog.create()
            alertDialog.show()
            //////////////////////////////////////////////
            bindingMF.closeSettingButton.setOnClickListener {
                playVideo()
                alertDialog.cancel()
            }
            //////////////////////////////////////////////
        } catch (e: Exception) {

            Toast.makeText(
                this,
                if (currentLanguage == "en") "Please wait..." else "يرجى الانتظار...",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun onTracksChanged(
        trackGroups: TrackGroupArray,
        trackSelection: DefaultTrackSelector,
        bindingMF: SettingBinding,
        rendererIndex: Int
    ) {
        //var audios = 0;
        //var subtitlesTracks = 0;
        Log.d("TAG", "Track Changed " + trackGroups.length)
        //Log.d("TAG", "Track selection : " + trackSelections.length)
        val mappedTrackInfo: MappingTrackSelector.MappedTrackInfo? =
            trackSelection?.currentMappedTrackInfo
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_VIDEO)
                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
            ) {
                Log.d("TAG", "Not Supported Video Track")
            }
        }
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTypeSupport(C.TRACK_TYPE_AUDIO)
                == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS
            ) {
                Log.d("TAG", "Not Supported Audio Track")
            }
        }

        if (!trackGroups.isEmpty) {

            for (arrayIndex in 0 until trackGroups.length) {
                for (groupIndex in 0 until trackGroups[arrayIndex].length) {

                    //////
                    var trackName = DefaultTrackNameProvider(resources).getTrackName(
                        trackGroups.get(arrayIndex).getFormat(groupIndex)
                    )
                    var isTrackSupported = mappedTrackInfo?.getTrackSupport(
                        rendererIndex,
                        arrayIndex,
                        groupIndex
                    ) == C.FORMAT_HANDLED
                    Log.d(
                        "Booom2023",
                        "Aaaaaaa:::item " + groupIndex + ": trackName: " + trackName + ", isTrackSupported: " + isTrackSupported
                    )
                    /////
                    val sampleMimeType =
                        trackGroups[arrayIndex].getFormat(groupIndex).sampleMimeType
                    if (sampleMimeType != null && sampleMimeType.contains("audio")) {
                        //video contains audio
                        Log.d("TAG Test HAS AUDIO", sampleMimeType)
                        Log.d("TAG Test HAS AUDIO", "HAS AUDIO")
                        bindingMF.audioRadioGroup.visibility = View.VISIBLE
                        if (trackName.lowercase().contains("English".lowercase())) {
                            bindingMF.audioTitle.visibility = View.VISIBLE
                            bindingMF.englishAudioRadioButton.visibility = View.VISIBLE
                        }
                        if (trackName.lowercase().contains("Arabic".lowercase())) {
                            print("Bom Arabic")
                            bindingMF.audioTitle.visibility = View.VISIBLE
                            bindingMF.arabicAudioRadioButton.visibility = View.VISIBLE
                        }
                    }
                    if (sampleMimeType != null && sampleMimeType.contains("x-quicktime-tx3g")) {
                        Log.d("TAG", "HAS Subtitle $sampleMimeType")
                        bindingMF.subtitleRadioGroup.visibility = View.VISIBLE
                        if (trackName.lowercase().contains("English".lowercase())) {
                            bindingMF.subtitleTitle.visibility = View.VISIBLE
                            bindingMF.offSubtitleRadioButton.visibility = View.VISIBLE
                            bindingMF.englishSubtitleRadioButton.visibility = View.VISIBLE
                        }
                        if (trackName.lowercase().contains("Arabic".lowercase())) {
                            bindingMF.subtitleTitle.visibility = View.VISIBLE
                            bindingMF.offSubtitleRadioButton.visibility = View.VISIBLE
                            bindingMF.arabicSubtitleRadioButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////
        }
        if (!bindingMF.audioTitle.isVisible && !bindingMF.subtitleTitle.isVisible) {
            bindingMF.noSettingsForVideoTitle.visibility = View.VISIBLE
        } else {
            bindingMF.noSettingsForVideoTitle.visibility = View.GONE
        }
    }

    private fun setPlayerLanguage(lang: String, bindingMF: SettingBinding?) {
        if (lang.contains("ar")) {
            viewBinding.playerSettingText.text = "الإعدادات"
            if (bindingMF != null) {
                bindingMF.audioTitle.text = "الصوت"
                bindingMF.subtitleTitle.text = "الترجمة"
                bindingMF.offSubtitleRadioButton.text = "ايقاف"
                bindingMF.noSettingsForVideoTitle.text = "لا يوجد اعدادات لهذا الفيديو"
            }
        } else {
            viewBinding.playerSettingText.text = "Setting"
            if (bindingMF != null) {
                bindingMF.subtitleTitle.text = "Subtitle"
                bindingMF.offSubtitleRadioButton.text = "Off"
                bindingMF.noSettingsForVideoTitle.text = "No settings for this video"
            }
        }
    }

    private fun download() {
        var result = 0
        if (mediaType == movie) {
            result = DownloaderDowPlay(context, activity, currentLanguage).startDownload(
                movieMedia?.info?.downloadURL.toString(),
                movieMedia?.title.toString(),
                movieMedia?.mediaType.toString(),
                movieMedia?.mediaID.toString(),
                jsonPlayMovieData ?: "",
                movieMedia?.userID.toString(),
                movieMedia?.profileID.toString(),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false
            )
        } else {
            //episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.id.toString()

            val gson = Gson()
            /*val jsonObjectEpisodeInfo =
                gson.toJsonTree(episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)).asJsonObject*/
            val episodeJson =
                gson.toJson(episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition))

            result = DownloaderDowPlay(context, activity, currentLanguage).startDownload(
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.downloadURL.toString(),
                episodeMedia?.mediaGroup?.tvShow?.title ?: "",
                episodeMedia?.mediaType ?: "",
                episodeMedia?.mediaGroup?.itemsIDS?.tvShowID ?: "",
                jsonPlayEpisodeData ?: "",
                episodeMedia?.userID ?: "",
                episodeMedia?.profileID ?: "",
                episodeMedia?.mediaGroup?.itemsIDS?.seasonID ?: "",
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.id.toString(),
                episodeMedia?.mediaGroup?.season?.seasonNumber ?: "",
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.order.toString(),
                episodeMedia?.mediaGroup?.season?.title ?: "",
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.title.toString(),
                episodeJson ?: "",
                false
            )
        }
        if (result == 1) {
            viewBinding.downloadButton.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.blue_download
                )
            );
            Toast.makeText(
                this,
                if (currentLanguage == "en") "Start downloading..." else "جاري التحميل...",
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                if (currentLanguage == "en") "Video was downloaded" else "تم تحميل الفيديو",
                Toast.LENGTH_LONG
            ).show()
            //viewBinding.downloadButton.setColorFilter(Color.parseColor("#ffffff"));
        }
    }

    ////////////////////////
    private fun isPiPSupported(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)
        } else {
            false
        }
    }

    // Check if picture-in-picture settings are enabled
    fun isPiPSettingsEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
    }

    // Open device settings to enable picture-in-picture settings
    fun openPiPSettings(context: Context) {
        val packageName = context.packageName
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    ////////////////////////
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showVideoAsPictureOnPicture() {
        // if (isInPictureInPictureMode)
        /*this.enterPictureInPictureMode().also {
            viewBinding.topController.visibility = View.GONE
            viewBinding.bottomController.visibility = View.GONE
            viewBinding.playerView.hideController()
        }*/
        val supportsPiP = isPiPSupported(activity)
        val isSettingsEnabled = isPiPSettingsEnabled(activity)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (supportsPiP) {
                val aspectRatio =
                    Rational(viewBinding.playerView.width, viewBinding.playerView.height)
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build()
                enterPictureInPictureMode(params)
            } /*else if (supportsPiP && !isSettingsEnabled) {
                // Picture-in-picture settings are disabled, guide the user to enable them manually
                openPiPSettings(activity)
            } */else {
                /*Toast.makeText(
                    this,
                    if (currentLanguage == "en") "Picture-in-Picture mode not supported" else "جهازك لا يدعم خاصية PIP",
                    Toast.LENGTH_LONG
                ).show()*/
            }
            viewBinding.topController.visibility = View.GONE
            viewBinding.bottomController.visibility = View.GONE
            viewBinding.playerView.hideController()
        }
    }
    //////////////////////////////////////////////////////////////////

    private var movieMedia: MovieMedia? = null
    private var episodeMedia: EpisodeMedia? = null
    private var mediaType: String = ""
    private var startVideoPosition: Int = 0
    private var currentLanguage: String = "en"
    private var jsonPlayMovieData: String? = null
    private var jsonPlayEpisodeData: String? = null
    private var isReadyPlayer = false
    private fun initToGetDataFromIntentAndTypeMedia() {
        //System.out.println("Bom Gson::: "+json);
        print("Bom 101:::")
        jsonPlayMovieData = intent.getStringExtra("PlayMovieData")
        jsonPlayEpisodeData = intent.getStringExtra("PlayEpisodeData")
        if (jsonPlayMovieData != null) {

            movieMedia = MovieMedia.fromJson(jsonPlayMovieData.toString())

            mediaType = movieMedia?.mediaType ?: movie
            currentLanguage = movieMedia?.lang ?: "en"
            setPlayerLanguage(currentLanguage, null)

            videoMediaID = arrayOf(movieMedia?.mediaID.toString())
            videoUris = arrayOf(movieMedia?.url.toString())
            videoTitle = arrayOf(movieMedia?.title.toString())
            viewBinding.videoTitle.text = videoTitle[0]
            videoSubTitle += ("")
            ////////////////////////
            setGreenColorForDownloadButtonIfIsDownloaded(mediaType)
        } else if (jsonPlayEpisodeData != null) {
            episodeMedia = EpisodeMedia.fromJson(jsonPlayEpisodeData.toString())

            mediaType = episodeMedia?.mediaType ?: series
            currentLanguage = episodeMedia?.lang ?: "en"
            setPlayerLanguage(currentLanguage, null)

            viewBinding.videoTitle.text = episodeMedia?.mediaGroup?.tvShow?.title.toString()

            for ((index, item) in episodeMedia?.mediaGroup?.episodes?.withIndex()!!) {
                println("Item $index is $item")
                videoMediaID += arrayOf(item.id.toString())
                videoUris += (item.mediaURL.toString())
                videoSubTitle += (item.title.toString())
                //Log.d("Path URL MEDIA:",item.mediaURL.toString())
            }
            val index =
                episodeMedia?.mediaGroup?.episodes?.indexOfFirst { info -> info.id == episodeMedia?.info?.id }
            startVideoPosition = index ?: 0
            if (0 > startVideoPosition) {
                startVideoPosition = 0
            }
            print("what the hell ? " + startVideoPosition)
            /*startVideoPosition = episodeMedia?.info?.order?.toIntOrNull() ?: 0
            if (startVideoPosition > 0) {
                startVideoPosition -= 1
            }*/
            //////
            viewBinding.videoTitle.text = episodeMedia?.mediaGroup?.tvShow?.title.toString()
            viewBinding.videoSubTitle.text = videoSubTitle[startVideoPosition]
            /////
            setGreenColorForDownloadButtonIfIsDownloaded(mediaType)
        } else {
            isReadyPlayer = false
            Toast.makeText(
                this,
                if (currentLanguage == "en") "There is no content to display" else "لا يوجد محتوى للعرض",
                Toast.LENGTH_LONG
            ).show()
        }

        ///////
        ////////////////////////////////////////////////////////////////////////////////////////////
        //hide download icon if download url is empty...
        if (mediaType == movie) {
            viewBinding.downloadButton.visibility =
                if (movieMedia?.info?.downloadURL.toString().trim()
                        .isEmpty() || movieMedia?.info?.downloadURL == null
                ) View.GONE else View.VISIBLE;
        } else {
            viewBinding.downloadButton.visibility =
                if (episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.downloadURL.toString()
                        .trim()
                        .isEmpty()
                    || episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.downloadURL == null
                ) View.GONE else View.VISIBLE;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        ///////
    }

    private fun setGreenColorForDownloadButtonIfIsDownloaded(mediaType: String) {
        var downloadInfo = HashMap<String, Any>()
        if (mediaType == series) {
            downloadInfo = DatabaseHelper(context).getDownloadInfoFromDB(
                episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.id.toString(),
                mediaType
            )
            //videoUris[startVideoPosition] = downloadInfo["video_path"].toString()
        } else {
            downloadInfo = DatabaseHelper(context).getDownloadInfoFromDB(
                movieMedia?.mediaID.toString(),
                mediaType
            )
        }
        //////////
        //Log.d("1122334455","${downloadInfo["status"]}")*/
        if (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_SUCCESSFUL && downloadInfo["status"] != null) {
            if (downloadInfo["video_path"].toString() != "" && downloadInfo["video_path"] != null) {
                videoUris[startVideoPosition] = downloadInfo["video_path"].toString()
                viewBinding.downloadButton.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.green_download
                    )
                );
                ///////
                //refresh player for new urls links
                val mediaItem = videoUris.map { MediaItem.fromUri(it) }
                player?.setMediaItems(mediaItem)
                ///////
            }
        } else if (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_RUNNING && downloadInfo["status"] != null) {
            viewBinding.downloadButton.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.blue_download
                )
            );
        } else if (downloadInfo["status"] == DownloadManagerSTATUS.STATUS_FAILED && downloadInfo["status"] != null) {
            viewBinding.downloadButton.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.red_download
                )
            );
        } else {
            viewBinding.downloadButton.setColorFilter(Color.parseColor("#ffffff"));
        }
    }


    @SuppressLint("InlinedApi", "ClickableViewAccessibility")
    private fun hideSystemUi() {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val exoPositionView = viewBinding.playerView.findViewById<TextView>(R.id.exo_position)
        val exoDurationView = viewBinding.playerView.findViewById<TextView>(R.id.exo_duration)
        val exoContentTimeBar =
            viewBinding.playerView.findViewById<LinearLayout>(R.id.exo_content_time_bar)
        viewBinding.playerView.showController()
        viewBinding.playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener {
            if (viewBinding.playerView.isControllerFullyVisible) {
                viewBinding.topController.visibility = View.VISIBLE
                viewBinding.bottomController.visibility = View.VISIBLE
                exoPositionView.visibility = View.VISIBLE
                exoDurationView.visibility = View.VISIBLE
                exoContentTimeBar.visibility = View.VISIBLE
                //exoContentTimeBar.setBackgroundColor(0X42000000)
                //viewBinding.downloadButton.setColorFilter( ContextCompat.getColor(context, R.color.green_download));
                //viewBinding.playerView.showController()
                Log.d("Heel-VISIBLE", "Bom")
            } else {
                viewBinding.topController.visibility = View.GONE
                viewBinding.bottomController.visibility = View.GONE
                exoPositionView.visibility = View.GONE
                exoDurationView.visibility = View.GONE
                exoContentTimeBar.visibility = View.GONE
                //exoContentTimeBar.setBackgroundColor(Color.TRANSPARENT)
                //viewBinding.playerView.hideController()
                Log.d("Heel-GONE", "Bom")
            }
            callAddWatchedEpisodesToTheList()
        })
    }

    private fun addToWatchingListAPI() {
        if (player != null && (player?.currentPosition ?: 0) > 0 && (player?.duration
                ?: 0) > 0
        ) {
            if (mediaType == movie) {
                //movie
                Log.d("Bom this is time: ", (player?.currentPosition?.div(1000)).toString())
                RetrofitBuildRequest().createRequestForAddWatching(
                    movieMedia?.apiBaseURL.toString(),
                    movieMedia?.profileID.toString(),
                    movie,
                    movieMedia?.mediaID.toString(),
                    //movieMedia?.info?.watching?.duration.toString(),
                    player?.duration?.div(1000).toString(),
                    (player?.currentPosition?.div(1000)).toString(),
                    DowplayPlugin.accessToken,
                    currentLanguage
                )
            } else if (mediaType == series) {
                //episode
                RetrofitBuildRequest().createRequestForAddWatching(
                    episodeMedia?.apiBaseURL.toString(),
                    episodeMedia?.profileID.toString(),
                    "episode",
                    episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.id.toString(),
                    player?.duration?.div(1000).toString(),
                    //episodeMedia?.mediaGroup?.episodes?.get(startVideoPosition)?.watching?.duration.toString(),
                    (player?.currentPosition?.div(1000)).toString(),
                    DowplayPlugin.accessToken,
                    currentLanguage
                )
            }
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (lifecycle.currentState == Lifecycle.State.CREATED) {
            //user clicked on close button of PiP window
            Log.d("PiP-is-close", "PiP is close by click on close button")
            vibratePhone()
            addToWatchingListAPI()
            returnDataAfterClosePlayer()
            finishAndRemoveTask()
        }
        else if (lifecycle.currentState == Lifecycle.State.STARTED){
            if (isInPictureInPictureMode) {
                // user clicked on minimize button
                addToWatchingListAPI()
                //returnDataAfterClosePlayer()
                Log.d("PiP-is-minimize", "PiP is minimize by click on full screen button")
            } else {
                // user clicked on maximize button of PiP window
                Log.d("PiP-is-maximize", "PiP is maximize by click on full screen button")
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        Log.d("current stats screen:", "onStart")
        if (startVideoPosition == 0) {
            initToGetDataFromIntentAndTypeMedia()
        }
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        Log.d("current playbackPosi", "> $playbackPosition")
        Log.d("current stats screen:", "onResume")
        hideSystemUi()
        if ((Util.SDK_INT <= 23)) {
            initializePlayer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onPause() {
        super.onPause()
        //addToWatchingList()
        //player?.currentPosition
        Log.d("current stats screen:", "onPause")
        showVideoAsPictureOnPicture()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    public override fun onStop() {
        super.onStop()
        Log.d("current stats screen:", "onStop")
        //showVideoAsPictureOnPicture()
        addToWatchingListAPI()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        Log.d("current stats screen:", "onDestroy")
        addToWatchingListAPI()
        //finish()
        /*if (Util.SDK_INT > 23) {
            releasePlayer()
        }*/
    }

    override fun onBackPressed() {
        releasePlayer()
        moveTaskToBack(true)
        Log.d("current stats screen:", "onBackPressed")

    }


    private var playWhenReady = true
    private var playbackPosition = 0L
    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            startVideoPosition = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

}