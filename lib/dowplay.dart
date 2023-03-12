
import 'Media.dart';
import 'dowplay_platform_interface.dart';

class Dowplay {
  Future<bool?> playEpisode(Media media) {
    return DowplayPlatform.instance.playEpisode(media);
  }

  Future<bool?> playMovie(Media media) {
    return DowplayPlatform.instance.playMovie(media);
  }

  Future<bool?> config() {
    return DowplayPlatform.instance.config();
  }
}
