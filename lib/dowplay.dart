
import 'dowplay_platform_interface.dart';

class Dowplay {
  Future<String?> getPlatformVersion() {
    return DowplayPlatform.instance.getPlatformVersion();
  }
}
