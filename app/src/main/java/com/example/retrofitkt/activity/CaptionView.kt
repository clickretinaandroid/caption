package com.example.retrofitkt.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.retrofitkt.R
import com.example.retrofitkt.adapter.CaptionViewAdapter
import com.example.retrofitkt.api.ApiService
import com.example.retrofitkt.modelClass.Data
import kotlinx.android.synthetic.main.activity_caption_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CaptionView : AppCompatActivity() {
    private var myValue: String? = ""
    lateinit var animationView: LottieAnimationView
    private lateinit var retrofit: Retrofit
    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val notConnected = intent.getBooleanExtra(
                ConnectivityManager
                    .EXTRA_NO_CONNECTIVITY, false
            )
            if (notConnected) {
                disconnected()
            } else {
                connected()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caption_view)
        animationView = findViewById(R.id.animation_view)
        animationView.playAnimation()
        val intent = intent
        myValue = intent.getStringExtra("URL")
        retrofit = Retrofit.Builder()
            .baseUrl(myValue)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }
    private fun showData(userList: List<Data>) {
        rv.apply {
            layoutManager = LinearLayoutManager(this@CaptionView)
            adapter = CaptionViewAdapter(context, userList)
        }
    }

    fun disconnected() {
        rv.visibility = View.INVISIBLE
        internetGone.visibility = View.VISIBLE
    }

    fun connected() {
        rv.visibility = View.VISIBLE
        internetGone.visibility = View.INVISIBLE
        fetchFeeds()
    }

    private fun fetchFeeds() {
        val api = retrofit.create(ApiService::class.java)
        api.fetchData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                showData(response.body()!!)
                if (response.code() == 200)
                    animationView.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
            }
        })
    }
}
