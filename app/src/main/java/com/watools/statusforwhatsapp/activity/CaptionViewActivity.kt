package com.watools.statusforwhatsapp.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.adapter.CaptionViewRvAdapter
import com.watools.statusforwhatsapp.api.ApiService
import com.watools.statusforwhatsapp.modelClass.Data
import kotlinx.android.synthetic.main.activity_caption_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CaptionViewActivity : AppCompatActivity() {
    private var fileName: String? = ""
    private var captionTitle: String? = ""
    lateinit var animationView: LottieAnimationView
    private lateinit var retrofit: Retrofit
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_caption_view)

        initViews()
        getMyUrl()
        adMobConfigure()
        setToolbar()
        retrofitConfiguration()
        loadCaptions()
    }

    private fun initViews() {
        animationView = findViewById(R.id.animation_view)
    }

    private fun getMyUrl() {
        val intent = intent
        fileName = intent.getStringExtra("fileName")
        captionTitle = intent.getStringExtra("title")
    }

    private fun adMobConfigure() {
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = getString(R.string.adMob_Interstitial_LiveId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                mInterstitialAd.loadAd(adRequest)
            }
        }
    }
    private fun setToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.title = captionTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun retrofitConfiguration() {
        retrofit = Retrofit.Builder()
            .baseUrl("https://abhi-debug.github.io/Caption/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    private fun showData(userList: List<Data>) {
        captionRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CaptionViewActivity)
            adapter = CaptionViewRvAdapter(context, userList,mInterstitialAd)
        }
    }

    private fun loadCaptions() {
        captionRecyclerView.visibility = View.VISIBLE
        val api = retrofit.create(ApiService::class.java)
        api.fetchData(fileName).enqueue(object : Callback<List<Data>> {
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
            Snackbar.make(coordinator, "Something Went Wrong", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    loadCaptions()
                }
                .setActionTextColor(Color.RED)
        snackbar.show()
    }
}
