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

  Future<bool?> playEpisode(EpisodeMedia media) {
    throw UnimplementedError('playEpisode() has not been implemented.');
  }
  Future<bool?> playMovie(MovieMedia media) {
    throw UnimplementedError('playMovie() has not been implemented.');
  }

  Future<bool?> config() {
    throw UnimplementedError('config() has not been implemented.');
  }

  Future<dynamic> getDownloadsList() {
    throw UnimplementedError('get_downloads_list() has not been implemented.');
  }
}
