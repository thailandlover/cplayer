import 'package:flutter_test/flutter_test.dart';
import 'package:dowplay/dowplay.dart';
import 'package:dowplay/dowplay_platform_interface.dart';
import 'package:dowplay/dowplay_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDowplayPlatform
    with MockPlatformInterfaceMixin
    implements DowplayPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final DowplayPlatform initialPlatform = DowplayPlatform.instance;

  test('$MethodChannelDowplay is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelDowplay>());
  });

  test('getPlatformVersion', () async {
    Dowplay dowplayPlugin = Dowplay();
    MockDowplayPlatform fakePlatform = MockDowplayPlatform();
    DowplayPlatform.instance = fakePlatform;

    expect(await dowplayPlugin.getPlatformVersion(), '42');
  });
}
