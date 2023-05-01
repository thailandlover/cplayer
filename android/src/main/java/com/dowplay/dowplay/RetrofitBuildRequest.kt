package com.dowplay.dowplay

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitBuildRequest {

    fun createRequestForAddWatching(url:String,profileId:String,mediaType: String,
                      mediaId: String,
                      duration: String,time: String,
                      authorization: String,
                      acceptLanguage: String){

        Log.d("Send Post Data Watch","profileId $profileId mediaId $mediaId mediaType $mediaType duration $duration time $time" +
                "url $url acceptLanguage $acceptLanguage authorization $authorization")
        val fullApiUrl = "$url/"

        val contentType = "application/x-www-form-urlencoded"
        val acceptType  = "android"
        val retrofit = Retrofit.Builder()
            .baseUrl(fullApiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        val call = apiService.sendPostData(
            contentType,
            "Bearer $authorization",
            acceptLanguage,
            acceptType,
            profileId,
            mediaType,
            mediaId,
            duration,
            time
        )

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                // handle success
                Log.d("onResponse is success", "Watching API: $response")
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // handle failure
                Log.d("onFailure is failure", "Watching API: ${t.message}")
            }
        })
    }
}