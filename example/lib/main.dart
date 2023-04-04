import 'dart:convert';

import 'package:dowplay/EpisodeMedia.dart';
import 'package:dowplay/MovieMedia.dart';
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

  final String accessToken =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxMSIsImp0aSI6ImNjMjFiNzE3MDFlZTMxNzAxYzVmZTc5ZjIzM2I4YzcxNzMzOTM0MGNmZDMwNTZhMWRhYjZlMmVhOGE1ZmZjMDk3MDExMjE5N2QzYmUyYzgxIiwiaWF0IjoxNjgwMjk0MTM3LjQ0NjY2NiwibmJmIjoxNjgwMjk0MTM3LjQ0NjY2OSwiZXhwIjoxNzExOTE2NTM3LjQzOTkwNywic3ViIjoiNzc4MTAiLCJzY29wZXMiOltdfQ.JCuscBuKiI7ZCnrcQhvVDj5heEYMbA3UQe4s322bt8zAwLh6hZBvpRUlvNoEmcmmw2GSRhBYHeC6JzvklHwxz6UO2eFBtJeGHWyAic4DC0T6FEYrYQcuZnExyZDhGco29QPED1_BHy8bVxB-yKZyCrgYoJzwik8IZ1qN8W0TdVmbsGXBRvR0CU9cq0q44tNljxhxcmw2fHzMdUCggfvKGBW8aC9ylXK3UoG4yAHzGp2Ug7K_QCUDG2yRW9aUEDP8YJte3PMZDg1yj99JyN20coiyyrAGqoyF-x76dIGqjnRug3X58gGIhWfy4BlX8VcJoCcnreoZqlXNrd0V7r1vIAL2kNSo14wfQH0lGDXz1hzY9blf8OVXIjoyseMdQTrXFkekvcfEPD1GoH6V4-kOKu7TJMpIf13FjdX4zp8xJqSfjfhCP5pE_qAO4-5TuOZawP-J1Pzz882xfhMCfZQVtG1r7iamLIh14A56SWmilOPs4mdoPpuVVybR7X89gEn77odMV7ivIa-WI6mevbo4P4yQj-1LZD8NY1rgW1-5W5Ak3_Myy8Wpg1QzDWNnd7XYJVzA4bTpoQCcQL-yjMiuvu3H0D3Xrp13-nZWGL1oHazEZvUk9R97nO7WfWxezam3kd5YEf_OxTQ5kBbvcM9RQRuYnmK3LXSlTecOYO0F5FU";
  late Map<String, dynamic> movieObject;
  late MovieMedia movieMedia;

  List _downloadsList = [];

  @override
  void initState() {
    super.initState();
    movieObject = {
      "id": 377530,
      "title": "The Simpsons in Plusaversary",
      "description":
          "يتبع The Simpsons أثناء استضافتهم لحفلة Disney Day مع الأصدقاء من جميع أنحاء الخدمة ، وكل شخص موجود في القائمة - باستثناء Homer\r\n\r\n\r\nFollows The Simpsons\" as they host a Disney+ Day party with friends from across the service, everyone is on the list - except Homer.",
      "download_url":
          "https://s-ed1.cloud.gcore.lu/video/m-159n/English/Animation&Family/Klaus.2019.720p.mp4",
      "hd_url":
          "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.720p.mp4?md5=ipbkWahE7MGcaEAdHHRS8g&expires=1678668998",
      "trailer_url": null,
      "media_url":
          "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080p.mp4?md5=shMoxWUw5sX8KK6q-QH16w&expires=1678668998",
      "duration": "5m",
      "language": "English",
      "translation": "العربية",
      "year": "2021",
      "cover_photo":
          "https://thekee-m.gcdn.co/images06012022/uploads/media/movies/covers/2022-10-16/w0wGiEtcApEObeNk.png",
      "poster_photo":
          "https://thekee-m.gcdn.co/images06012022/uploads/media/movies/posters/2021-11-13/cOKqU1ftE6HQbxv6.jpg",
      "tags": [
        {"id": 57, "title": "Kids"},
        {"id": 2, "title": "Comedy"},
        {"id": 28, "title": "Animation"},
        {"id": 64, "title": "English"}
      ],
      "actors": [
        {
          "id": 6019,
          "name": "Dan Castellaneta",
          "image":
              "https://thekee-m.gcdn.co/images06012022/uploads/actors/6019/818ABOIVFVFMolTI.png"
        },
        {
          "id": 25326,
          "name": "Yeardley Smith",
          "image":
              "https://thekee-m.gcdn.co/images06012022/uploads/actors/2021-11-13/X477M3hjEfXSv7Um.png"
        },
        {
          "id": 10955,
          "name": "Nancy Cartwright",
          "image":
              "https://thekee-m.gcdn.co/images06012022/uploads/actors/10955/kNrav6MXJYTdvQbg.png"
        },
        {
          "id": 13727,
          "name": "Hank Azaria",
          "image":
              "https://thekee-m.gcdn.co/images06012022/uploads/actors/13727/ae18InPvKS9MXDLC.png"
        },
        {
          "id": 25327,
          "name": "Tress MacNeille",
          "image":
              "https://thekee-m.gcdn.co/images06012022/uploads/actors/2021-11-13/47A9foEusHXvvWv2.png"
        }
      ],
      "director_info": {
        "id": 6593,
        "name": "David Silverman",
        "image":
            "https://thekee-m.gcdn.co/images06012022/uploads/directors/2022-10-16/z16camhTHPAt1JMh.png"
      },
      "is_favourite": false,
      "imdb_rating": "5.6",
      "imdb_certificate": null,
      "watching": {
        "current_time": "3121",
        "duration": "7576",
        "title": "ملاذكرد 1071 - Malazgirt 1071",
        "order": 0,
        "description":
            "في العام 1071، يلتقي الجيشان السلجوقي والبيزنطي في ميدان المعركة ليخوضا مواجهة دامية في هذه اللحظة المفصلية من تاريخ الإمبراطورية البيزنطية.\r\n\r\nThe story of the war, which is the beginning of the history of Turks in Anatolia.",
        "last_media_id": 380988,
        "1080_url":
            "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.1080p.mp4?md5=Jr7WQNNScUtlCM1AcjCN2Q&expires=1678754308",
        "720_url":
            "https://site.gcdn.co/video/m-159n/English/Animation&Family/Klaus.2019.720p.mp4?md5=JSkSnlOwvrIqbPwlC8vekA&expires=1678754308",
        "continue_type": null,
        "next_type": null,
        "next_episode": null
      }
    };
    movieMedia = MovieMedia(
        title: "The Simpsons in Plusaversary",
        subTitle: "",
        url:
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/The.Simpsons.in.Plusaversary.2021.1080.mp4?md5=QA-5PWsq9OIEaa0EM79p9A&expires=1678670074",
        mediaId: "377530",
        mediaType: "movie",
        userId: "77810",
        profileId: "741029",
        token:
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxMSIsImp0aSI6Ijk4MGJhODk4NmYyYzU1YWJiNzg1MWEwYzMwM2U1ZGEzMjI3YmQ0YWM5M2VjNGM5YjQ1NzM5NzhhNDUyMTYzMDdiYTAxZWU4M2Y0Y2Q4ZDBiIiwiaWF0IjoxNjc4NzM5NzAyLjQ0NDUzMiwibmJmIjoxNjc4NzM5NzAyLjQ0NDUzNSwiZXhwIjoxNzEwMzYyMTAyLjQzNzA2NCwic3ViIjoiNzc4MTAiLCJzY29wZXMiOltdfQ.mG9mYXSRv9JunSEFRfhk-RwvtI4KRhjB7yD3vuDWOqgW3I9mZuTfl-93Fsh8pFSMcRmYyknm9tK4npQKj436Q1WD-cRFy30qpsywCVB3zK5-B6kU4ljIMSXLHiAXiXD40mlrOdi2W38_EKB8ziC_4uu7xb-SWeqypmlFuUBHiNl5LX3N4L8YN_ujaKmXUlLw49n_iTwyY1OnU9KhEv1HJqe0LTWEhh48OZ1YWHF74G1n3pGDkZqWj0kWs3jNAmTUh6pTsXSbiAvWmYqe4zvBQZCpNjEx6MB4LPbnChtWFdmJ1UhOePo-S_j5K1OQkn_uzzzffUv4dK3VxstQBpGbBvPqZq0dZUzY_8Jj1d8e0kORV5U0McO-ED8lybW6M1rbjL0K3OvKOvOJLQynIKFPMOGvJI2zIdQHeEJxA2omgn3zqFcYJG-jVNlQb0wqy9HPkCDizW38dbL_wMXXTiljk3WDqgFOIa_sm-v3lW474_0zZw3zDjUts1LT3Xw6kn1IOfzFNBt54P0Omx9T3tXFgCmGj2cvfw3BK6j00soPpvD_HTkyo4-1IjbqeP4tsVgX-HgNpvN-1yEraYYNT236fmGAv9X-Or-_KS76PM555JWM2FVs4vgyzyJXvMr_ue8mOMBighPerO6jVucCcmtu7Il6CraPl7vpGdSEDbdohxY",
        apiBaseUrl: "https://v4.kaayapp.com/api",
        lang: "ar",
        startAt: 3,
        info: movieObject);

    // Should be called on the app start
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
    // Will be Episode Item model which you need to start
    Map<String, dynamic> episodeToStart = {
      "id": 64864,
      "title": "Episode 2",
      "order": "2",
      "poster_photo":
          "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
      "duration": "10 min",
      "download_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "hd_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "trailer_url": null,
      "media_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "created_at": "2020-07-01 13:27:14",
      "release_date": "2020-07-01 00:00:00",
      "watching": null
    };

    List<Map<String, dynamic>> seasonEpisodes = [
      {
        "id": 64863,
        "title": "Episode 1",
        "order": "1",
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
        "duration": "10 min",
        "download_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/01.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "hd_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/01.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "trailer_url": null,
        "media_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/01.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "created_at": "2020-07-01 13:27:14",
        "release_date": "2020-07-01 00:00:00",
        "watching": {"current_time": "13", "duration": "1380"}
      },
      {
        "id": 64864,
        "title": "Episode 2",
        "order": "2",
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
        "duration": "10 min",
        "download_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "hd_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "trailer_url": null,
        "media_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "created_at": "2020-07-01 13:27:14",
        "release_date": "2020-07-01 00:00:00",
        "watching": {"current_time": "25", "duration": "60"}
      },
      {
        "id": 64865,
        "title": "Episode 3",
        "order": "3",
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
        "duration": "10 min",
        "download_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/03.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "hd_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/03.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "trailer_url": null,
        "media_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/03.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "created_at": "2020-07-01 13:27:14",
        "release_date": "2020-07-01 00:00:00",
        "watching": null
      },
      {
        "id": 64866,
        "title": "Episode 4",
        "order": "4",
        "poster_photo":
            "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
        "duration": "10 min",
        "download_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/04.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "hd_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/04.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "trailer_url": null,
        "media_url":
            "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/04.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
        "created_at": "2020-07-01 13:27:14",
        "release_date": "2020-07-01 00:00:00",
        "watching": null
      }
    ];

    Map<String, dynamic> itemIds = {
      "season_id": "3236",
      "tv_show_id": "1532",
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
      "items_ids": itemIds,
      "episodes": seasonEpisodes
    };

    EpisodeMedia media = EpisodeMedia(
        mediaType: "series",
        userId: "77810",
        profileId: "741029",
        token: accessToken,
        apiBaseUrl: "https://v4.kaayapp.com/api",
        lang: "ar",
        info: episodeToStart,
        mediaGroup: mediaGroup);
    bool result = await invokePlayEpisode(media);
    if (kDebugMode) {
      printWrapped("Play episode result : $result");
    }
  }

  Future<void> playMovie() async {
    bool result = await invokePlayMovie(movieMedia);
    if (kDebugMode) {
      printWrapped("Play movie result : $result");
    }
  }

  Future<dynamic> getDownloadsList() async {
    dynamic result = await invokeGetDownloadsList();
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads[1]['object']['info'])}");
    }
  }

  Future<dynamic> startDownloadMovie() async {
    String type = "movie";
    Map<String, dynamic> item = {
      "type": type,
      "media": movieMedia,
    };
    dynamic result = await invokeStartDownloadMovie(item);
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads)}");
    }
  }

  Future<dynamic> startDownloadEpisode() async {
    String type = "series";
    // Will be Episode Item model which you need to download
    Map<String, dynamic> episodeToDownload = {
      "id": 64864,
      "title": "Episode 2",
      "order": "2",
      "poster_photo":
          "https://thekee-m.gcdn.co/images06012022/uploads/media/series/seasons/posters/2020-07-01/ZMx47Bf3sO03FprZ.jpg",
      "duration": "10 min",
      "download_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "hd_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "trailer_url": null,
      "media_url":
          "https://thekee.gcdn.co/video/m-159n/English/Animation&Family/Tom.and.Jerry.1965/02.mp4?md5=eCp0VmIS_doipZ6lGVxwVg&expires=1678550892",
      "created_at": "2020-07-01 13:27:14",
      "release_date": "2020-07-01 00:00:00",
      "watching": null
    };

    Map<String, dynamic> itemIds = {
      "season_id": "3236",
      "tv_show_id": "1532",
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
      "items_ids": itemIds,
    };

    EpisodeMedia media = EpisodeMedia(
        mediaType: "series",
        userId: "77810",
        profileId: "741029",
        token: accessToken,
        apiBaseUrl: "https://v4.kaayapp.com/api",
        lang: "ar",
        info: episodeToDownload,
        mediaGroup: mediaGroup);

    Map<String, dynamic> item = {
      "type": type,
      "media": media,
    };
    dynamic result = await invokeStartDownloadEpisode(item);
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads)}");
    }
  }

  Future<dynamic> pauseDownload() async {
    dynamic result =
        await invokePauseDownload(movieMedia.mediaId, movieMedia.mediaType);
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads)}");
    }
  }

  Future<dynamic> resumeDownload() async {
    dynamic result =
        await invokeResumeDownload(movieMedia.mediaId, movieMedia.mediaType);
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads)}");
    }
  }

  Future<dynamic> cancelDownload() async {
    dynamic result =
        await invokeCancelDownload(movieMedia.mediaId, movieMedia.mediaType);
    if (kDebugMode) {
      List<dynamic> downloads = List.from(result as Iterable);
      if (mounted) {
        setState(() {
          _downloadsList = downloads;
        });
      }
      // printWrapped(
      //     "Downloads list [${downloads.length}] : ${jsonEncode(downloads)}");
    }
  }

  Future<bool> invokePlayEpisode(EpisodeMedia media) async {
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

  Future<bool> invokePlayMovie(MovieMedia media) async {
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

  Future<dynamic> invokeGetDownloadsList() async {
    dynamic result;
    try {
      result = await _dowplayPlugin.getDownloadsList();
    } on PlatformException {
      result = false;
    }
    return result;
  }

  Future<dynamic> invokeStartDownloadMovie(dynamic item) async {
    dynamic result;
    if (item['type'] == "movie" && item['media'] is MovieMedia) {
      item['media'] = (item['media'] as MovieMedia).toJson();
      try {
        result = await _dowplayPlugin.startDownloadMovie(item);
      } on PlatformException {
        result = false;
      }
    } else {
      if (kDebugMode) {
        print("Invalid item type");
      }
      result = false;
    }
    return result;
  }

  Future<dynamic> invokeStartDownloadEpisode(dynamic item) async {
    dynamic result;
    if (item['type'] == "series" && item['media'] is EpisodeMedia) {
      item['media'] = (item['media'] as EpisodeMedia).toJson();
      try {
        result = await _dowplayPlugin.startDownloadEpisode(item);
      } on PlatformException {
        result = false;
      }
    } else {
      if (kDebugMode) {
        print("Invalid item type");
      }
      result = false;
    }
    return result;
  }

  Future<dynamic> invokePauseDownload(String mediaId, String mediaType) async {
    dynamic result;
    try {
      result = await _dowplayPlugin.pauseDownload(mediaId, mediaType);
    } on PlatformException {
      result = false;
    }
    return result;
  }

  Future<dynamic> invokeResumeDownload(String mediaId, String mediaType) async {
    dynamic result;
    try {
      result = await _dowplayPlugin.resumeDownload(mediaId, mediaType);
    } on PlatformException {
      result = false;
    }
    return result;
  }

  Future<dynamic> invokeCancelDownload(String mediaId, String mediaType) async {
    dynamic result;
    try {
      result = await _dowplayPlugin.cancelDownload(mediaId, mediaType);
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
          child: SingleChildScrollView(
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
                Padding(
                  padding:
                      const EdgeInsets.symmetric(vertical: 24.0, horizontal: 32),
                  child: SizedBox(
                    height: 2,
                    child: Container(
                      color: Colors.black26,
                    ),
                  ),
                ),
                ElevatedButton(
                  onPressed: getDownloadsList,
                  child: const Text("Print Downloads List"),
                ),
                Visibility(
                  visible: _downloadsList.isNotEmpty,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ListView.builder(
                      shrinkWrap: true,
                      itemCount: _downloadsList.length,
                      itemBuilder: (context, index) {
                        return Text(
                          ''
                          "[${index + 1}]-> (${_downloadsList[index]['mediaType']}) : ${_downloadsList[index]['mediaType'] == "movie" ? _downloadsList[index]['object']['title'] : _downloadsList[index]['object']['info']['title']} - ${_downloadsList[index]['status']} - ${_downloadsList[index]['progress']}",
                        );
                      },
                    ),
                  ),
                ),
                ElevatedButton(
                  onPressed: startDownloadMovie,
                  child: const Text("Start Download Movie"),
                ),
                ElevatedButton(
                  onPressed: startDownloadEpisode,
                  child: const Text("Start Download Episode"),
                ),
                ElevatedButton(
                  onPressed: pauseDownload,
                  child: const Text("Pause Download"),
                ),
                ElevatedButton(
                  onPressed: resumeDownload,
                  child: const Text("Resume Download Movie"),
                ),
                ElevatedButton(
                  onPressed: cancelDownload,
                  child: const Text("Cancel Download Movie"),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
