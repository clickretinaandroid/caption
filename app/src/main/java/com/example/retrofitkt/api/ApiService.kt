package com.example.retrofitkt.api

import com.example.retrofitkt.modelClass.Data
import com.example.retrofitkt.modelClass.Captions
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {


    @GET("Caption/caption_home.json")
    fun fetchUser() : Call<List<Captions>>


    @GET(" ")
    fun fetchData(): Call<List<Data>>
}