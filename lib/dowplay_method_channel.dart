import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'Media.dart';
import 'dowplay_platform_interface.dart';

/// An implementation of [DowplayPlatform] that uses method channels.
class MethodChannelDowplay extends DowplayPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('dowplay');

  @override
  Future<bool?> play(Media media) async {
    final bool? result = await methodChannel.invokeMethod<bool>('play',media.toJson());
    return result;
  }

  @override
  Future<bool?> config() async {
    final bool? result = await methodChannel.invokeMethod<bool>('config_downloader');
    return result;
  }
}
