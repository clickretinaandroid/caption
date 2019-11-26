package com.watools.statusforwhatsapp.api

import com.watools.statusforwhatsapp.modelClass.Data
import com.watools.statusforwhatsapp.modelClass.Captions
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {


    @GET("Caption/caption_home.json")
    fun fetchUser() : Call<List<Captions>>


    @GET(" ")
    fun fetchData(): Call<List<Data>>
}