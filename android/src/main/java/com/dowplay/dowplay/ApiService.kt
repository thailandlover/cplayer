package com.dowplay.dowplay

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {
    @POST("mobile/v4/watching/add")
    @FormUrlEncoded
    fun sendPostData(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Header("Accept-Language") acceptLanguage: String,
        @Header("Accept-Type") acceptType: String,

        @Field("profile_id") profileId: String,
        @Field("media_type") mediaType: String,
        @Field("media_id") mediaId: String,
        @Field("duration") duration: String,
        @Field("time") time: String,

        ): Call<ResponseBody>
}