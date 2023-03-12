import 'dart:convert';

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

  Future<void> playEpisode() async {
    Map<String, dynamic> episodeObject = {
      "id": 64864,
      "title": "Episode 2",
      "order": "2",
      "poster_photo":
          "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
      "duration": "10 min",
      "download_url":
          "https://s-ed1.cloud.gcore.lu/video/m-159n/English/Animation&Family/Barbie.Dreamhouse.Adventures/S01/08.mp4",
      "hd_url":
          "https://site.gcdn.co/video/m-159n/English/Animation&Family/Barbie.Dreamhouse.Adventures/S01/08.mp4?md5=L77x49ZfcsyykmBfTIgcIg&expires=1678551514",
      "trailer_url": null,
      "media_url":
          "https://site.gcdn.co/video/m-159n/English/Animation&Family/Barbie.Dreamhouse.Adventures/S01/08.mp4?md5=L77x49ZfcsyykmBfTIgcIg&expires=1678551514",
      "created_at": "2020-07-01 13:27:14",
      "release_date": "2020-07-01 00:00:00",
      "watching": null
    };

    Map<String, dynamic> itemIds = {
      "season_id": "3236",
      "tv_show_id": "1532",
      "episode_id": "64864",
    };

    Map<String, dynamic> mediaGroup = {
      "tv_show": {
        "id": 1532,
        "title": "Tom and Jerry",
        "description":
            "المسلسل الكارتونى المحبب للجميع و اشهر المعارك بين القط و الفار",
        "trailer_url": null,
        "language": "English",
        "translation": "-",
        "start_year": "1967",
        "end_year": "1967",
        "cover_photo": null,
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/posters/2020-07-01/UJP1JIdmnijsHDX2.jpg",
        "seasons": [
          {
            "id": 3236,
            "season_id": 3747,
            "poster_photo":
                "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
            "cover_photo": null,
            "trailer_url": null,
            "season_number": "1",
            "title": "Tom and Jerry S01"
          }
        ],
        "tags": [
          {"id": 57, "title": "Kids"},
          {"id": 28, "title": "Animation"},
          {"id": 64, "title": "English"}
        ],
        "actors": [],
        "director_info": null,
        "is_favourite": false,
        "imdb_rating": null,
        "imdb_certificate": null,
        "last_watching": null,
        "last_watching_season_id": null
      },
      "season": {
        "id": 3236,
        "season_id": 3747,
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
        "cover_photo": null,
        "trailer_url": null,
        "season_number": "1",
        "title": "Tom and Jerry S01"
      },
      "items_ids": itemIds
    };

    Media media = Media(
        title: "Tom and Jerry",
        subTitle: "Episode 2",
        url:
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        mediaId: "64864",
        mediaType: "series",
        userId: "77810",
        profileId: "741029",
        token:
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxMSIsImp0aSI6IjVkM2FjZWMzNTM3YTBlNGExYTBjMDZjMGVmNDM2NTc3ZThlYjRjNjNiMjNhZDdiNGE0NjEzMTRlN2IwMDc4NWIxYTZkYTJlMmRjN2UzYWQ2IiwiaWF0IjoxNjc4NTM2NjEwLjc0NzQyNCwibmJmIjoxNjc4NTM2NjEwLjc0NzQyOCwiZXhwIjoxNzEwMTU5MDEwLjc0MDg1Mywic3ViIjoiNzc4MTAiLCJzY29wZXMiOltdfQ.AljaU6eJVwYzhauYtWwt-GjrSxTtbWn37z8KqQF1dMAXuTyCba9GqWP0dQxugTtANARhZHkunq1lcj9GdsLQciX2wr7pPDssAkX9f--CqCNN7XH491btYy5WU2ug_AtCXp47KGT9btZHZjPuYN99fXDpEuf94FtaWouKJIvNmvRwBOIzS50SRmgPHRc34MeDIzvq6hVNAB3cziok4GHAURF7BKjAgIhpcCHKefdIqdvyOy8wbcKxL2BKbiZ7wqvQSJowceyg86MySOqRDNiSvTq91ALC2rsF45jGwuHokinn806AavVngQe91i6ID_9oDeTNKmo2KUlDf9B5sWGZ3QRtM5v7qZspQGv-xLjrjf_ka0WrtQKlU7cD2fgu7mhelhG-1JA4hfJPos3SIusr6mD_x5NybDovjYH141zQJJGuYxWGLLcoiMKor4821uSDWh613w0fRWyJEadOmjEnRE2Wz52Y0BTqv7--LCeGe9nqMvi28H_6gJoi2ejLmdgnWUm64Z-YIQOj-bNJglqLJjkSs64nMAN0hStPSItqX7Qej3B6gADg573HobPu6jRdUtAHs1GXHSW-1yuxAmHrso3myFocSRYDmVPqRpTFwbbrFQ9kGOpl7dLwXrvOtAAUMNfoIcdYoVTbZB0VQXtLfpEOR_7Mi0XLD7xaW78nSRU",
        apiBaseUrl: "https://v4.kaayapp.com/api/mobile/v4",
        lang: "ar",
        startAt: 10.5,
        info: episodeObject,
        mediaGroup: mediaGroup);
    bool result = await invokePlayEpisode(media);
  }
  Future<void> playMovie() async {
    Map<String, dynamic> movieObject = {
      "id": 377530,
      "title": "The Simpsons in Plusaversary",
      "description": "يتبع The Simpsons أثناء استضافتهم لحفلة Disney Day مع الأصدقاء من جميع أنحاء الخدمة ، وكل شخص موجود في القائمة - باستثناء Homer\r\n\r\n\r\nFollows The Simpsons\" as they host a Disney+ Day party with friends from across the service, everyone is on the list - except Homer.",
      "download_url": "https://s-ed1.cloud.gcore.lu/video/m-159n/English/Animation&Family/Klaus.2019.720p.mp4",
      "hd_url": "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.720p.mp4?md5=ipbkWahE7MGcaEAdHHRS8g&expires=1678668998",
      "trailer_url": null,
      "media_url": "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080p.mp4?md5=shMoxWUw5sX8KK6q-QH16w&expires=1678668998",
      "duration": "5m",
      "language": "English",
      "translation": "العربية",
      "year": "2021",
      "cover_photo": "https://thekee-m.gcdn.co/images06012022/uploads/media/movies/covers/2022-10-16/w0wGiEtcApEObeNk.png",
      "poster_photo": "https://thekee-m.gcdn.co/images06012022/uploads/media/movies/posters/2021-11-13/cOKqU1ftE6HQbxv6.jpg",
      "tags": [
        {
          "id": 57,
          "title": "Kids"
        },
        {
          "id": 2,
          "title": "Comedy"
        },
        {
          "id": 28,
          "title": "Animation"
        },
        {
          "id": 64,
          "title": "English"
        }
      ],
      "actors": [
        {
          "id": 6019,
          "name": "Dan Castellaneta",
          "image": "https://thekee-m.gcdn.co/images06012022/uploads/actors/6019/818ABOIVFVFMolTI.png"
        },
        {
          "id": 25326,
          "name": "Yeardley Smith",
          "image": "https://thekee-m.gcdn.co/images06012022/uploads/actors/2021-11-13/X477M3hjEfXSv7Um.png"
        },
        {
          "id": 10955,
          "name": "Nancy Cartwright",
          "image": "https://thekee-m.gcdn.co/images06012022/uploads/actors/10955/kNrav6MXJYTdvQbg.png"
        },
        {
          "id": 13727,
          "name": "Hank Azaria",
          "image": "https://thekee-m.gcdn.co/images06012022/uploads/actors/13727/ae18InPvKS9MXDLC.png"
        },
        {
          "id": 25327,
          "name": "Tress MacNeille",
          "image": "https://thekee-m.gcdn.co/images06012022/uploads/actors/2021-11-13/47A9foEusHXvvWv2.png"
        }
      ],
      "director_info": {
        "id": 6593,
        "name": "David Silverman",
        "image": "https://thekee-m.gcdn.co/images06012022/uploads/directors/2022-10-16/z16camhTHPAt1JMh.png"
      },
      "is_favourite": false,
      "imdb_rating": "5.6",
      "imdb_certificate": null,
      "watching": null
    };

    Media media = Media(
        title: "The Simpsons in Plusaversary",
        subTitle: "",
        url: "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4?md5=QA-5PWsq9OIEaa0EM79p9A&expires=1678670074",
        mediaId: "377530",
        mediaType: "movie",
        userId: "77810",
        profileId: "741029",
        token:
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxMSIsImp0aSI6IjA2NTM3YjhlMjk5ZjJiYzhhOTEyMmI2ODA3OGEzYjJhNzgzNTBlZTg4ZTBjZWM5NjFmYmRhNDhiNzQ3ZGFmY2RkNGJmM2MwNjcwNmRhYjkwIiwiaWF0IjoxNjc4NjU0NDgyLjQxOTc0MiwibmJmIjoxNjc4NjU0NDgyLjQxOTc0NiwiZXhwIjoxNzEwMjc2ODgyLjQxMzUxNywic3ViIjoiNzc4MTAiLCJzY29wZXMiOltdfQ.XBS8ZLTeM3kvIAoGhOdTVPHR1sloOPvSsF7Izf27yZc7Ge3T42imnpmvGLaqffXIxKV_CW5-AbXgAoJOb91p76BRNUw48xw3NMdw98SzmgzBnIEOfHU3J0EVeK7e5202w75POmcaQhZ4U7D_aJmlnaSZqG8B_mDI8sQb9NWeZTaUHX41s8RdnfVQjI6-gm0Y4MfpZ776e4y78t259W4pQ0eumly0Rlt6UlKuK8WQLBY_JqGrdt1ssAZx8lypoZXbbwGjdHIf2oal1RNCMeHIHXLAHGFErmDCjkHJcFHU-XpuGfZ-oLFHpgrkXxdQi5G7rbKkGKsBW9TUZQKVSHU8SeIqaKnk8MA3-JNnaTcKb3wxItxXM07Y7oznKg9hzgGN_42yCMLEstG6aN7Z2UBsqNAtXseHVJ1Idms5WDeqxSQEiZDkR6Btnr7L012SotiHWP26d1Oa4GnYK0XyzwkNFvNg9_lcTfaUbCLuBtzy3nkREWDYBOR9CxFzgWHv-N0lJOacfZdrmrbQgIZCp02e-sROGNsDUl8oOi4vH9TjNRfgvlNw4OVPbKYNna9DgRVz2MtdVjmU0adXMi9Niwoztj0whTwxeiLYDn-_mC044x9RF3Ugw-EL_DjaYzRuFy3FgznW7Jq7B2vZbOVVeWtqQ83h45X_izQwxTIQP8Ddy0g",
        apiBaseUrl: "https://v4.kaayapp.com/api/mobile/v4",
        lang: "ar",
        startAt: 3,
        info: movieObject,
        mediaGroup: null);
    bool result = await invokePlayMovie(media);
  }

  Future<bool> invokePlayEpisode(Media media) async {
    printWrapped("playEpisode : ${media.toJson()}");
    bool result;
    try {
      result = await _dowplayPlugin.playEpisode(media) ?? false;
      if (kDebugMode) {
        print("result : $result");
      }
    } on PlatformException {
      result = false;
    }
    return result;
  }
  Future<bool> invokePlayMovie(Media media) async {
    printWrapped("playMovie : ${media.toJson()}");
    bool result;
    try {
      result = await _dowplayPlugin.playMovie(media) ?? false;
      if (kDebugMode) {
        print("result : $result");
      }
    } on PlatformException {
      result = false;
    }
    return result;
  }

  void printWrapped(String text) {
    final pattern = RegExp('.{1,800}'); // 800 is the size of each chunk
    pattern.allMatches(text).forEach((match) => print(match.group(0)));
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              ElevatedButton(
                onPressed: playEpisode,
                child: const Text("Play Episode"),
              ),
              ElevatedButton(
                onPressed: playMovie,
                child: const Text("Play Movie"),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
