package com.watools.statusforwhatsapp

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

object AppUtils{
    fun loadAdmobInterstitialAds(context: Context): InterstitialAd {
        var interstitialAd = InterstitialAd(context)

            interstitialAd.adUnitId = context.getString(R.string.adMob_Interstitial_LiveId)
            interstitialAd.loadAd(AdRequest.Builder().build())

        return interstitialAd
    }
    fun showAdmobInterstitialAd(interstitialAd: InterstitialAd) {
        if (interstitialAd != null && interstitialAd.isLoaded) {
            interstitialAd.show()
        }
    }
}