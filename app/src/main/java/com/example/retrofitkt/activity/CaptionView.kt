package com.example.retrofitkt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitkt.api.ApiService
import com.example.retrofitkt.modelClass.Data
import com.example.retrofitkt.R
import com.example.retrofitkt.adapter.CaptionViewAdapter
import kotlinx.android.synthetic.main.activity_caption_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CaptionView : AppCompatActivity() {
    private var myValue: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caption_view)
        val intent = intent
        myValue = intent.getStringExtra("URL")
        val retrofit = Retrofit.Builder()
            .baseUrl(myValue)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ApiService::class.java)
        api.fetchData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                showData(response.body()!!)
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
            }
        })
    }

    private fun showData(userList: List<Data>) {
        rv.apply {
            layoutManager = LinearLayoutManager(this@CaptionView)
            adapter = CaptionViewAdapter(context, userList)
        }
    }
}
