package com.dowplay.dowplay

import android.annotation.SuppressLint
import android.app.PictureInPictureParams
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.Rational
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.Assertions
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.TrackGroupArray
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.MappingTrackSelector
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.DefaultTrackNameProvider
import androidx.media3.ui.PlayerView
import com.dowplay.dowplay.MovieMedia.Companion.fromJson
import com.dowplay.dowplay.databinding.ActivityCustomPlayerBinding
import com.dowplay.dowplay.databinding.SettingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.flutter.embedding.android.FlutterActivity

@UnstableApi /**
 * A fullscreen activity to play audio or video streams.
 */
class CustomPlayerActivity() : FlutterActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityCustomPlayerBinding.inflate(layoutInflater)
    }
    private var player: ExoPlayer? = null
   /*
    var videoUris = listOf(
        Uri.parse("https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4"),
        Uri.parse("https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080pAr.mp4"),
        Uri.parse("https://storage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
    )*/
   var videoUris = arrayOf<String>()

    var videoTitle = arrayOf<String>()
    var videoSubTitle = arrayOf<String>()
    /*listOf(
       "Video 1","Klaus","Video 2"
    )*/
    var currentVideoIndex = 0

    lateinit var trackSelection: DefaultTrackSelector

    ///////////////////////////////////////////////////////////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initializeBinding()
    }

    private fun initializePlayer() {
        trackSelection = DefaultTrackSelector(this).apply{
            setParameters(buildUponParameters().setMaxVideoSizeSd())
        }
        player = ExoPlayer.Builder(this)
            .setSkipSilenceEnabled(true)
            .setTrackSelector(trackSelection)
            .setVideoScalingMode(2)
            .setAudioAttributes(AudioAttributes.DEFAULT,false)

            .build()
            .also { exoPlayer ->
                //val mediaItem = MediaItem.fromUri("https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080pAr.mp4")
                //////////////////////

                val mediaItem = videoUris.map { MediaItem.fromUri(it) }

                /////////////////////
                exoPlayer.setMediaItems(mediaItem)
                exoPlayer.prepare()
                exoPlayer.play()
                viewBinding.playerView.player = exoPlayer

            }
        movieMedia?.info?.watching?.currentTime?.toLong()?.let {
            player?.seekTo(it*1000)
            print("A7a creent time :::: "+it*1000)
        }
        playerEvent()
    }
   private fun playerEvent(){
       player?.addListener(object : Player.Listener {
           override fun onPlaybackStateChanged(@Player.State state: Int) {
               when (state) {
                   Player.STATE_READY -> {
                       // The player is able to immediately play from its current position. The player will be playing if getPlayWhenReady() is true, and paused otherwise.
                       viewBinding.progressBarVideo.visibility = View.GONE
                   }
                   Player.STATE_BUFFERING -> {
                       // The player is not able to immediately play the media, but is doing work toward being able to do so. This state typically occurs when the player needs to buffer more data before playback can start.
                       viewBinding.progressBarVideo.visibility = View.VISIBLE
                   }
                   Player.STATE_IDLE -> {
                       // The player is idle, meaning it holds only limited resources.The player must be prepared before it will play the media.
                   }
                   Player.STATE_ENDED -> {
                       // The player has finished playing the media.
                   }
                   else -> {
                       // Other things
                   }
               }
           }
       })
    }

    @SuppressLint("NewApi")
    private fun initializeBinding() {
        /////////////////////////////////////////
        viewBinding.backButton.setOnClickListener{
            vibratePhone()
            finish()
        }
        viewBinding.fullScreenScale.setOnClickListener{
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
        viewBinding.goNext10SecondButton.setOnClickListener{
            vibratePhone()
            goForward10Seconds()
        }
        viewBinding.goBack10SecondButton.setOnClickListener{
            vibratePhone()
            backOff10Seconds()
        }
        viewBinding.nextButton.setOnClickListener{
            vibratePhone()
            next()
        }
        viewBinding.previousButton.setOnClickListener{
            vibratePhone()
            previous()
        }
        viewBinding.settingButton.setOnClickListener{
            vibratePhone()
            if(player != null) {
                settingScreen()
            }
        }
        viewBinding.downloadButton.setOnClickListener{
            vibratePhone()
            download()
        }
        viewBinding.pictureOnPictureButton.setOnClickListener{
            vibratePhone()
            showVideoAsPictureOnPicture()
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
    private fun fullScreenScale(){
        if(enable) {
            viewBinding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            enable = false
        }else{
            viewBinding.playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            player?.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            enable = true
        }
    }
    private fun playVideo(){
        viewBinding.playPauseButton.setImageResource(R.drawable.pause_icon)
        player?.play()
    }
    private fun pauseVideo(){
        viewBinding.playPauseButton.setImageResource(R.drawable.play_icon)
        player?.pause()
    }

    private fun goForward10Seconds(){
        val currentPosition = player?.currentPosition
        if (currentPosition != null) {
            player?.seekTo(currentPosition + 10000 )
        }
    }

    private fun backOff10Seconds(){
        val currentPosition = player?.currentPosition
        if (currentPosition != null) {
            player?.seekTo(currentPosition - 10000 )
        }
    }


    private fun previous(){
        if(currentVideoIndex > 0) {
            currentVideoIndex = (currentVideoIndex - 1 + videoUris.size) % videoUris.size
            player?.seekToPreviousMediaItem()
            viewBinding.videoTitle.text = videoTitle[currentVideoIndex]
        }else{
            Toast.makeText(this, "There are no previous videos", Toast.LENGTH_LONG).show()
        }
    }

    private fun next(){
        if(videoUris.size-1 > currentVideoIndex) {
            currentVideoIndex = (currentVideoIndex + 1) % videoUris.size
            player?.seekToNextMediaItem()
            viewBinding.videoTitle.text = videoTitle[currentVideoIndex]
        }
        else{
            Toast.makeText(this, "There are no next videos", Toast.LENGTH_LONG).show()
        }
    }

    var radioButtonAudioSelected = 0;
    var radioButtonSubtitleSelected = 0;
    private fun settingScreen(){
        pauseVideo()
        val customDialog = LayoutInflater.from(this).inflate(R.layout.setting, viewBinding.root, false)
        val bindingMF = SettingBinding.bind(customDialog)
        bindingMF.audioRadioGroup.visibility = View.GONE
        bindingMF.subtitleRadioGroup.visibility = View.GONE
        bindingMF.englishAudioRadioButton.visibility = View.GONE
        bindingMF.arabicAudioRadioButton.visibility = View.GONE
        bindingMF.englishSubtitleRadioButton.visibility = View.GONE
        bindingMF.arabicSubtitleRadioButton.visibility = View.GONE
        bindingMF.offSubtitleRadioButton.visibility = View.GONE
        (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked = true
        (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked = true
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
                if(checkedId == bindingMF.englishAudioRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        //.setPreferredTextLanguage("en")
                        .setPreferredAudioLanguage("en")
                    trackSelection?.setParameters(builder)
                    radioButtonAudioSelected = 0
                    (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked = true
                }else if(checkedId == bindingMF.arabicAudioRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        //.setPreferredTextLanguage("ar")
                        .setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonAudioSelected = 1
                    (bindingMF.audioRadioGroup.getChildAt(radioButtonAudioSelected) as RadioButton).isChecked = true
                }
            }
            bindingMF.subtitleRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                if(checkedId == bindingMF.englishSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage("en")
                    //.setPreferredAudioLanguage("en")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 0
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked = true
                }else if(checkedId == bindingMF.arabicSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage("ar")
                    //.setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 1
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked = true
                }else if(checkedId == bindingMF.offSubtitleRadioButton.id) {
                    builder.setMaxVideoSizeSd()
                        .setPreferredTextLanguage(null)
                    //.setPreferredAudioLanguage("ar")
                    trackSelection?.setParameters(builder)
                    radioButtonSubtitleSelected = 2
                    (bindingMF.subtitleRadioGroup.getChildAt(radioButtonSubtitleSelected) as RadioButton).isChecked = true
                }
            }

            //////////////////////////////////////////////
            val dialog = MaterialAlertDialogBuilder(this).setView(customDialog)
                .setOnCancelListener { playVideo() }
                .setCancelable(false)
                .setBackground(ColorDrawable(0xCC232833.toInt()))
                .create()
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.setContentView(R.layout.setting);
            //dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.show()
            //////////////////////////////////////////////
            bindingMF.closeSettingButton.setOnClickListener{
                playVideo()
                dialog.cancel()
            }
            //////////////////////////////////////////////
        }catch (e: Exception){
            Toast.makeText(this, "Video is loading...", Toast.LENGTH_LONG).show()
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
                    Log.d("Booom2023",
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
                        if(trackName.lowercase().contains("English".lowercase())){
                            bindingMF.englishAudioRadioButton.visibility = View.VISIBLE
                        }
                        if(trackName.lowercase().contains("Arabic".lowercase())){
                            print("A7a Arabic")
                            bindingMF.arabicAudioRadioButton.visibility = View.VISIBLE
                        }
                        bindingMF.offSubtitleRadioButton.visibility = View.VISIBLE
                    }
                    if (sampleMimeType != null && sampleMimeType.contains("x-quicktime-tx3g")) {
                        Log.d("TAG", "HAS Subtitle $sampleMimeType")
                        bindingMF.subtitleRadioGroup.visibility = View.VISIBLE
                        if(trackName.lowercase().contains("English".lowercase())){
                            bindingMF.englishSubtitleRadioButton.visibility = View.VISIBLE
                        }
                        if(trackName.lowercase().contains("Arabic".lowercase())){
                            bindingMF.arabicSubtitleRadioButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
            ///////////////////////////////////////////////////////////////////////////////////////
            /*for (groupIndex in 0 until trackGroups.length) {
                for (trackIndex in 0 until trackGroups.get(groupIndex).length) {
                    var trackName = DefaultTrackNameProvider(resources).getTrackName(
                        trackGroups.get(groupIndex).getFormat(trackIndex)
                    )
                    var isTrackSupported = mappedTrackInfo?.getTrackSupport(
                        rendererIndex,
                        groupIndex,
                        trackIndex
                    ) == C.FORMAT_HANDLED
                    Log.d(
                        "Track Info",
                        "track item " + groupIndex + ": trackName: " + trackName + ", isTrackSupported: " + isTrackSupported
                    )

                }
            }*/

        }
    }

    private fun download(){

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showVideoAsPictureOnPicture(){
        // if (isInPictureInPictureMode)
        /*this.enterPictureInPictureMode().also {
            viewBinding.topController.visibility = View.GONE
            viewBinding.bottomController.visibility = View.GONE
            viewBinding.playerView.hideController()
        }*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(viewBinding.playerView.width, viewBinding.playerView.height)
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(params)
        } else {
            enterPictureInPictureMode()
        }
        viewBinding.topController.visibility = View.GONE
        viewBinding.bottomController.visibility = View.GONE
        viewBinding.playerView.hideController()
    }

    public override fun onStart() {
        super.onStart()
        getDataFromIntentAndTypeMedia()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }


    private var movieMedia: MovieMedia? = null
    private var episodeMedia: EpisodeMedia? = null
    private fun getDataFromIntentAndTypeMedia(){
        //System.out.println("A7a Gson::: "+json);
        print("A7a 101:::")
        val jsonPlayMovieData = intent.getStringExtra("PlayMovieData")
        val jsonPlayEpisodeData = intent.getStringExtra("PlayEpisodeData")
        if(jsonPlayMovieData != null) {
            movieMedia = MovieMedia.fromJson(jsonPlayMovieData.toString())
            videoUris = arrayOf(movieMedia?.info?.hdURL.toString())!!
            //videoName = arrayOf(movieMedia?.title.toString())!!
            viewBinding.videoTitle.text = movieMedia?.title.toString()
            //videoUris.plus(movieMedia?.url)
        }else if(jsonPlayEpisodeData != null){
            episodeMedia = EpisodeMedia.fromJson(jsonPlayEpisodeData.toString())
            videoUris = arrayOf(episodeMedia?.info?.mediaURL.toString())!!
            //videoName = arrayOf(episodeMedia?.info?.title.toString())!!
            viewBinding.videoTitle.text = episodeMedia?.mediaGroup?.tvShow?.title.toString()
            viewBinding.videoSubTitle.text = episodeMedia?.info?.title.toString()
            //videoUris.plus(movieMedia?.url)
        }else{
            Toast.makeText(this, "No Data Found!!!", Toast.LENGTH_LONG).show()
        }
    }
    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }
    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.playerView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        viewBinding.playerView.setControllerVisibilityListener(PlayerView.ControllerVisibilityListener {
            if(it == 0) {
                viewBinding.topController.visibility = View.VISIBLE
                viewBinding.bottomController.visibility = View.VISIBLE
            }else {
                viewBinding.topController.visibility = View.GONE
                viewBinding.bottomController.visibility = View.GONE
            }
        })
    }
    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }
}