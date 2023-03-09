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
    config();
  }

  config() async {
    bool result;
    try {
      result = await _dowplayPlugin.config() ?? false;
      if (kDebugMode) {
        print("result : $result");
      }
    } on PlatformException {
      result = false;
    }
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
        token: "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxMSIsImp0aSI6IjE2ZmI2MGUxYjJkMjM1YzQ5Mzk0MGZmYmU5MmE3ZTQ4MmY3OGVhZDRmZmQ3NTA3MjM5NDdmODRiNDJiMzQ4NDBjYzM5YTg2MGFmNThhM2ExIiwiaWF0IjoxNjc4Mzk1OTE5LjQ4NzQzMywibmJmIjoxNjc4Mzk1OTE5LjQ4NzQzNSwiZXhwIjoxNzEwMDE4MzE5LjQ4MzIwNiwic3ViIjoiNjY3NTQiLCJzY29wZXMiOltdfQ.Ytw0TddQJSOSSOUsVZiLuxgWoFVpdd5KjTpMDryh3t45QVosjI6oAxhvQ0Sn7IbqTJzfP_PfcO5kRzfIF-i6OguWWsAGvWt54yyKlZc-qV4ZtMQURL557RCgYu_cfQr4fbZOoTM-tDNrAUAAPQWYC6VsCU3KR0QekhUISSXbpAN2ztZVc8Tn0o3HDlC6vOLBphwSUtAT09Uury9nCrODhoK-13LUyn8RkPMlrGaS7j20ah9BTY1RYRTf5ysdqaLXtB-enjnIocwlRmLi7vT1_hLdsI0y-q9MJL3ATf041UpA3CpDNmh_Z_-7G703Eh-JG7tM-8bfUAu9Jrpyq5Cbqy5ETnlidSIRJRYHvKPdSNtQcDtt3Z-sIKoOiKO4Bws85fXeJvjtU7jcDbw_m55lyVlEKTtDGzcOl3pCkdVuJBg_k8e9SRtRmSOkOnyNK3m2_38rgKj61hq-5_2nCK8TRZbvD7FsXixcNeKOrR1QNMmiBofn4QPnzfosmXN8qp-xP-JtfRFoIcKWCJgSvOtVU3kw2uvXhVugPn5RFNMU2nuBa4f_wGfmOn76nJ3IpgO3XXXMUtYeMPR6K519RJ4ULVLEymIQgBOA7an-GgF_ITvO8Fmd1zuLYJ1kTQfa8lGxWsIr1mMM-POgfSivBW9njhcvfRMIGj_6JfPV8BgJM3o",
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
