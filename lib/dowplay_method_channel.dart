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
  Future<bool?> playEpisode(EpisodeMedia media) async {
    final bool? result =
        await methodChannel.invokeMethod<bool>('play_episode', media.toJson());
    return result;
  }
  @override
  Future<bool?> playMovie(MovieMedia media) async {
    final bool? result =
        await methodChannel.invokeMethod<bool>('play_movie', media.toJson());
    return result;
  }

  @override
  Future<bool?> config() async {
    final bool? result =
        await methodChannel.invokeMethod<bool>('config_downloader');
    return result;
  }

  @override
  Future<dynamic> getDownloadsList() async {
    final dynamic result =
        await methodChannel.invokeMethod<dynamic>('get_downloads_list');
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
  Future<dynamic> cancelDownload(String mediaId, String mediaType) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>(
        'cancel_download', {"mediaId": mediaId, "mediaType": mediaType});
    return result;
  }

}
