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
    final bool? result = await methodChannel.invokeMethod<bool>('play_episode',media.toJson());
    return result;
  }
  Future<bool?> playMovie(MovieMedia media) async {
    final bool? result = await methodChannel.invokeMethod<bool>('play_movie',media.toJson());
    return result;
  }

  @override
  Future<bool?> config() async {
    final bool? result = await methodChannel.invokeMethod<bool>('config_downloader');
    return result;
  }

  @override
  Future<dynamic> getDownloadsList() async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>('get_downloads_list');
    return result;
  }

  @override
  Future<dynamic> startDownload(dynamic item) async {
    final dynamic result = await methodChannel.invokeMethod<dynamic>('start_download',item);
    return result;
  }
}
