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

  Future<bool?> config(Map<String,dynamic> data) {
    return DowplayPlatform.instance.config(data);
  }

  Future<dynamic> getDownloadsList() {
    return DowplayPlatform.instance.getDownloadsList();
  }

  Future<dynamic> getTvShowSeasonsDownloadList(Map<String,dynamic> data) {
    return DowplayPlatform.instance.getTvShowSeasonsDownloadList(data);
  }
  Future<dynamic> getSeasonEpisodesDownloadList(Map<String,dynamic> data) {
    return DowplayPlatform.instance.getSeasonEpisodesDownloadList(data);
  }

  Future<dynamic> startDownloadMovie(dynamic item) {
    return DowplayPlatform.instance.startDownloadMovie(item);
  }

  Future<dynamic> startDownloadEpisode(dynamic item) {
    return DowplayPlatform.instance.startDownloadEpisode(item);
  }

  Future<dynamic> pauseDownload(String mediaId, String mediaType) {
    return DowplayPlatform.instance.pauseDownload(mediaId, mediaType);
  }

  Future<dynamic> resumeDownload(String mediaId, String mediaType) {
    return DowplayPlatform.instance.resumeDownload(mediaId, mediaType);
  }

  Future<dynamic> cancelDownload(String mediaId, String mediaType) {
    return DowplayPlatform.instance.cancelDownload(mediaId, mediaType);
  }
}
