package com.watools.statusforwhatsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.activity.CaptionView
import com.watools.statusforwhatsapp.modelClass.Captions
import kotlinx.android.synthetic.main.view_caption_title.view.*

class HomePageAdapter(private val context: Context, private val captionsList: List<Captions>) :
    RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    private lateinit var mInterstitialAd: InterstitialAd
    var urlForNextActivity = arrayOfNulls<String?>(captionsList.size)
    var positionForNextActivity: Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_caption_title, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return captionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        createInterstitial()
        val captions = captionsList[position]
        holder.setData(captions, position)
        holder.itemView.setOnClickListener {
            positionForNextActivity=position

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                intentToCaptionActivity(position)
            }

        }
    }

    private fun intentToCaptionActivity(position: Int) {
        val intent = Intent(context, CaptionView::class.java)
        intent.putExtra("URL", urlForNextActivity[position])
        context.startActivity(intent)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(captions: Captions?, pos: Int) {
            captions?.let {
                itemView.tv_caption.text = captions.title
                urlForNextActivity[pos] = captions.url
            }
        }
    }

    private fun createInterstitial() {
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = context.getString(R.string.adMob_Interstitial_LiveId)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                intentToCaptionActivity(positionForNextActivity)
            }
        }
    }
}