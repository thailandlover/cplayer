import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'dowplay_platform_interface.dart';

/// An implementation of [DowplayPlatform] that uses method channels.
class MethodChannelDowplay extends DowplayPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dowplay');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
