package com.example.retrofitkt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.retrofitkt.api.ApiService
import com.example.retrofitkt.R
import com.example.retrofitkt.adapter.HomePageAdapter
import com.example.retrofitkt.modelClass.Captions
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://abhi-debug.github.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.fetchUser().enqueue(object : Callback<List<Captions>> {

            override fun onResponse(call: Call<List<Captions>>, response: Response<List<Captions>>) {
                showData(response.body()!!)
            }
            override fun onFailure(call: Call<List<Captions>>, t: Throwable) {
            }
        })
    }
    private fun showData(captionsList: List<Captions>) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = HomePageAdapter(context, captionsList)
        }

    }
}
