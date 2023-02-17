import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:dowplay/dowplay_method_channel.dart';

void main() {
  MethodChannelDowplay platform = MethodChannelDowplay();
  const MethodChannel channel = MethodChannel('dowplay');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
