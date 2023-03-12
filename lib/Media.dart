import 'dart:convert';

Media mediaFromJson(String str) => Media.fromJson(json.decode(str));

String mediaToJson(Media data) => json.encode(data.toJson());

class Media {
  Media({
    required this.title,
    required this.subTitle,
    required this.url,
    required this.mediaId,
    required this.mediaType,
    required this.userId,
    required this.profileId,
    required this.token,
    required this.apiBaseUrl,
    required this.lang,
    required this.startAt,
    required this.info,
    this.mediaGroup,
  });

  String title;
  String subTitle;
  String url;
  String mediaId;
  String mediaType;
  String userId;
  String profileId;
  String token;
  String apiBaseUrl;
  String lang;
  double startAt;
  Map<String,dynamic> info;
  Map<String,dynamic>? mediaGroup;

  factory Media.fromJson(Map<String, dynamic> json) => Media(
    title: json["title"],
    subTitle: json["sub_title"],
    url: json["url"],
    mediaId: json["media_id"],
    mediaType: json["media_type"],
    userId: json["user_id"],
    profileId: json["profile_id"],
    token: json["token"],
    apiBaseUrl: json["api_base_url"],
    lang: json["lang"],
    startAt: json["start_at"],
    info: json["info"],
    mediaGroup: json["media_group"],
  );

  Map<String, dynamic> toJson() => {
    "title": title,
    "sub_title": subTitle,
    "url": url,
    "media_id": mediaId,
    "media_type": mediaType,
    "user_id": userId,
    "profile_id": profileId,
    "token": token,
    "api_base_url": apiBaseUrl,
    "lang": lang,
    "start_at": startAt,
    "info": info,
    "media_group": mediaGroup,
  };
}
