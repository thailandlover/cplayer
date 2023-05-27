import 'package:dowplay/EpisodeMedia.dart';
import 'package:dowplay/MovieMedia.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dowplay_platform_interface.dart';

/// An implementation of [DowplayPlatform] that uses method channels.
class MethodChannelDowplay extends DowplayPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dowplay');

  @override
  Future<dynamic> playEpisode(EpisodeMedia media) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'play_episode', media.toJson());
    return result;
  }

  @override
  Future<dynamic> playMovie(MovieMedia media) async {
    final dynamic result =
        await methodChannel.invokeMethod<dynamic>('play_movie', media.toJson());
    return result;
  }

  @override
  Future<bool?> config(Map<String, dynamic> data) async {
    final bool? result =
        await methodChannel.invokeMethod<bool>('config_downloader', data);
    return result;
  }

  @override
  Future<dynamic> getDownloadsList() async {
    final dynamic result =
        await methodChannel.invokeMethod<dynamic>('get_downloads_list');
    return result;
  }

  @override
  Future<dynamic> getTvShowSeasonsDownloadList(
      Map<String, dynamic> data) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'tvshow_seasons_downloads_list', data);
    return result;
  }

  @override
  Future<dynamic> getSeasonEpisodesDownloadList(
      Map<String, dynamic> data) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'season_episodes_downloads_list', data);
    return result;
  }

  @override
  Future<dynamic> startDownloadMovie(dynamic item) async {
    final dynamic result =
        await methodChannel.invokeMethod<dynamic>('start_download_movie', item);
    return result;
  }

  @override
  Future<dynamic> startDownloadEpisode(dynamic item) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'start_download_episode', item);
    return result;
  }

  @override
  Future<dynamic> pauseDownload(String mediaId, String mediaType) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'pause_download', {"mediaId": mediaId, "mediaType": mediaType});
    return result;
  }

  @override
  Future<dynamic> resumeDownload(String mediaId, String mediaType) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'resume_download', {"mediaId": mediaId, "mediaType": mediaType});
    return result;
  }

  @override
  Future<dynamic> cancelDownload(String mediaId, String mediaType,
      dynamic tvShowId, dynamic seasonId) async {
    final dynamic result =
        await methodChannel.invokeMethod<dynamic>('cancel_download', {
      "mediaId": mediaId,
      "mediaType": mediaType,
      "tvshowId": tvShowId,
      "seasonId": seasonId
    });
    return result;
  }

  @override
  Future<dynamic> getDownloadMovie(String mediaId) async {
    final dynamic result = await methodChannel
        .invokeMethod<dynamic>('get_download_movie', {"media_id": mediaId});
    return result;
  }

  @override
  Future<dynamic> getDownloadEpisode(
      String mediaId, String tvShowId, String seasonId) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'get_download_episode',
        {"media_id": mediaId, "tvshow_id": tvShowId, "season_id": seasonId});
    return result;
  }
}
