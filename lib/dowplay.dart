import 'package:dowplay/EpisodeMedia.dart';
import 'package:dowplay/MovieMedia.dart';

import 'dowplay_platform_interface.dart';

class Dowplay {
  Future<bool?> playEpisode(EpisodeMedia media) {
    return DowplayPlatform.instance.playEpisode(media);
  }

  Future<bool?> playMovie(MovieMedia media) {
    return DowplayPlatform.instance.playMovie(media);
  }

  Future<bool?> config() {
    return DowplayPlatform.instance.config();
  }

  Future<dynamic> getDownloadsList() {
    return DowplayPlatform.instance.getDownloadsList();
  }

  Future<dynamic> startDownloadMovie(dynamic item) {
    return DowplayPlatform.instance.startDownloadMovie(item);
  }

  Future<dynamic> startDownloadEpisode(dynamic item) {
    return DowplayPlatform.instance.startDownloadEpisode(item);
  }
}
