
import 'Media.dart';
import 'dowplay_platform_interface.dart';

class Dowplay {
  Future<bool?> play(Media media) {
    return DowplayPlatform.instance.play(media);
  }
}
