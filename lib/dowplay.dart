
import 'package:dowplay/EpisodeMedia.dart';

import 'Media.dart';
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
}
