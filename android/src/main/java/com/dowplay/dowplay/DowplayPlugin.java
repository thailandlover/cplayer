package com.dowplay.dowplay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.media3.common.util.UnstableApi;

import com.beust.klaxon.Klaxon;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@UnstableApi
/** DowplayPlugin */
public class DowplayPlugin implements FlutterPlugin, MethodCallHandler {
    private static final String TAG = "DowplayPlugin";
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dowplay");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("play_movie")) {
            Log.d(TAG, "onMethodCall: play_movie");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            //System.out.println("B7b Gson::: "+json);
            //MovieMedia movieMedia = MovieMedia.Companion.fromJson(json);
            //Log.d(TAG, "B7b movieMedia?.title is: " + movieMedia);
            //MovieMedia movieMedia = MovieMedia.Companion.fromJson(call.arguments.toString());
            Intent intent = new Intent(context, CustomPlayerActivity.class);
            intent.putExtra("PlayMovieData", json);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            result.success(true);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("play_episode")) {
            Log.d(TAG, "onMethodCall: play_episode");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            Intent intent = new Intent(context, CustomPlayerActivity.class);
            intent.putExtra("PlayEpisodeData", json);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            result.success(true);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("config_downloader")) {
            Log.d(TAG, "onMethodCall: config_downloader");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            result.success(true);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("get_downloads_list")) {
            Log.d(TAG, "onMethodCall: get_downloads_list");
            result.success(new DownloaderDowPlay(context).getAllDownloadMedia());
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("tvshow_seasons_downloads_list")) {
            Log.d(TAG, "onMethodCall: tvshow_seasons_downloads_list");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{tvshow_id=1532}
            String tvshowId = call.arguments.toString().replaceAll(".*tvshow_id=(\\d+).*", "$1");
            Log.d(TAG, "onMethodCall: > "+tvshowId);
            result.success(new DownloaderDowPlay(context).getAllSeasons(tvshowId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("season_episodes_downloads_list")) {
            Log.d(TAG, "onMethodCall: season_episodes_downloads_list");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //String input = "{season_id=3236, tvshow_id=1532}";
            String seasonId = call.arguments.toString().replaceAll(".*season_id=(\\d+).*", "$1");
            String tvshowId = call.arguments.toString().replaceAll(".*tvshow_id=(\\d+).*", "$1");
            Log.d(TAG, "onMethodCall: " + seasonId+" > "+tvshowId);
            result.success(new DownloaderDowPlay(context).getAllEpisodes(seasonId, tvshowId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("start_download_movie")) {
            Log.d(TAG, "onMethodCall: start_download_movie");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            MovieMedia movieMedia = MovieMedia.Companion.fromJson(json);
            System.out.println("B7b Gson::: " + movieMedia);
            new DownloaderDowPlay(context).startDownload(movieMedia.getUrl(), movieMedia.getTitle(), movieMedia.getMediaType(),
                    movieMedia.getMediaID(), call.arguments.toString(), movieMedia.getUserID(), movieMedia.getProfileID(), "", "", "", "", "", "");
            List returnData = new ArrayList();
            result.success(returnData);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("start_download_episode")) {
            Log.d(TAG, "onMethodCall: start_download_episode");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            EpisodeMedia episodeMedia = EpisodeMedia.Companion.fromJson(json);
            System.out.println("B7b Gson::: " + episodeMedia);
            new DownloaderDowPlay(context).startDownload(episodeMedia.getInfo().getDownloadURL(), episodeMedia.getMediaGroup().getTvShow().getTitle(), episodeMedia.getMediaType(),
                    episodeMedia.getMediaGroup().getItemsIDS().getTvShowID(), call.arguments.toString(), episodeMedia.getUserID(), episodeMedia.getProfileID(),
                    episodeMedia.getMediaGroup().getItemsIDS().getSeasonID(), episodeMedia.getInfo().getId().toString(), episodeMedia.getMediaGroup().getSeason().getSeasonNumber(),
                    episodeMedia.getInfo().getOrder(), episodeMedia.getMediaGroup().getSeason().getTitle(), episodeMedia.getInfo().getTitle());
            List returnData = new ArrayList();
            result.success(returnData);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("pause_download")) {
            Log.d(TAG, "onMethodCall: pause_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}
            String mediaType = call.arguments.toString().replaceAll(".*mediaType=(\\d+).*", "$1");
            String mediaId = call.arguments.toString().replaceAll(".*mediaId=(\\d+).*", "$1");
            new DownloaderDowPlay(context).pauseDownload(mediaId,mediaType);
            List returnData = new ArrayList();
            result.success(returnData);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("resume_download")) {
            Log.d(TAG, "onMethodCall: resume_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}
            String mediaType = call.arguments.toString().replaceAll(".*mediaType=(\\d+).*", "$1");
            String mediaId = call.arguments.toString().replaceAll(".*mediaId=(\\d+).*", "$1");
            new DownloaderDowPlay(context).resumeDownload(mediaId,mediaType);
            List returnData = new ArrayList();
            result.success(returnData);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("cancel_download")) {
            Log.d(TAG, "onMethodCall: cancel_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}
            String mediaType = call.arguments.toString().replaceAll(".*mediaType=(\\d+).*", "$1");
            String mediaId = call.arguments.toString().replaceAll(".*mediaId=(\\d+).*", "$1");
            new DownloaderDowPlay(context).cancelDownload(mediaId,mediaType);
            List returnData = new ArrayList();
            result.success(returnData);
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else {
            result.notImplemented();
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
