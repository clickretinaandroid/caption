package com.watools.statusforwhatsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.activity.CaptionView
import com.watools.statusforwhatsapp.modelClass.Captions
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.view_caption_title.view.*

class HomePageAdapter(private val context: Context, private val captionsList: List<Captions>) :
    RecyclerView.Adapter<HomePageAdapter.MyViewHolder>() {

    private lateinit var mInterstitialAd: InterstitialAd
    var urlForNextActivity = arrayOfNulls<String?>(captionsList.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_caption_title, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return captionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        createInterstitial(position)
        val captions = captionsList[position]
        holder.setData(captions, position)
        holder.itemView.setOnClickListener {
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

        fun setData(Captions: Captions?, pos: Int) {
            Captions?.let {
                itemView.tv_caption.text = Captions.title
                urlForNextActivity[pos] = Captions.url
            }
        }
    }

    private fun createInterstitial(position: Int) {
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                intentToCaptionActivity(position)
            }
        }
    }
}