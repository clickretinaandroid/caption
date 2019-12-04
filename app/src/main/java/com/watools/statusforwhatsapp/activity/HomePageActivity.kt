package com.watools.statusforwhatsapp.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.ads.AdListener
import com.watools.statusforwhatsapp.NetworkUtil
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.adapter.HomePageRvAdapter
import com.watools.statusforwhatsapp.api.ApiService
import com.watools.statusforwhatsapp.modelClass.Captions
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.onesignal.OneSignal
import kotlinx.android.synthetic.main.activity_home_page.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class HomePageActivity : AppCompatActivity() {
    lateinit var animationView: LottieAnimationView
    private lateinit var retrofit: Retrofit
    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        initViews()
        oneSignalConfigure()
        adMobConfigure()
       // setToolbar()
        retrofitConfiguration()
        // createNetworkConfigure()

        if (NetworkUtil.isNetworkAvailable(this@HomePageActivity)) {
            fetchCaptionTitle()
        } else {
            showSnackBarDisconnected()
        }
    }

    private fun initViews() {
        animationView = findViewById(R.id.animation_view)
    }

    private fun oneSignalConfigure() {
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
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

   /* private fun setToolbar() {
        setSupportActionBar(toolbar as Toolbar?)
        supportActionBar?.title = "   " + "Status For Whatsapp"
        val drawable = resources.getDrawable(R.drawable.icon_caption)
        val bitmap = (drawable as BitmapDrawable).bitmap
        val newDrawable: Drawable =
            BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 100, 100, true))
        supportActionBar?.setLogo(newDrawable)
    }
*/
    private fun retrofitConfiguration() {
        retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.baseUrl))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun fetchCaptionTitle() {
        captionTitleRecyclerView.visibility = View.VISIBLE

        val api = retrofit.create(ApiService::class.java)
        api.fetchUser().enqueue(object : Callback<List<Captions>> {

            override fun onResponse(
                call: Call<List<Captions>>,
                response: Response<List<Captions>>
            ) {
                showData(response.body()!!)
                if (response.code() == 200)
                    animationView.visibility = View.GONE
            }

            override fun onFailure(call: Call<List<Captions>>, t: Throwable) {
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                showSnackBarDisconnected()
            }
        })
    }

    private fun showData(captionsList: List<Captions>) {
        captionTitleRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = HomePageRvAdapter(context, captionsList, mInterstitialAd)
        }
    }

    private fun showSnackBarDisconnected() {
        val snackbar: Snackbar =
            Snackbar.make(coordinator, "No Internet Available", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") {
                    fetchCaptionTitle()
                }
                .setActionTextColor(Color.RED)
        snackbar.show()
    }
}