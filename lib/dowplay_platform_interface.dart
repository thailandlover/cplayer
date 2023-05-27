import 'package:dowplay/EpisodeMedia.dart';
import 'package:dowplay/MovieMedia.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'dowplay_method_channel.dart';

abstract class DowplayPlatform extends PlatformInterface {
  /// Constructs a DowplayPlatform.
  DowplayPlatform() : super(token: _token);

  static final Object _token = Object();

  static DowplayPlatform _instance = MethodChannelDowplay();

  /// The default instance of [DowplayPlatform] to use.
  ///
  /// Defaults to [MethodChannelDowplay].
  static DowplayPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DowplayPlatform] when
  /// they register themselves.
  static set instance(DowplayPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<dynamic> playEpisode(EpisodeMedia media) {
    throw UnimplementedError('playEpisode() has not been implemented.');
  }

  Future<dynamic> playMovie(MovieMedia media) {
    throw UnimplementedError('playMovie() has not been implemented.');
  }

  Future<bool?> config(Map<String, dynamic> data) {
    throw UnimplementedError('config() has not been implemented.');
  }

  Future<dynamic> getDownloadsList() {
    throw UnimplementedError('get_downloads_list() has not been implemented.');
  }

  Future<dynamic> getTvShowSeasonsDownloadList(Map<String, dynamic> data) {
    throw UnimplementedError(
        'tvshow_seasons_downloads_list() has not been implemented.');
  }

  Future<dynamic> getSeasonEpisodesDownloadList(Map<String, dynamic> data) {
    throw UnimplementedError(
        'season_episodes_downloads_list() has not been implemented.');
  }

  Future<dynamic> startDownloadMovie(dynamic item) {
    throw UnimplementedError(
        'start_download_movie() has not been implemented.');
  }

  Future<dynamic> startDownloadEpisode(dynamic item) {
    throw UnimplementedError(
        'start_download_episode() has not been implemented.');
  }

  Future<dynamic> pauseDownload(String mediaId, String mediaType) {
    throw UnimplementedError('pause_download() has not been implemented.');
  }

  Future<dynamic> resumeDownload(String mediaId, String mediaType) {
    throw UnimplementedError('resume_download() has not been implemented.');
  }

  Future<dynamic> cancelDownload(
      String mediaId, String mediaType, String tvShowId, String seasonId) {
    throw UnimplementedError('cancel_download() has not been implemented.');
  }

  Future<dynamic> getDownloadMovie(String mediaId) {
    throw UnimplementedError('get_download_movie() has not been implemented.');
  }

  Future<dynamic> getDownloadEpisode(
      String mediaId, String tvShowId, String seasonId) {
    throw UnimplementedError(
        'get_download_episode() has not been implemented.');
  }
}
