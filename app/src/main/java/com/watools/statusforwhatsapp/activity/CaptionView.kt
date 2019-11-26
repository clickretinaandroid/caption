package com.watools.statusforwhatsapp.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.adapter.CaptionViewAdapter
import com.watools.statusforwhatsapp.api.ApiService
import com.watools.statusforwhatsapp.modelClass.Data
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_caption_view.*
import kotlinx.android.synthetic.main.activity_caption_view.adView
import kotlinx.android.synthetic.main.activity_caption_view.coordinator
import kotlinx.android.synthetic.main.activity_caption_view.toolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CaptionView : AppCompatActivity() {
    private var myUrl: String? = ""
    lateinit var animationView: LottieAnimationView
    private lateinit var retrofit: Retrofit
    lateinit var snackbar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caption_view)

        initViews()
        adMobConfigure()
        setToolbar()
        getMyUrl()
        retrofitConfiguration()
        loadCaptions()
    }

    private fun getMyUrl() {
        val intent = intent
        myUrl = intent.getStringExtra("URL")
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.title = "Status for Whatsapp"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun retrofitConfiguration() {
        retrofit = Retrofit.Builder()
            .baseUrl(myUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun initViews() {
        animationView = findViewById(R.id.animation_view)
    }

    private fun adMobConfigure() {
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun showData(userList: List<Data>) {
        captionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CaptionView)
            adapter = CaptionViewAdapter(context, userList)
        }
    }

    private fun loadCaptions() {
        captionRecyclerView.visibility = View.VISIBLE
        val api = retrofit.create(ApiService::class.java)
        api.fetchData().enqueue(object : Callback<List<Data>> {
            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                showData(response.body()!!)
                if (response.code() == 200)
                    animationView.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                showSnackBarDisconnected()
            }
        })
    }
    private fun showSnackBarDisconnected() {
        val snackbar: Snackbar =
            Snackbar.make(coordinator, "No Internet Available", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    loadCaptions()
                }
                .setActionTextColor(Color.RED)
        snackbar.show()
    }
}
