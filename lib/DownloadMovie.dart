import 'dart:convert';

DownloadMovie downloadMovieFromJson(String str) => DownloadMovie.fromJson(json.decode(str));

String downloadMovieToJson(DownloadMovie data) => json.encode(data.toJson());

class DownloadMovie {
  DownloadMovie({
    required this.title,
    required this.subTitle,
    required this.url,
    required this.mediaId,
    required this.mediaType,
    required this.userId,
    required this.profileId,
    required this.info,
  });

  String title;
  String subTitle;
  String url;
  String mediaId;
  String mediaType;
  String userId;
  String profileId;
  Map<String,dynamic> info;

  factory DownloadMovie.fromJson(Map<String, dynamic> json) => DownloadMovie(
    title: json["title"],
    subTitle: json["sub_title"],
    url: json["url"],
    mediaId: json["media_id"],
    mediaType: json["media_type"],
    userId: json["user_id"],
    profileId: json["profile_id"],
    info: json["info"],
  );

  Map<String, dynamic> toJson() => {
    "title": title,
    "sub_title": subTitle,
    "url": url,
    "media_id": mediaId,
    "media_type": mediaType,
    "user_id": userId,
    "profile_id": profileId,
    "info": info,
  };
}
