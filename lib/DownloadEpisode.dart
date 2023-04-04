import 'dart:convert';

DownloadEpisode downloadEpisodeFromJson(String str) => DownloadEpisode.fromJson(json.decode(str));

String downloadEpisodeToJson(DownloadEpisode data) => json.encode(data.toJson());

class DownloadEpisode {
  DownloadEpisode({
    required this.mediaType,
    required this.userId,
    required this.profileId,
    required this.info,
    this.mediaGroup,
  });

  String mediaType;
  String userId;
  String profileId;
  Map<String,dynamic> info;
  Map<String,dynamic>? mediaGroup;

  factory DownloadEpisode.fromJson(Map<String, dynamic> json) => DownloadEpisode(
    mediaType: json["media_type"],
    userId: json["user_id"],
    profileId: json["profile_id"],
    info: json["info"],
    mediaGroup: json["media_group"],
  );

  Map<String, dynamic> toJson() => {
    "media_type": mediaType,
    "user_id": userId,
    "profile_id": profileId,
    "info": info,
    "media_group": mediaGroup,
  };
}
