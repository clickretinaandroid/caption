package com.example.retrofitkt.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.retrofitkt.R
import com.example.retrofitkt.adapter.HomePageAdapter
import com.example.retrofitkt.api.ApiService
import com.example.retrofitkt.modelClass.Captions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomePage : AppCompatActivity() {
    lateinit var animationView: LottieAnimationView
    lateinit var refreshLayout: SwipeRefreshLayout
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://abhi-debug.github.io/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
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
        setContentView(R.layout.activity_home_page)
        var toolbar : Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Captions"
        animationView = findViewById(R.id.animation_view)
        animationView.visibility = View.VISIBLE
        refreshLayout = findViewById(R.id.itemsswipetorefresh)

        refreshLayout.setOnRefreshListener {
            fetchFeeds()
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

    private fun showData(captionsList: List<Captions>) {
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = HomePageAdapter(context, captionsList)
        }
    }

    fun disconnected() {
        recyclerView.visibility = View.INVISIBLE
        internetGone.visibility = View.VISIBLE
        //Snackbar.make(applicationContext,"Internet Not Connected",Snackbar.LENGTH_SHORT).show()
    }

    fun connected() {
        recyclerView.visibility = View.VISIBLE
        internetGone.visibility = View.INVISIBLE
        fetchFeeds()
    }

    private fun fetchFeeds() {
        refreshLayout.isRefreshing = true
        val api = retrofit.create(ApiService::class.java)
        api.fetchUser().enqueue(object : Callback<List<Captions>> {

            override fun onResponse(
                call: Call<List<Captions>>,
                response: Response<List<Captions>>
            ) {
                refreshLayout.isRefreshing = false
                showData(response.body()!!)
                if (response.code() == 200)
                    animationView.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Captions>>, t: Throwable) {
                refreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
