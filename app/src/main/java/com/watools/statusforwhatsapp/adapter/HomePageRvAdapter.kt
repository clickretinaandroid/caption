package com.watools.statusforwhatsapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.watools.statusforwhatsapp.R
import com.watools.statusforwhatsapp.activity.CaptionView
import com.watools.statusforwhatsapp.modelClass.Captions
import kotlinx.android.synthetic.main.view_caption_title.view.*

class HomePageRvAdapter(
    private val context: Context,
    private val captionsList: List<Captions>,
    private var mInterstitialAd: InterstitialAd
) :
    RecyclerView.Adapter<HomePageRvAdapter.MyViewHolder>() {

    var urlForNextActivity = arrayOfNulls<String?>(captionsList.size)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.view_caption_title, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return captionsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val captions = captionsList[position]
        holder.setData(captions, position)
        holder.itemView.setOnClickListener {

            intentToCaptionActivity(position)
        }
    }

    private fun intentToCaptionActivity(position: Int) {
        val intent = Intent(context, CaptionView::class.java)
        intent.putExtra("URL", urlForNextActivity[position])
        context.startActivity(intent)
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(captions: Captions?, pos: Int) {
            captions?.let {
                itemView.tv_caption.text = captionsList[pos].title
                urlForNextActivity[pos] = captionsList[pos].url
            }
        }
    }
}