import 'package:dowplay/Media.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:dowplay/dowplay.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final _dowplayPlugin = Dowplay();

  @override
  void initState() {
    super.initState();
  }

  Future<void> play() async {
    bool result;
    Media media = Media(
        title: "Klaus",
        subTitle: "Subtitle",
        url:
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080pAr.mp4",
        mediaId: "20132",
        mediaType: "movie",
        userId: "66754",
        profileId: "84861",
        apiBaseUrl: "https://v4.kaayapp.com/api/mobile/v4",
        lang: "ar",
        startAt: 10.5);
    try {
      result = await _dowplayPlugin.play(media) ?? false;
      if (kDebugMode) {
        print("result : $result");
      }
    } on PlatformException {
      result = false;
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: ElevatedButton(
            onPressed: play,
            child: const Text("Play"),
          ),
        ),
      ),
    );
  }
}
