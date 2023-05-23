package com.dowplay.dowplay;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.media3.common.util.UnstableApi;

import com.beust.klaxon.Klaxon;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@UnstableApi
/** DowplayPlugin */
public class DowplayPlugin extends FlutterActivity implements FlutterPlugin, MethodCallHandler, ActivityAware {
    private static final String TAG = "DowplayPlugin";
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;
    private Activity activity;
    String userId = "";
    String profileId = "";
    String lang = "";

    @Override
    public void onAttachedToActivity(ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {

    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "dowplay");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
        Log.d(TAG, "onMethodCall Test: config_downloader");
    }
/////////////////////////////////

    private static final int REQUEST_FOR_PLAY_MOVIE = 1998;
    private static final int REQUEST_FOR_PLAY_EPISODE = 1999;
    static Result myResultCallback;
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("player_result", "Start");
        if (requestCode == REQUEST_FOR_PLAY_MOVIE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("player_result");
                Log.d("player_result", result);
                myResultCallback.success(result);
            } else {
                //Toast.makeText(this, "Result not OK", Toast.LENGTH_SHORT).show();
                Log.d("player_result", "Error");
                myResultCallback.success("{}");
            }
        }
        if (requestCode == REQUEST_FOR_PLAY_EPISODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("player_result");
                Log.d("player_result", result);
                myResultCallback.success(result);
            } else {
                //Toast.makeText(this, "Result not OK", Toast.LENGTH_SHORT).show();
                Log.d("player_result", "Error");
                List returnData = new ArrayList();
                myResultCallback.success(returnData);
            }
        }
    }*/

    ///////////////////////////////
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("config_downloader")) {
            Log.d(TAG, "onMethodCall: config_downloader");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            ///{user_id=245394, profile_id=562674, lang=ar}
            //userId = call.arguments.toString().replaceAll(".*user_id=(\\d+).*", "$1");
            //profileId = call.arguments.toString().replaceAll(".*profile_id=(\\d+).*", "$1");
            //lang = call.arguments.toString().replaceAll(".*lang=(.+).*", "$1");
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            userId = (String) object.get("user_id");
            profileId = (String) object.get("profile_id");
            lang = (String) object.get("lang");

            result.success(true);
///////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("play_movie")) {
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
            myResultCallback = result;
            context.startActivity(intent);
            //result.success("{type: movie, duration: 262.891, currentTime: 11.721485617,id:377530}");
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("play_episode")) {
            Log.d(TAG, "onMethodCall: play_episode");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            Intent intent = new Intent(context, CustomPlayerActivity.class);
            intent.putExtra("PlayEpisodeData", json);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            myResultCallback = result;
            context.startActivity(intent);
            //result.success("[{duration: 428.569, id: 64864, type: series, currentTime: 34.420640823}, {currentTime: 9.164657244, type: series, id: 64865, duration:455.463}]");
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("get_downloads_list")) {
            Log.d(TAG, "onMethodCall: get_downloads_list: " + userId + " > " + profileId + " > " + lang);
            //showDownloadStatePermission();
            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("tvshow_seasons_downloads_list")) {
            Log.d(TAG, "onMethodCall: tvshow_seasons_downloads_list");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{tvshow_id=1532}
            //String tvshowId = call.arguments.toString().replaceAll(".*tvshow_id=(\\d+).*", "$1");
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String tvshowId = (String) object.get("tvshow_id");

            Log.d(TAG, "onMethodCall: > " + tvshowId);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllSeasons(tvshowId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("season_episodes_downloads_list")) {
            Log.d(TAG, "onMethodCall: season_episodes_downloads_list");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //String input = "{season_id=3236, tvshow_id=1532}";
            //String seasonId = call.arguments.toString().replaceAll(".*season_id=(\\d+).*", "$1");
            //String tvshowId = call.arguments.toString().replaceAll(".*tvshow_id=(\\d+).*", "$1");
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String seasonId = (String) object.get("season_id");
            String tvshowId = (String) object.get("tvshow_id");
            Log.d(TAG, "onMethodCall: " + seasonId + " > " + tvshowId);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllEpisodes(seasonId, tvshowId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("start_download_movie")) {
            Log.d(TAG, "onMethodCall: start_download_movie");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            MovieMedia movieMedia = MovieMedia.Companion.fromJson(json);
            System.out.println("B7b Gson::: " + movieMedia);
            new DownloaderDowPlay(context, activity, lang).startDownload(movieMedia.getInfo().getDownloadURL(), movieMedia.getTitle(), movieMedia.getMediaType(),
                    movieMedia.getMediaID(), json, movieMedia.getUserID(), movieMedia.getProfileID(), "", "", "", "", "", "", "");

            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("start_download_episode")) {
            Log.d(TAG, "onMethodCall: start_download_episode");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            EpisodeMedia episodeMedia = EpisodeMedia.Companion.fromJson(json);
            System.out.println("B7b Gson::: " + episodeMedia);
            //////////////////////
            //JsonElement jsonObjectEpisodeInfo = gson.toJsonTree(episodeMedia.getInfo());
            //JsonObject episodeJson = jsonObjectEpisodeInfo.getAsJsonObject();
            //String episodeJsonStringOld = gson.toJson(episodeJson);
            String episodeJsonString = gson.toJson(episodeMedia.getInfo());
            //Log.d("episodeJsonStringOld",episodeJsonStringOld);
            //Log.d("episodeJsonString",episodeJsonString);
            /////////////////////
            //Log.d("infoEEEE::: ",episodeMedia.getInfo().getDuration());
            new DownloaderDowPlay(context, activity, lang).startDownload(episodeMedia.getInfo().getDownloadURL(), episodeMedia.getMediaGroup().getTvShow().getTitle(), episodeMedia.getMediaType(),
                    episodeMedia.getMediaGroup().getItemsIDS().getTvShowID(), json, episodeMedia.getUserID(), episodeMedia.getProfileID(),
                    episodeMedia.getMediaGroup().getItemsIDS().getSeasonID(), episodeMedia.getInfo().getId().toString(), episodeMedia.getMediaGroup().getSeason().getSeasonNumber(),
                    episodeMedia.getInfo().getOrder(), episodeMedia.getMediaGroup().getSeason().getTitle(), episodeMedia.getInfo().getTitle(), episodeJsonString);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("pause_download")) {
            Log.d(TAG, "onMethodCall: pause_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}

            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String mediaType = (String) object.get("mediaType");
            String mediaId = (String) object.get("mediaId");

            new DownloaderDowPlay(context, activity, lang).pauseDownload(mediaId, mediaType);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("resume_download")) {
            Log.d(TAG, "onMethodCall: resume_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}

            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String mediaType = (String) object.get("mediaType");
            String mediaId = (String) object.get("mediaId");

            new DownloaderDowPlay(context, activity, lang).resumeDownload(mediaId, mediaType);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("cancel_download")) {
            Log.d(TAG, "onMethodCall: cancel_download");
            Log.d(TAG, "onMethodCall: " + call.arguments.toString());
            //{mediaType=movie, mediaId=377530}

            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            Log.d(TAG, "onMethodCall???: " + object);

            String mediaType = (String) object.get("mediaType");
            String mediaId = (String) object.get("mediaId");

            Log.d(TAG, "onMethodCall???: " + mediaType + " > " + mediaId);
            new DownloaderDowPlay(context, activity, lang).cancelDownload(mediaId, mediaType);
            result.success(new DownloaderDowPlay(context, activity, lang).getAllDownloadMedia(userId, profileId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("get_download_movie")) {
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String mediaId = (String) object.get("media_id");//movie
            result.success(new DownloaderDowPlay(context, activity, lang).getDownloadMediaInfo(userId, profileId, mediaId, "movie", "", ""));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else if (call.method.equals("get_download_episode")) {
            Gson gson = new Gson();
            String json = gson.toJson(call.arguments);
            HashMap<String, Object> object = gson.fromJson(json, HashMap.class);
            String mediaId = (String) object.get("media_id");//episod
            String tvshowId = (String) object.get("tvshow_id");
            String seasonId = (String) object.get("season_id");
            result.success(new DownloaderDowPlay(context, activity, lang).getDownloadMediaInfo(userId, profileId, mediaId, "series", seasonId, tvshowId));
//////////////////////////////////////////////////////////////////////////////////////////////////
        } else {
            result.notImplemented();
        }
    }

////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    //////////////////////////////////////////////////////////
    /*
    int myRequestCode = 1997;
    private void showPhoneStatePermission() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.d("onMethodCall", ":2:");
            //requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},requestCode);

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", " > ");
                Log.d("onMethodCall", ":1:");
            } else {

            }
        } else {
            Log.d("onMethodCall", ":3:");
            Toast.makeText(activity, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
        }
    }*/
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////FOR Permission
    /*
    int myRequestCode = 1997;
    boolean permissionToDownload = false;

    private void showDownloadStatePermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (lang.equalsIgnoreCase("en")) {
                    showExplanation("Permission Needed", "Media and file access must be granted to start downloading movies and series through the app permissions option", true);
                } else {
                    showExplanation("منح الإذن", "يجب منح صلاحية الوصول للوسائط والملفات لبدء تحميل الافلام والمسلسلات من خلال خيار اذونات التطبيق", true);
                }
            } else {
                if (lang.equalsIgnoreCase("en")) {
                    showExplanation("Permission Needed", "You must grant access to media and files to start downloading movies and series", false);
                } else {
                    showExplanation("منح الإذن", "يجب منح صلاحية الوصول للوسائط والملفات لبدء تحميل الافلام والمسلسلات", false);
                }
            }
        } else {
            //Toast.makeText(activity, "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
            permissionToDownload = true;
        }
    }

    private void showExplanation(String title, String message, boolean openSettingScreen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.ScreenDialogTheme);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (openSettingScreen) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setData(uri);
                            context.startActivity(intent);
                        } else {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    myRequestCode);
                        }
                    }
                });
        builder.create().show();
    }*/
    /*
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(activity, "Permission Granted!1111111111", Toast.LENGTH_SHORT).show();
            //
        } else {
            //Toast.makeText(activity, "Permission Denied!22222222222", Toast.LENGTH_SHORT).show();
        }
    }*/
///////////////////////////////////////////////////////////////////////////
}
