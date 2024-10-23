import 'dart:convert';

EpisodeMedia episodeMediaFromJson(String str) => EpisodeMedia.fromJson(json.decode(str));

String episodeMediaToJson(EpisodeMedia data) => json.encode(data.toJson());

class EpisodeMedia {
  EpisodeMedia({
    required this.mediaType,
    required this.userId,
    required this.profileId,
    required this.token,
    required this.apiBaseUrl,
    required this.lang,
    required this.startAt,
    required this.info,
    this.mediaGroup,
    this.isDownloadEnabled
  });

  String mediaType;
  String userId;
  String profileId;
  String token;
  String apiBaseUrl;
  String lang;
  double startAt;
  Map<String,dynamic> info;
  Map<String,dynamic>? mediaGroup;
  bool? isDownloadEnabled;

  factory EpisodeMedia.fromJson(Map<String, dynamic> json) => EpisodeMedia(
    mediaType: json["media_type"],
    userId: json["user_id"],
    profileId: json["profile_id"],
    token: json["token"],
    apiBaseUrl: json["api_base_url"],
    lang: json["lang"],
    startAt: json["start_at"],
    info: json["info"],
    mediaGroup: json["media_group"],
    isDownloadEnabled: json["is_download_enabled"]
  );

  Map<String, dynamic> toJson() => {
    "media_type": mediaType,
    "user_id": userId,
    "profile_id": profileId,
    "token": token,
    "api_base_url": apiBaseUrl,
    "lang": lang,
    "start_at": startAt,
    "info": info,
    "media_group": mediaGroup,
    "is_download_enabled": isDownloadEnabled
  };
}