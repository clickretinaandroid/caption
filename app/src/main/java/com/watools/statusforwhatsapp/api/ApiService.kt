package com.watools.statusforwhatsapp.api

import com.watools.statusforwhatsapp.modelClass.Data
import com.watools.statusforwhatsapp.modelClass.Captions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {


    @GET("Caption/caption_home.json")
    fun fetchUser() : Call<List<Captions>>


    @GET("Caption/{All}.json")
    fun fetchData(@Path("All")postId:String?): Call<List<Data>>
}